package cglib.interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 11:22 2020/5/18
 * @modified By
 */
public class CustomInterceptor implements MethodInterceptor {

	private final Object targetObject;

	/**
	 * 传入代理目标类实例对象（用于反射调用原方法）
	 *
	 * @param targetObject
	 */
	public CustomInterceptor(Object targetObject) {
		this.targetObject = targetObject;
	}

	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		System.out.println("cglib-proxy intercept");
		// 写法参照Spring AOP源码
		return methodProxy.invoke(targetObject, objects);
	}
}
