```
self-mybatis（简化版mybatis，主要用于学习mybatis的加载机制和执行原理）
└── java
     └── org.self.mybatis
          ├── annotations -- 注解相关的文件夹
          |    └── SelfSelect -- mapper接口中，方法上配置sql语句时，使用的注解
          ├── binding -- 绑定相关的文件夹
          |    ├── MapperMethod -- mapper中的方法调用（JDK动态代理调用）及处理
          |    ├── MapperProxy -- JDK动态代理的调用处理类（代理后，执行时调用MapperMethod）
          |    ├── MapperProxyFactory -- 为mapper接口动态代理的工厂（注册mapper时调用）
          |    └── MapperRegistry -- mapper注册器
          ├── builder -- builder相关的文件夹
          |    └── annotation -- 注解配置的builder的文件夹
          |         ├── MapperAnnotationBuilder -- 注解配置sql的mapper的builder
          |         └── MethodResolver -- mapper接口中的方法解析
          ├── exceptions -- 异常相关的文件夹
          |    └── PersistenceException -- 持久化异常
          ├── executor -- 执行器相关的文件夹
          |    ├── parameter -- 查询参数相关的文件夹
          |    |    ├── DefaultParameterHandler -- 默认参数处理器
          |    |    └── ParameterHandler -- 参数处理器接口
          |    ├── resultset -- 查询结果集相关的文件夹
          |    |    ├── DefaultResultSetHandler -- 默认结果集处理器
          |    |    └── ResultSetHandler -- 结果集处理器接口
          |    ├── statement -- 查询statement相关的文件夹
          |    |    ├── SelfStatementHandler -- 自定义statement处理器，用来创建statement（mybatis中有多个，此处为自定义的）
          |    |    └── StatementHandler -- statement处理器接口
          |    ├── Executor -- 执行器接口
          |    └── SelfExecutor -- 自定义执行器，用来执行数据库或缓存的查询（mybatis中有多个，此处为自定义的）
          ├── mapping -- 映射相关的文件夹
          |    ├── BoundSql -- 存储sql和查询参数的对象（类中属性被简化过）
          |    ├── Environment -- 环境配置（主要存储数据源和事务工厂对象）
          |    ├── MappedStatement -- 用来存储生成statement对象时，需要的信息
          |    └── SqlSource -- 获取（生成）BoundSql的接口
          ├── session -- 数据库session相关的文件夹
          |    └── defaults -- 默认的session相关的文件夹
          |    |    ├── DefaultSqlSession -- 默认的sqlSession
          |    |    ├── DefaultSqlSessionFactory -- 默认的sqlSession工厂
          |    |    └── SqlSessionFactory -- sqlSession工厂的接口，用来创建sqlSession
          |    ├── Configuration -- 记录mybatis主要配置的核心配置类
          |    ├── ExecutorType -- 执行器类型枚举类
          |    ├── SqlSession -- sqlSession（提供查询、获取mapper、获取数据库连接等操作）
          |    └── TransactionIsolationLevel -- 事务隔离等级枚举类
          └── transaction -- 事务相关的文件夹
               ├── managed -- 主事务（默认事务）相关的文件夹
               |    ├── ManagedTransaction -- 主事务（默认事务，未指定事务时使用）
               |    └── ManagedTransactionFactory -- 主事务（默认事务）工厂
               ├── JdbcTransaction -- Jdbc事务
               ├── JdbcTransactionFactory -- Jdbc事务工程
               ├── Transaction -- 事务接口（创建数据库连接，提交事务，事务回滚等方法）
               ├── TransactionException -- 事务一场
               └── TransactionFactory -- 事务工厂接口（创建事务）
```
### 配置及运行机制
1. （开发人员实现）传入`数据源`和`事务工厂`，创建环境配置对象`Environment`
2. （开发人员实现）创建核心配置类对象`configuration`
3. （开发人员实现）添加mapper到配置类
    1. 将mapper注册到mybatis中（代码体现在`MapperRegistry`类的`knownMappers`集合，`knownMappers`集合的`key`为接口类型，`value`为代理工厂对象）
    2. 完成接口方法注解中的sql语句的解析，并存入新建的`MappedStatement`对象
    3. 将`MappedStatement`对象存入核心配置类`configuration`中
4. （开发人员实现）创建`sqlSessionFactory`
5. （开发人员实现）从`sqlSessionFactory`中打开一个`sqlSession`
    1. 由`Environment`中的`数据源`和`事务工厂`创建一个事务对象`tx`
    2. 由`tx`创建一个执行器`executor`
    3. 由`executor`和`configuration`创建一个默认的`sqlSession`
6. （开发人员实现）从`sqlSession`中获取指定类型的mapper
    1. `sqlSession`去`configuration`查找mapper，`configuration`去`MapperRegistry`类中查找mapper
    2. 在`MapperRegistry`类中，通过`key`从`knownMappers`集合中获取代理工厂对象`mapperProxyFactory`
    3. 通过代理工厂对象`mapperProxyFactory`，创建mapper接口的实例并返回（此处使用了JDK动态代理，`MapperProxy`类为动态代理的调用处理类）
7. （开发人员实现）调用mapper的指定方法
    1. 调用mapper指定方法，`MapperProxy`类中的`invoke`方法会触发
    2. `MapperProxy`类中的`invoke`方法调用内部静态类`PlainMethodInvoker`的`invoke`方法，并创建`MapperMethod`对象的实例
    3. `PlainMethodInvoker`的`invoke`方法调用`MapperMethod`实例的`execute`方法，传入`sqlSession`和查询参数
    4. `execute`方法调用`sqlSession`的查询方法，传入完整的方法名和查询参数
    5. `sqlSession`的查询方法通过方法名，从`configuration`中获取`MappedStatement`对象
    6. 调用`executor`的查询方法，并传入`MappedStatement`对象和查询参数
        1. 在`executor`的查询方法中，创建`StatementHandler`的实例`handler`（此时将解析过的sql语句等信息存入实例）
        2. 获取数据库连接`connection`（通过`connection`的`prepareStatement`方法设置sql语句），并生成查询用的`statement`对象（期间会设置查询参数相关信息）
        3. 调用`handler`的`query`(执行查询)，接收到返回结果集后，使用`ResultSetHandler`的实例对结果集进行处理
        4. 返回查询结果
