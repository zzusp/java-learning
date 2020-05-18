package jdk;

import jdk.dao.UserDao;
import jdk.dao.impl.UserDaoImpl;
import jdk.handler.CustomInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * @author Aaron.Sun
 * @description Jdk动态代理演示
 * @date Created in 10:11 2020/5/18
 * @modified By
 */
public class JdkDynamicProxy {

	public static void main(String[] args) {
		UserDao userDao = (UserDao) Proxy.newProxyInstance(getDefaultClassLoader(), new Class[]{UserDao.class},
				new CustomInvocationHandler(new UserDaoImpl()));
		System.out.println(userDao.findByName("张三"));
	}

	/**
	 * 获取ClassLoader
	 * Copy form org.springframework.util#getDefaultClassLoader
	 *
	 * @return
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;

		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable var3) {
		}

		if (cl == null) {
			cl = JdkDynamicProxy.class.getClassLoader();
			if (cl == null) {
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (Throwable var2) {
				}
			}
		}

		return cl;
	}

}
