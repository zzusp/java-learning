## Bean工厂（BeanFactory）
`BeanFactory`API为Spring的IoC功能提供了基础。主要用于与Spring的其他部分以及与相关的第三方框架集成，
它的`DefaultListableBeanFactory`实现是更高级别的`GenericApplicationContext`容器内的关键委托。

`BeanFactory`和相关接口（例如`BeanFactoryAware`，`InitializingBean`，`DisposableBean`）是其他框架组件的重要集成点。
通过不需要任何注释，甚至不需要反射，它们可以在容器及其组件之间进行非常有效的交互。
应用程序级`bean`可以使用相同的回调接口，但通常更喜欢通过注释或通过编程配置进行声明式依赖注入。

请注意，`BeanFactory`核心级别的API及其`DefaultListableBeanFactory`实现，不对配置格式或要使用的任何组件注释进行假设。
所有这些都是通过扩展（例如`XmlBeanDefinitionReader`和`AutowiredAnnotationBeanPostProcessor`）引入的，
并以核心元数据表示形式对共享`BeanDefinition`对象进行操作。这就是使Spring的容器如此灵活和可扩展的本质。

### BeanFactory or ApplicationContext?
本节说明`BeanFactory`和`ApplicationContext`容器之间的区别以及对引导的影响。

除非将`GenericApplicationContext`及其子类`AnnotationConfigApplicationContext`作为自定义引导的常见实现，否则都应使用`ApplicationContext`。

以下是Spring核心容器的所有常见用途的主要切入点:
* 加载配置文件
* 触发类路径扫描
* 以编程方式注册Bean定义和带注释的类
* 以及（从5.0版本开始）注册功能性Bean定义

因为`ApplicationContext`（是`BeanFactory`的子接口）包含`BeanFactory`的所有功能，所以通常建议使用普通的`BeanFactory`，除非需要完全控制Bean处理的方案。
在`ApplicationContext`（例如`GenericApplicationContext`实现）中，可以按照约定（即，按bean名称或按bean类型（尤其是后处理器））检测到几种bean，而普通的`DefaultListableBeanFactory`则与任何特殊bean无关。

对于许多容器的扩展功能，例如注释处理和AOP代理，`BeanPostProcessor`扩展点是必不可少的。
如果仅使用普通的`DefaultListableBeanFactory`，在默认情况下不会检测到后处理器并将其激活。这种情况下就可能会造成混淆，因为您的`bean`配置实际上并没有错。
而在这种情况下，需要通过其他设置完全引导容器。

下表列出了BeanFactory和ApplicationContext接口和实现所提供的功能。

 特征  | BeanFactory  | ApplicationContext
 ---- | ----- | -----
  Bean实例化/注入  | Yes  | Yes
  集成生命周期管理  | No  | Yes
  `BeanPostProcessor`的自动注册  | No  | Yes
  `BeanFactoryPostProcessor`的自动注册  | No  | Yes
  方便的`MessageSource`访问（用于内部化）  | No  | Yes
  内置的`ApplicationEvent`发布机制  | No  | Yes

要向`DefaultListableBeanFactory`显式注册Bean的后处理器，需要以编程方式调用`addBeanPostProcessor`，如以下示例所示：
```text
DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
// 添加bean定义到工厂

// 注册任何需要的BeanPostProcessor实例
factory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
factory.addBeanPostProcessor(new MyBeanPostProcessor());

// 开始使用工厂
```
要将`BeanFactoryPostProcessor`应用于普通的`DefaultListableBeanFactory`，您需要调用其postProcessBeanFactory方法，如以下示例所示：
```text
DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
reader.loadBeanDefinitions(new FileSystemResource("beans.xml"));

// 从Properties文件中取出property数据
PropertySourcesPlaceholderConfigurer cfg = new PropertySourcesPlaceholderConfigurer();
cfg.setLocation(new FileSystemResource("jdbc.properties"));

// 开始实际的更换
cfg.postProcessBeanFactory(factory);
```
在这两种情况下，显式的注册步骤都不方便，这就是为什么在Spring支持的应用程序中，
各种`ApplicationContext`变量比普通的`DefaultListableBeanFactory`更为可取的原因，
尤其是在典型企业设置中依赖`BeanFactoryPostProcessor`和`BeanPostProcessor`实例来扩展容器功能时。

`AnnotationConfigApplicationContext`已注册了所有常见的注释的后处理器，并且可以通过配置注释（例如`@EnableTransactionManagement`）在幕后引入其他处理器。
在Spring基于注释的配置模型的抽象级别上，bean的后处理器的概念仅是内部容器详细信息。
