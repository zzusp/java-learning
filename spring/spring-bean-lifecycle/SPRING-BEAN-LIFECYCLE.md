## spring bean的完整生命周期
* BeanFactoryPostProcessor接口实现类的实例化
* 调用BeanFactoryPostProcessor接口的postProcessBeanFactory()方法，在需要修改`bean`实例的配置元数据时使用（如：数据源配置（即将${datasource.url}等替换为properties文件中对应的值））
* BeanPostProcessor接口实现类的实例化
* InstantiationAwareBeanPostProcessorAdapter接口(BeanPostProcessor接口的子接口)实现类的实例化
* 实例化bean，对象的构造方法被调用
* 通过setter注入bean对象的属性
* 调用InstantiationAwareBeanPostProcessorAdapter接口的postProcessProperties()方法
* Aware接口（感知接口）
    * BeanNameAware接口的setBeanName()方法被调用
    * BeanFactoryAware接口的setBeanFactory()方法被调用
    * ApplicationContextAware接口的setApplicationContext()方法被调用
* 调用BeanPostProcessor接口的postProcessBeforeInitialization()方法
* 调用InstantiationAwareBeanPostProcessorAdapter接口的postProcessBeforeInitialization()方法
* 生命周期初始化方法
    * 配置@PostContruct注解的方法被调用
    * InitializingBean接口的afterPropertiesSet()方法被调用
    * init-method配置的方法被调用
* 调用BeanPostProcessor接口的postProcessAfterInitialization()方法
* 调用InstantiationAwareBeanPostProcessorAdapt接口的postProcessAfterInitialization()方法
* SmartLifecyle接口的相关方法（可不用特别关注）
* ========容器初始化完成========
* ========容器开始关闭========
* SmartLifecyle接口的相关方法（可不用特别关注）
* 生命周期销毁方法
      * 配置@Predestory注解的方法被调用
      * DiposibleBean接口的destory()方法被调用
      * destory-method配置的方法被调用


## Spring Bean的完整生命周期包括以下阶段：

1. **实例化（Instantiation）**：在这个阶段，Spring容器会根据配置文件或注解创建Bean的实例。

2. **设置属性值（Populate Properties）**：在实例化之后，Spring通过依赖注入的方式为Bean注入属性。

3. **设置Bean名称（Set Bean Name）**：Spring会通过Bean的名称或者标签来设置Bean的名称。

4. **BeanPostProcessor的前置处理（BeanPostProcessor Pre Initialization）**：在这个阶段，BeanPostProcessor的实现类会对Bean进行一些前置处理。

5. **初始化（Initialization）**：在调用BeanPostProcessor的postProcessBeforeInitialization()方法之后，Spring会调用Bean对象的初始化方法进行一些初始化操作。

6. **BeanPostProcessor的后置处理（BeanPostProcessor Post Initialization）**：在初始化完成后，BeanPostProcessor的实现类会对Bean进行一些后置处理。

7. **Bean准备就绪（Initialization finished）**：在这个阶段，Bean已经初始化完成，可以被应用程序使用了。

8. **销毁（Destroy）**：当Bean不再需要的时候，Spring会调用Bean的销毁方法进行清理工作。

这就是Spring Bean的完整生命周期，Spring框架通过BeanPostProcessor接口和InitializingBean、DisposableBean接口提供了丰富的扩展点，可以在Bean生命周期中的各个时机进行个性化的操作。