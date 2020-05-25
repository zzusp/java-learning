```
self-mybatis-spring（简化版mybatis-spring，主要用于学习在spring中，mybatis的加载机制和执行原理）
└── java
     └── org.self.mybatis.spring
          ├── annotations -- 注解相关的文件夹
          |    ├── MapperScannerRegistrar -- mapper扫描注册器
          |    ├── SelfMapperScan -- 自定义mapper扫描注解（配置需要扫描的mapper的路径）
          |    └── SelfMapperScans -- 自定义mapper扫描注解，可配置多个（配置需要扫描的mapper的路径）
          ├── mapper -- mapper相关的文件夹
          |    ├── ClassPathMapperScanner -- mapper扫描器
          |    ├── MapperFactoryBean -- mapper的工厂bean（用来生成mapper实例）
          |    └── MapperScannerConfigurer -- 配置mapper扫描器（并将ClassPathMapperScanner注入到spring容器中）
          ├── support -- 帮助相关的文件夹
          |    └── SqlSessionDaoSupport -- sqlSessionDao帮助类（继承spring的DaoSupport）
          ├── transaction -- 事务相关的文件夹
          |    ├── SpringManagedTransaction -- spring管理的事务
          |    └── SpringManagedTransactionFactory -- spring管理的事务的工厂
          ├── MyBatisExceptionTranslator -- 异常处理
          ├── MyBatisSystemException -- mybatis的系统异常
          ├── SqlSessionFactoryBean -- ssqlSessionFactory的工厂bean（负责sqlSessionFactory对象的实例化）
          ├── SqlSessionHolder -- 负责记录当前sqlSession的持有者
          ├── SqlSessionTemplate -- 主要负责代理所有sqlSession的查询
          └── SqlSessionUtils -- sqlSession的工具类（使用sqlSessionFactory创建sqlSession，记录、切换SqlSessionHolder等）
```
### 配置及运行机制
1. （开发人员实现）在spring的配置类中，添加mybatis的扫描注解（`@SelfMapperScan`为自实现的注解），并指定mapper所在路径
2. （开发人员实现）在spring的配置类中，配置数据源
3. （开发人员实现）在spring的配置类中，配置`SqlSessionFactory`
    1. 将数据源注入到新建的`SqlSessionFactoryBean`实例`factoryBean`中（`factoryBean`在spring专题有讲解，此处不做详述）
4. 到此由开发人员进行的配置项都已完成，后续操作都由spring容器完成
5. 容器首先进行`@SelfMapperScan`注解的处理
    1. 因`@SelfMapperScan`注解中配置了`@Import({MapperScannerRegistrar.class})`，
    且`MapperScannerRegistrar`类实现了`ImportBeanDefinitionRegistrar`接口，所以会将`MapperScannerRegistrar`加载到容器中
    （此时并没有注册为bd，而是存储到`org.springframework.context.annotation.ConfigurationClass`类中的`importBeanDefinitionRegistrars`属性中）
    2. 在容器在处理所有`ImportBeanDefinitionRegistrar`接口的实现时，会触发`MapperScannerRegistrar`类中的`registerBeanDefinitions`方法
    3. `registerBeanDefinitions`方法中会创建一个`MapperScannerConfigurer`的`BeanDefinitionBuilder`实例`builder`
    4. 将`@SelfMapperScan`注解中的配置，添加到`builder`中，创建一个bd（`MapperScannerConfigurer`类型的bd）并注入到容器中
        * 如果使用的`@SelfMapperScans`注解，则会调用`MapperScannerRegistrar`类中的静态内部类`RepeatingRegistrar`的方法，注册多个bd（`MapperScannerConfigurer`类型的bd）到容器
6. `MapperScannerConfigurer`类实现了`BeanDefinitionRegistryPostProcessor`, `InitializingBean`, `ApplicationContextAware`, `BeanNameAware`四个接口
    * `BeanDefinitionRegistryPostProcessor`（bd注册后处理）：
        1. 负责property文件的读取，并设置到当前对象中
        2. 创建`ClassPathMapperScanner`对象实例，扫描指定路径下的所有接口、类等，并完成扫描到的接口、类等的bd注册（bd的`beanClass`属性的值为`MapperFactoryBean`的类型）。
    * `InitializingBean`（初始化回调）：
        1. 扫描路径（`basePackage`属性）的非空判断
    * `ApplicationContextAware`（应用上下文感知接口）：
        1. 获取应用上下文`applicationContext`
    * `BeanNameAware`(bean名称的感知接口)：
        1. 获取bean名称`beanName`
7. 到这里为止，完成了指定路径的扫描，和扫描路径下接口、类等的bd注册。然后开始容器中所有bd的实例化。
    * mapper的bd实例化时，`SqlSessionTemplate`如何注入到`MapperFactoryBean`实例中的：
    mapper在spring容器中的bd的`beanClass`属性的值为`MapperFactoryBean`，所以对应的实例归容器管理。
    因为`MapperFactoryBean`类中存在`SqlSessionTemplate`类型的属性和对应的setter方法，所以容器在实例化时完成了自动注入（注入时如果为null则新建）
        * 实例化`SqlSessionTemplate`时，使用JDK动态代理实现了`SqlSession`接口，调用处理对象为`SqlSessionTemplate`的内部类`SqlSessionInterceptor`,
        其重写了`invoke`方法，方法中每次调用都会从`sqlSessionFactory`中获取（实际为创建）一个`sqlSession`，用于执行查询。
        （这就解释了为什么mybatis与spring整合后，一级缓存会失效，因为在mybatis中，一级缓存是基于sqlSession的）
    * mapper的bd实例化时，mapper是如何注册到mybatis中的：
    mapper在spring容器中的bd的`beanClass`属性的值为`MapperFactoryBean`，又因为`MapperFactoryBean`继承了`SqlSessionDaoSupport`，
    而`SqlSessionDaoSupport`又继承了`org.springframework.dao.support.DaoSupport`，`DaoSupport`又实现了`InitializingBean`接口，
    并在`afterPropertiesSet`回调方法中调用了`checkDaoConfig`方法。`MapperFactoryBean`重写了`checkDaoConfig`方法，并在方法中将mapper接口注入到mybatis。
8. 到此为止，容器初始化过程结束。接下来是用户从容器中获取mapper实例
    1. 用户指定mapper的类型或name等，容器根据指定内容查找对应的bean实例，找到了`beanClass`属性为`MapperFactoryBean`类
    2. 因为`MapperFactoryBean`类实现了FactoryBean接口，所以通过`getObject()`方法返回期望的实例
    3. `getObject()`方法中，找到了`SqlSessionTemplate`，然后调用mybatis的`Configuration`类的`getMapper`方法返回了mapper实例
    （该实例为mybatis使用JDK动态代理生成的代理对象，不过`sqlSession`为当前的`SqlSessionTemplate`实例）
9. 执行mapper对应的方法
    1. 因为从容器中获取到的mapper的实例的`sqlSession`属性为`SqlSessionTemplate`，
    所以执行的方法都需要`SqlSessionTemplate`做一层代理，代理到mybatis，执行后返回结果。
