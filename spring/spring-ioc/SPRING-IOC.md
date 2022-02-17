## spring-ioc简介
IoC也称为依赖注入（DI）。在此过程中，对象仅通过构造函数参数，工厂方法的参数或在构造或从工厂方法返回后在对象实例上设置的属性来
定义其依赖项（即，与它们一起使用的其他对象）。然后，容器在创建bean时注入那些依赖项。此过程从根本上讲是通过使用类的直接构造或
诸如服务定位器模式之类的方法来控制其依赖项的实例化或位置的bean本身的逆过程（因此称为Inversion of Control）。

在`org.springframework.beans`和`org.springframework.context`包是Spring框架的IoC容器的基础。该`BeanFactory`界面提供了一
种高级配置机制，能够管理任何类型的对象。`ApplicationContext`是`BeanFactory`的子接口。它增加了：
* 与Spring的AOP功能轻松集成
* 消息资源处理（用于国际化）
* 活动发布(Event publication)
* 应用层特定的上下文，例如`WebApplicationContext`用于Web应用程序中的。

简而言之，`BeanFactory`提供了配置框架和基本功能，`ApplicationContext`增加了更多针对企业的功能。该`ApplicationContext`是对
一个`BeanFactory`的完整的超集，并在Spring的IoC容器的描述本章独占使用。

在Spring中，构成应用程序主干并由Spring IoC容器管理的对象称为`bean`。`Bean`是由Spring IoC容器实例化，组装和以其他方式管理的对象。
否则，`bean`仅仅是应用程序中许多对象之一。`Bean`及其之间的依赖关系反映在容器使用的配置元数据中。

## spring实现IOC的思路和方法
1. 将类与类之间的依赖关系（属性或者构造方法）存放在配置元数据中
2. 把需要交给容器管理的对象通过配置元数据告诉容器（`XML`、`Java annotations`，`Java code`）
3. 由spring容器统一解析配置元数据
4. 容器解析配置元数据后，获取到对象及其依赖关系
5. 根据解析后的内容，由`org.springframework.context.ApplicationContext`接口代表spring容器，完成对象（`bean`）的实例化、配置和组装

## 配置元数据（Configuration Metadata）
应用程序开发人员通过配置元数据，告诉Spring容器在应用程序中，容器负责的实例化，配置和组装的对象。

配置方式：
1. `XML-based configuration`--基于XML文件配置
2. `Annotation-based configuration`--基于注解配置（Spring 2.5引入）
3. `Java-based configuration`--基于java配置（Spring 3.0引入）
    * 从Spring 3.0开始，Spring JavaConfig项目提供的许多功能成为核心Spring Framework的一部分

## 实例化容器
* `ApplicationContext`是spring实例化工厂（容器）的接口，该工厂能够维护不同的bean及其依赖关系的注册表。
* 通过使用接口提供的方法`T getBean（String name，Class <T> requiredType）`，可以检索bean的实例。
* 换句话说，所有实例化工厂的类都实现了`ApplicationContext`这个接口

下面列出两个最常用的实例化工厂的类：
1. `ClassPathXmlApplicationContext`读取xml文件实例化工厂
    * ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
2. `AnnotationConfigApplicationContext`读取自定义配置类实例化工厂
    * AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

## Bean
* Spring IoC容器管理一个或多个bean。这些bean是使用配置文件提供给容器的配置元数据创建的（例如，以XML <bean/>定义的形式 ）。
* 在容器本身内，这些bean的定义以`BeanDefinition`接口的实例对象来表示。

The bean definition：

 属性  | 描述
 ---- | -----
 Class  | bean对应的实例化对象的类
 Name  | bean对应的命名
 Scope  | bean的作用域
 Constructor arguments  | 构造方法参数，用于依赖注入
 Properties  | set方法参数，用于依赖注入
 Autowiring mode  | 自动装配模式
 Lazy initialization mode  | 懒初始化模式
 Initialization method  | 初始化回调
 Destruction method  | 销毁回调
 
除了通过读取配置元数据实例化bean以外，`ApplicationContext`还允许注册在容器外部（由用户）创建的现有对象
1. 访问`ApplicationContext`的`getBeanFactory()`方法，返回的是`BeanFactory`接口的实现类`DefaultListableBeanFactory`
2. `DefaultListableBeanFactory`通过`registerSingleton(..)`和`registerBeanDefinition(..)`方法支持此注册

## 依赖
在java中，如果A类中使用了B类的属性或方法，那么就可以称A类依赖于B类。
### 依赖注入（Dependency Injection）
1. 依赖的定义
    1. 构造函数参数
    2. 工厂方法的参数
    3. 获取到`构造方法创建的对象实例`或`工厂方法返回的对象实例`后，在对象实例上设置的属性（Setter）
2. 容器在创建bean时注入那些依赖项

依赖注入（DI）是一个过程，是对象实例化及实例化过程中依赖查找并注入的逆过程（因此称为Inversion of Control）。

使用DI原理，代码更加简洁，解耦效果更好，当使用对象的实例时，容器会帮助我们实例化，并不需要关注其依赖项，容器会自动将关联的依赖注入，
开发更加简单，并且可以将更多的精力放在业务逻辑上。

DI存在两个主要变体：
* 基于`构造函数`的依赖注入
    * 解析构造函数的参数得到依赖
    * 通过参数的`type`，在容器中查找匹配的bean，找到后注入
* 基于`Setter`的依赖注入
    * 容器实例化bean
    * 手动调用或容器调用（根据处理后的setter方法名来匹配依赖bean）bean的setter方法完成注入
    
## 懒加载（Lazy-initialized Beans）
默认情况下，`ApplicationContext`的实现会在容器初始化过程中，创建和配置所有的单例beans。通常，这种机制是可取的，
因为可以立刻发现配置或周围环境中的错误。当然，如果不希望这种情况，spring也提供了延迟加载的机制，通过参数的配置即可实现。

懒加载：
1. xml中的配置：
   ```xml
    <!-- 方式2：default-lazy-init="true" -->
     <beans default-lazy-init="true">
       <!-- 方式1：lazy-init="true" -->
       <bean id="lazy" class="xxx.xxx.xxxBean" lazy-init="true"/> 
    </beans>
   ```
  
2. 注解方式的配置：`@Lazy`

## 自动装配
spring容器可以为有依赖的bean自动装配依赖关系。你可以通过spring检查`ApplicationContext`的内容，来解析bean的协作者（依赖的其他beans）

自动装配的优点：
* 自动装配可以大大减少指定属性或构造函数参数的需要

自动装配功能具有四种模式。您可以为每个bean指定自动装配，因此可以选择要自动装配的装配。
下表描述了四种自动装配模式：

 模式  | 说明
 ---- | -----
 no  | （默认）无自动装配。Bean引用必须由`ref`元素定义。对于较大的部署，建议不要更改默认设置，因为明确指定协作者可以提供更好的控制和清晰度。在某种程度上，它记录了系统的结构。
 byName  | 按属性名称自动装配。Spring寻找与需要自动装配的属性同名的bean。
 byType  | 如果容器中恰好存在一个该属性类型的bean，则使该属性自动装配。如果存在多个，则将引发致命异常，这表明您可能不对该bean使用`byType`自动装配。如果没有匹配的bean，则什么都不会发生（未设置该属性）
 constructor  | 与`byType`类似，但适用于构造函数参数。如果容器中不存在构造函数参数类型的一个bean，则将引发致命错误。

## Bean的作用域（Bean Scopes）
创建一个`bean definition`时，实际将创建一个配方（recipe），该配方用于创建`bean definition`中定义的类的实例。
`bean definition`就是配方的想法很重要，因为它意味着与类一样，您可以从一个配方中创建许多对象实例。

补充：个人理解，配方（recipe）指的就是spring中的`BeanDefinition`接口。

spring支持6种bean的作用域

 作用域  | 说明
 ---- | -----
 singleton  | （默认）将每个Spring IoC容器的单个bean定义范围限定为单个对象实例。即：单例
 prototype  | 将单个Bean定义范围限制为任意数量的对象实例。即：每次调用都重新实例化
 request  | 将单个bean定义的范围限定为单个HTTP请求的生命周期。
 session  | 将单个bean定义的作用域限定为HTTP会话的生命周期
 application  | 将单个bean定义的作用域限定为ServletContext的生命周期
 websocket  | 将单个bean定义的作用域限定为WebSocket的生命周期。
