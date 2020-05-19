## 容器拓展点（Container Extension Points）
如果开发中需要对Spring IoC容器进行拓展，那么应用开发人员不需要自己实现一个`ApplicationContext`的子类。
相反的，通过向容器中插入特殊集成的接口的实现，就可以实现容器的拓展。

## 使用`BeanPostProcessor`自定义`bean`（Customizing Beans by Using a BeanPostProcessor）
`BeanPostProcessor`接口定义了回调方法供开发人员实现，以此来提供自定义的（或覆盖容器的默认值）实例化逻辑，依赖关系解析逻辑等。
如果您想在Spring容器完成实例化，配置和初始化`bean`之后，实现一些自定义逻辑，则可以插入一个或多个自定义的`BeanPostProcessor`实现。

您可以配置多个`BeanPostProcessor`实例，并且可以通过设置`order`属性来控制这些`BeanPostProcessor`实例的执行顺序。
仅当`BeanPostProcessor`实现Ordered接口时，才可以设置此属性。

`org.springframework.beans.factory.config.BeanPostProcessor`提供了两个方法：
* `postProcessBeforeInitialization()`
    * 使用场景：`bean`的前置校验（如：有无指定注解，有无指定属性等），如果满足则正常返回，不满足则抛出异常
* `postProcessAfterInitialization()`
    * 使用场景：`bean`的后置校验（如：创建的`bean`的实例是否与期望相符等）
## 使用`BeanFactoryPostProcessor`自定义配置元数据（Customizing Configuration Metadata with a BeanFactoryPostProcessor）
`org.springframework.beans.factory.config.BeanFactoryPostProcessor`接口的语义与`BeanPostProcessor`接口相似，但有一个主要区别：
* `BeanFactoryPostProcessor`可以对`bean`的元数据进行操作。也就是说，Spring IoC容器允许`BeanFactoryPostProcessor`读取配置元数据，
并在容器实例化所有的`bean`（除了`BeanFactoryPostProcessor`实例）之前，修改配置元数据。

您可以配置多个`BeanFactoryPostProcessor`实例，并且可以通过设置`order`属性来控制这些`BeanFactoryPostProcessor`实例的执行顺序。
仅当`BeanFactoryPostProcessor`实现Ordered接口时，才可以设置此属性。

Spring中的预定义`BeanFactoryPostProcessor`：
* `PropertyOverrideConfigurer`
* `PropertySourcesPlaceholderConfigurer`
* ...

`org.springframework.beans.factory.config.BeanFactoryPostProcessor`提供了一个方法：
* `postProcessBeanFactory`
    * 使用场景：需要修改`bean`实例的配置元数据时使用（如：数据源配置等）

## `BeanPostProcessor`和`BeanFactoryPostProcessor`执行过程概述
（在第一步执行之前有部分容器初始化操作，这里不做详述，如：读取xml配置、扫描、添加`BeanDefinition`到`beanFactory`中的`beanDefinitionMap`等）
1. 调用`BeanFactoryPostProcessor`中`postProcessBeanFactory`方法
2. 在`postProcessBeanFactory`方法中，修改的配置元数据（即：`BeanDefinition`的`propertyValues`属性）
3. 实例化`bean`，实例化时使用的配置元数据是未修改之前的！
4. 取出修改后的配置元数据，通过setter方法，修改实例化后`bean`对象中对应的属性
5. 调用`BeanPostProcessor`中`postProcessBeforeInitialization()`方法
6. 调用生命周期初始化方法（如`InitializingBean.afterPropertiesSet()`、`@PostConstruct`或任何声明的`init-method`）
    1. 注解（`@PostConstruct`）
    2. 回调接口定义的方法（`afterPropertiesSet()`）
    3. 自定义配置方法（`init-method`）
7. 调用`BeanPostProcessor`中`postProcessAfterInitialization()`方法
8. `bean`实例销毁时，调用生命周期销毁方法（如`InitializingBean.destroy()`、`@PreDestroy`或任何声明的`destroy-method`）
    1. 回调接口定义的方法（`destroy()`）
    2. 自定义配置方法（`destroy-method`）
    3. 注解（`@PreDestroy`）

## `FactoryBean`自定义实例化逻辑（Customizing Instantiation Logic with a `FactoryBean`）
您可以实现`org.springframework.beans.factory.FactoryBean`接口，实现类可作为对象的工厂。

`FactoryBean`接口可以扩展Spring IoC容器的实例化逻辑。如果您初始化对象的逻辑较为复杂，使用xml配置方式可能会很冗余，使用Java代码更好表达，
那么这时候就可以使用`FactoryBean`接口来实现。创建`FactoryBean`接口的实现类，在实现类中编写复杂的初始化逻辑，然后将实现类插入容器。

`FactoryBean`接口提供了三个方法：
* `Object getObject()`：返回此工厂创建的对象的实例。实例可以共享，具体取决于该工厂返回单例还是原型。
    * 工厂创建的对象被使用时，触发该方法进行实例化
* `boolean isSingleton()`：如果`FactoryBean`返回对象实例是单例则返回`true`，否则返回`false`
* `Class getObjectType()`：返回`getObject()`方法返回的对象的类型，如果类型未知，则返回`null`

Spring框架中的许多地方都使用了`FactoryBean`概念和接口。Spring中附带了超过50个的`FactoryBean`接口实现。

当您需要从容器中获取实际的`FactoryBean`接口时，需要在`bean`的`id`前面加上`&`符号，这样容器返回的`bean`是`FactoryBean`接口的实例；
如果没有添加`&`符号，容器返回的会是`FactoryBean`接口`getObject()`方法返回的实例。

典型使用场景：
* mybatis中的`SqlSessionFactoryBean`
