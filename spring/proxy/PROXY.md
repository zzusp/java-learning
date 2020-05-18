## 动态代理
因Spring AOP中使用到了两种代理，所以将这两个代理单独提出来，做个整理。
* JDK动态代理
* CGLIB代理

### JDK动态代理
原理：动态生成一个代理类，该类实现所有指定的接口，并继承`java.lang.reflect.Proxy`类，
然后将代理类通过类加载器加载到程序中（运行时加载，非编译时加载）。
* `代理类内逻辑` 代理类的实现并不包含实际的处理逻辑，而是调用了`InvocationHandler`中的`invoke`方法，
通过`invoke`方法来调用`目标对象`（需传入`InvocationHandler`中，如通过构造方法传入）中的`目标方法`
（在此期间对`目标方法`进行拓展操作，如调用`目标方法`前的通知等），或进行其他处理逻辑（可以不包含`目标对象`，如mybatis）。
```text
/**
 * @param loader 指定代理对象的类加载器
 * @param interfaces 代理对象需要实现的接口，可以同时指定多个接口
 * @param handler 方法调用的实际处理者，代理对象的方法调用都会转发到handler类中的invoke方法
 */
Proxy.newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler handler)
```
### CGLIB代理
CGLIB(Code Generation Library)是一个基于ASM的字节码生成库，它允许我们在运行时对字节码进行修改和动态生成。

原理：动态生成一个代理类，该类继承`目标对象`的类，然后将代理类通过类加载器加载到程序中（运行时加载，非编译时加载）。
* `代理类内逻辑` 代理类会重写父类中的所有方法，在重写的方法中做如下处理：
    * 尝试获取`MethodInterceptor`对象
        * 如果对象不存在（即对象==null），则直接调用父类的方法
        * 如果存在，那么调用`MethodInterceptor`对象的`intercept`方法，在`intercept`方法中可进行`目标方法`的拓展操作，如调用`目标方法`前的通知等
```text
// 创建加强器，用来创建代理类
Enhancer enhancer = new Enhancer();
// 为加强器指定要代理的业务类（即：为下面生成的代理类指定父类）
enhancer.setSuperclass(Xxx.class);
// 设置回调：对于代理类上所有方法的调用，都会调用MethodInterceptor的实现，而MethodInterceptor则需要实现intercept()方法进行拦截
enhancer.setCallback(new XxxInterceptor(new Xxx()));
// 创建代理类对象并返回
Xxx xxx = (Xxx) enhancer.create();
```
### 总结
1. 当`目标对象`有实现接口的话，使用`JDK动态代理`；没有实现接口，使用`CGLIB代理`。
2. `JDK动态代理`和`CGLIB代理`都是在运行时生成并加载代理类，而不是编译时。
3. `JDK动态代理`生成的代理类继承`java.lang.reflect.Proxy`类，并实现`目标接口`；`CGLIB代理`生成的代理类继承`目标对象`的类。

