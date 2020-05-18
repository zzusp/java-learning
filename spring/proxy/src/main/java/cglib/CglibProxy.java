package cglib;

import cglib.dao.UserDao;
import cglib.dao.impl.UserDaoImpl;
import cglib.interceptor.CustomInterceptor;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author Aaron.Sun
 * @description cglib动态代理演示
 * @date Created in 10:53 2020/5/18
 * @modified By
 */
public class CglibProxy {

	public static void main(String[] args) {
		// 创建加强器，用来创建代理类
		Enhancer enhancer = new Enhancer();
		// 为加强器指定要代理的业务类（即：为下面生成的代理类指定父类）
		enhancer.setSuperclass(UserDaoImpl.class);
		// 设置回调：对于代理类上所有方法的调用，都会调用CallBack，而Callback则需要实现intercept()方法进行拦截
		enhancer.setCallback(new CustomInterceptor(new UserDaoImpl()));
		// 创建代理类对象并返回
		UserDao userDao = (UserDao) enhancer.create();
		System.out.println(userDao.findByName("张三"));
	}

}
