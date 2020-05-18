package jdk.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Aaron.Sun
 * @description 自定义动态代理的调用处理操作
 * @date Created in 10:38 2020/5/18
 * @modified By
 */
public class CustomInvocationHandler implements InvocationHandler {

	private final Object targetObject;

	/**
	 * 传入动态代理目标类实例对象（用于反射调用原方法）
	 *
	 * @param targetObject
	 */
	public CustomInvocationHandler(Object targetObject) {
		this.targetObject = targetObject;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("jdk-dynamic-proxy invoke");
		return method.invoke(targetObject, args);
	}

}
