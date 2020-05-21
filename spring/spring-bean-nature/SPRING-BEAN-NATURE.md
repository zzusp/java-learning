## Bean的自定义性质（Customizing the Nature of a Bean）
Spring提供了以下几个接口来自定义`bean`的性质：
* `Lifecycle Callbacks`
* `ApplicationContextAware` and `BeanNameAware`
* Other `Aware` Interfaces

### Bean的生命周期回调（Lifecycle Callbacks）
为管理spring容器中bean的生命周期，您可以实现Spring提供的`InitializingBean`和`DisposableBean`接口。
容器提供了`afterPropertiesSet()`方法（bean初始化回调），`destroy()`方法（bean销毁回调），供在Bean的初始化和销毁时，执行某些操作。

在容器内部，spring使用`BeanPostProcessor`接口的实现，来处理接口能找到的所有回调，并调用对应的回调方法。
如果您需要Spring默认不提供的自定义功能或其他生命周期行为，，您可以自己实现`BeanPostProcessor`。

除了上述的初始化和销毁的回调，spring管理的对象也可以通过实现`Lifecycle`接口，在容器自身的生命周期的驱动下参与启动和关闭过程。

Spring 2.5开始，配置生命周期的方式分为以下三种：
1. 注解（`@PostConstruct` `@PreDestroy`）
2. 回调接口定义的方法（`afterPropertiesSet()` `destroy()`）
3. 自定义配置方法（`init-method` `destroy-method`）

三种生命周期配置方式的调用先后顺序：
1. 注解（`@PostConstruct`）
2. 回调接口定义的方法（`afterPropertiesSet()`）
3. 自定义配置方法（`init-method`）
4. 注解（`@PreDestroy`）
5. 回调接口定义的方法（`destroy()`）
6. 自定义配置方法（`destroy-method`）

#### Startup and Shutdown Callbacks
`Lifecycle`接口定义的方法，是任何有生命周期需求的对象都必要的方法（例如启动和停止某些后台进程）。
```java
public interface Lifecycle {
    /** 
     * 启动此组件。
     * 如果组件已经在运行，则不应抛出异常。
     * 对于容器，这会将启动信号传播到所有适用的组件。
     */
    void start();
    /**
     * 通常以同步方式停止此组件，以便在返回此方法后完全停止该组件。
     * 当需要异步停止行为时，请考虑实现{SmartLifecycle}及其{@code stop（Runnable）}
     * 请注意，此停止通知不能保证在销毁之前发出：
     * 在常规关闭时，{Lifecycle} Bean将在传播常规销毁回调之前首先收到停止通知。
     * 但是，在上下文生存期内的热刷新或中止的刷新尝试下，将调用给定bean的destroy方法，而无需事先考虑停止信号。
     * 如果组件未运行（尚未启动），则不应引发异常。
     * 对于容器，这将把停止信号传播到所有应用的组件。
     */
    void stop();
    /** 
     * 检查此组件当前是否正在运行
     * 对于容器而言，只有当应用的所有组件当前都在运行时，才会返回{@code true}。
     * @return 组件当前是否正在运行
     */
    boolean isRunning();
}
```
任何Spring管理的对象都可以实现`Lifecycle`接口。当`ApplicationContext`接收到启动和停止信号（例，运行时需要停止/重启的场景）时，
它会级联调用上下文中声明的所有`Lifecycle`接口实现。它通过委派给`LifecycleProcessor`来做到这一点。

注：`LifecycleProcessor`接口在spring内部有默认实现`DefaultLifecycleProcessor`，经尝试，外部实现的回调方法不会被触发！！！
```java
public interface LifecycleProcessor extends Lifecycle {
    /** 上下文刷新通知，例如自动启动组件。 */
    void onRefresh();
    /** 上下文关闭阶段的通知，例如自动停止组件。 */
    void onClose();
}
```
注意`LifecycleProcessor`接口是`Lifecycle`接口的拓展（继承了`Lifecycle`接口），并新增了两个额外的方法，在上下文`刷新`和`关闭`时响应。
```text
特别注意

常规的org.springframework.context.Lifecycle接口是用于显式启动和停止通知的普通协议，并不意味着会在上下文刷新时自动启动。
为了对特定bean的自动启动（包括启动阶段）进行细粒度的控制，请考虑改为实现org.springframework.context.SmartLifecycle

另外，请注意，不能保证会在销毁之前发出停止通知。
在常规关闭时，在传播常规销毁回调之前，所有Lifecycle bean首先都会收到停止通知。
但是，在上下文生命周期内进行热刷新或刷新尝试失败时，仅调用destroy方法。
```
启动和关闭调用的顺序可能很重要。如果任何两个对象之间存在“依赖”关系，则依赖方在其依赖之后开始，而在依赖之前停止。
但是，有时直接依赖项是未知的。您可能只知道某种类型的对象应该先于另一种类型的对象开始。
在这些情况（简言之，需要控制`bean`初始化顺序）下，`SmartLifecycle`接口定义了另一个选项，即在其超级接口`Phased`上定义的`getPhase()`方法。
```java
public interface Phased {
    /** 
     * 返回此对象的相位值。
     * 个人理解：表示加载优先级（先进后出FILO）
     * 数值越小（Integer.MIN_VALUE）优先级越高；
     * 数值越大（Integer.MAX_VALUE）优先级越低；
     * 没有实现{SmartLifecycle}接口的{Lifecycle}对象的默认相位为0
     */
    int getPhase();
}
```
`SmartLifecycle`接口
```java
public interface SmartLifecycle extends Lifecycle, Phased {
    /**
     * {SmartLifecycle}默认相位值 {@code Integer.MAX_VALUE}
     * 这不同于与常规{Lifecycle}接口实现类中，相关联的公共相位值{0}，
     * 它将通常自动启动的{SmartLifecycle}类型的bean置于稍后的启动和较早的关闭
     */
    int DEFAULT_PHASE = Integer.MAX_VALUE;
    /**
     * 返回{true}：在容器{ApplicationContext}刷新时，{Lifecycle}组件应该会自动启动
     * 返回{false}：组件打算通过显式的调用{start()}方法启动，类似于普通的{Lifecycle}实现。
     * 默认实现返回{true}
     */
    default boolean isAutoStartup() {
    	return true;
    }
    /**
     * 意味着生命周期组件如果当前正在运行，则必须停止。
     * {LifecycleProcessor}使用提供的回调来支持有序和潜在的并发，关闭具有共同关闭顺序值的所有组件。回调必须在{SmartLifecycle}组件确实停止后执行。
     * {LifecycleProcessor}将仅调用{stop()}方法的变体；即{Lifecycle#stop()}将不会为{SmartLifecycle}实现调用，除非在该方法的实现内显式委托给它。
     * 默认实现委托给{#stop()}并立即在调用线程中触发给定的回调。注意，两者之间没有同步，所以希望至少在自定义实现中，将相同的步骤放入其通用生命周期监控器（如果有）中。
     */
    default void stop(Runnable callback) {
        stop();
        callback.run();
    }
    /**
     * 返回此生命周期对象应该在其中运行的相位值。
     * 默认实现返回{DEFAULT_PHASE}，以便让{stop()}回调在常规的{Lifecycle}实现之后执行。
     */
    @Override
    default int getPhase() {
        return DEFAULT_PHASE;
    }
}
```
`SmartLifecycle`定义的stop方法接受回调。任何`SmartLifecycle`接口实现都必须在该实现的关闭过程完成后调用该回调的`run()`方法。
由于`LifecycleProcessor`接口的默认实现`DefaultLifecycleProcessor`会在每个相位（阶段）内的对象组等待其超时值，以调用该回调，因此可以在必要时启用异步关闭。
默认的每阶段超时是30秒。您可以通过在上下文中定义一个名为`lifecycleProcessor`的`bean`来覆盖默认的生命周期处理器实例。
如果只想修改超时，则定义以下内容即可：
```xml
<bean id="lifecycleProcessor" class="org.springframework.context.support.DefaultLifecycleProcessor">
    <!-- timeout value in milliseconds -->
    <property name="timeoutPerShutdownPhase" value="10000"/>
</bean>
```
如前所述，`LifecycleProcessor`接口还定义了用于刷新和关闭上下文的回调方法。
* 关闭回调`onClose()`：驱动关闭过程，就好像已经显式调用了`stop()`一样，但是它在上下文关闭时发生
* 刷新回调`onRefresh()`：刷新上下文时（在所有对象都被实例化和初始化之后），该回调将被调用。
    1. 届时，默认的生命周期处理器将检查每个`SmartLifecycle`对象的`isAutoStartup()`方法返回的布尔值。
    2. 如果为true，则在该时刻开始该对象，而不是等待上下文的显式调用或自己的`start()`方法（与上下文刷新不同，对于标准的上下文实现，上下文启动不会自动发生）

#### 在非Web应用程序中正常关闭Spring IoC容器（Shutting Down the Spring IoC Container Gracefully in Non-Web Applications）
如果您在非Web应用程序环境中（例如，在客户端桌面环境中）使用Spring的IoC容器，请向JVM注册一个关闭钩子。
这样做可以确保正常关闭，并在您的`Singleton bean`上调用相关的`destroy`方法，以便释放所有资源。您仍然必须正确配置和实现这些`destroy`回调。

要注册关闭挂钩，请调用`ConfigurableApplicationContext`接口上声明的`registerShutdownHook()`方法

### `ApplicationContextAware`和`BeanNameAware`（`ApplicationContextAware` and `BeanNameAware`）
当`ApplicationContext`创建了一个`ApplicationContextAware`接口的实现类实例时，这个实例会提供对该`ApplicationContext`的引用。
ApplicationContextAware接口的定义：
```java
public interface ApplicationContextAware {
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
```
因此，`bean`可以通过`ApplicationContext`接口或通过将引用转换为该接口的已知子类（例如`ConfigurableApplicationContext`，它公开了其他功能）
来以编程方式操纵创建它们的`ApplicationContext`。
* 一种用途是通过编程方式检索其他`bean`。有时，此功能很有用。但是，通常应避免使用它，因为它将代码耦合到Spring，并且不遵循`IoC`，在该场景中，将是协作者（被依赖`bean`）作为属性提供给`bean`。

`ApplicationContext`的其他方法提供对资源文件的访问，发布应用程序事件以及访问`MessageSource`的方法。
这些附加功能在`ApplicationContext`的其他功能（https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#context-introduction）中进行了描述。

自动装配是另一种获得`ApplicationContext`的引用的方法。
传统的`constructor`和`byType`自动装配模式，可以分别提供构造器参数或设置器方法参数依赖的`ApplicationContext`类型。
要获得更大的灵活性，包括能够自动连接字段和使用多个参数方法，请使用基于注释的自动装配（`annotation-based autowiring`）功能。
如果使用基于注解的自动装配，则将`ApplicationContext`自动连接到期望使用`ApplicationContext`类型的字段、构造函数参数或方法参数中（即：如果字段，构造函数或方法带有`@Autowired`注解）。

当`ApplicationContext`创建一个实现该`org.springframework.beans.factory.BeanNameAware`接口的类时，该类将获得对其关联对象定义中定义的名称的引用。
```java
public interface BeanNameAware {
    void setBeanName(String name) throws BeansException;
}
```
在填充正常的bean属性之后，在初始化回调（例如`InitializingBean.afterPropertiesSet()`或自定义`init-method`）之前，调用该回调。

### 其他感知接口（Other Aware Interfaces）
除了`ApplicationContextAware`和`BeanNameAware`（前面已经讨论过）之外，Spring还提供了多种Aware回调接口，这些接口使Bean向容器指示它们需要某种基础结构依赖性。
通常，名称表示依赖项类型。下表总结了最重要的Aware接口：

 名称  | 依赖注入（可理解为接口方法的参数）
 ---- | -----
  `ApplicationContextAware`  | 声明`ApplicationContext`
  `ApplicationEventPublisherAware`  | 封闭`ApplicationContext`的事件发布者
  `BeanClassLoaderAware`  | 类加载器，用于加载`bean`类
  `BeanFactoryAware`  | 声明`BeanFactoryAware`
  `BeanNameAware`  | 声明`bean`的名称
  `BootstrapContextAware`  | 资源适配器`BootstrapContext`（容器在其中运行）。通常仅在支持JCA (Java EE Connector Architecture)的`ApplicationContext`实例中可用。
  `LoadTimeWeaverAware`  | 定义的编织器，用于在加载时处理类定义。
  `MessageSourceAware`  | 解决消息的已配置策略（支持参数化和国际化）
  `NotificationPublisherAware`  | Spring JMX通知发布者
  `ResourceLoaderAware`  | 配置的加载程序，用于对资源的低级别访问。
  `ServletConfigAware`  | 当前`ServletConfig`（容器在其中运行）。仅在可感知网络（web-aware）的Spring`ApplicationContext`中有效。
  `ServletContextAware`  | 当前`ServletContext`（容器在其中运行）。仅在可感知网络（web-aware）的Spring`ApplicationContext`中有效。

再次注意，使用这些接口会将您的代码与Spring API绑定在一起，并且不遵循“控制反转”。
因此，我们建议将它们用于需要以编程方式访问容器的基础结构Bean。
