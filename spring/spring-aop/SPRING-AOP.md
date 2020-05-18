## spring-aop（Aspect Oriented Programming with Spring）简介
面向切面编程（AOP）的出现，提供了另一种编程结构的思路，作为面向对象编程（OOP）的补充。
OOP中模块化的单位是class，AOP中模块化的单位是切面。切面使模块化的关注点可以跨越多种类型和对象。

AOP框架是Spring核心组件之一。尽管Spring IoC容器并不依赖于AOP，但AOP是对Spring IoC的补充，提供了功能非常强大的中间件解决方案。

### spring-aop的实现方式
* `schema-based approach` -- 基于xml文件配置
* `@AspectJ annotation style` -- 基于注解配置

### AOP在spring框架中的使用
* 提供声明式企业服务。此类服务中最重要的是声明式事务管理。
* 让用户实现自定义切面，使用AOP实现对OOP的拓展。如：
    * 日志记录
    * 权限验证
    * 效率检查
    * exception

### Spring AOP与AspectJ
1. Sprng AOP和AspectJ都是AOP的框架
2. Spring AOP和IoC与AspectJ无缝集成，并会继续向后兼容
3. Spring AOP与AspectJ的底层实现不同（织入方式不同）
4. Spring AOP的细粒度不如AspectJ
5. Spring AOP借助了AspectJ的注解

### AOP的概念
* `Aspect`（切面）：，模块化的关注点（跨越多种类型和对象）。
* `Join point`（连接点）：程序执行过程中的一个点，例如方法的执行或异常的处理。在Spring AOP中，连接点始终代表方法的执行。
* `Advice`（建议、通知）：切面在连接点处采取的操作。
* `Pointcut`（切入点）：匹配连接点的谓词（表达式）。
* `Introduction`（简介）：代表类型声明其他方法或字段。Spring AOP允许您向任何建议（`Advice`）对象引入新的接口（和相应的实现）。
可理解为`Advice`的拓展。
* `Target object`（目标对象）：被一个或多个切面（`Aspect`）建议（`Advice`）的对象。因为Spring AOP使用运行时代理实现，所以这个对象始终是代理对象（`并非原始对象`）。
* `AOP proxy`（AOP代理）：由AOP框架创建的一个对象，用于实现切面（建议方法的执行等）。Spring框架使用`JDK动态代理`或`CGLIB代理`来实现AOP代理。
* `Weaving`（织入）：将方面与其他应用程序类型或对象链接，创建建议的对象（`Target object`）。
该操作可以在编译时执行（例如，使用AspectJ编译器），加载时或在运行时完成。Spring AOP在运行时执行织入。

### Spring AOP的通知方式
* `Before advice`：在连接点之前运行，但无法阻止程序执行到连接点（除非它引发异常）。
* `After returning advice`：连接点正常完成后运行（例如，如果某个方法返回而没有引发异常）；如果异常返回则不执行
* `After throwing advice`：如果方法因抛出异常而退出时执行。
* `After (finally) advice`：无论连接点退出的方式如何（正常或异常返回），都将执行。
* `Around advice`：围绕联接点的建议，例如方法调用。这是最有力的建议。周围建议可以在方法调用之前和之后执行自定义行为。它还负责选择是返回连接点还是通过返回其自身的返回值或引发异常来捷径建议的方法执行。

### Spring AOP代理模式的选取
通过以下两种方式可以指定代理模式：
* XML配置文件中的`<aop:aspectj-autoproxy/>`标签，其中的`proxy-target-class`属性，默认为false。
* 注解配置类添加`@EnableAspectJAutoProxy`注解，其中的`proxyTargetClass`属性，默认为false。

默认为false,表示使用JDK动态代理技术织入增强；当配置为true时，表示使用CGLIB代理技术织入增强。
不过即使设置为false，如果目标类没有声明接口，则Spring将自动使用CGLIB代理。
