package service.impl;

import dao.UserDao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import service.UserService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Aaron.Sun
 * @description 用户服务接口实现类
 * @date Created in 11:48 2020/5/15
 * @modified By
 */
public class UserServiceImpl implements UserService, ApplicationContextAware, BeanFactoryAware, BeanNameAware,
		InitializingBean, DisposableBean, SmartLifecycle {

	public boolean running = false;
	private UserDao userDao;

	/**
	 * 构造方法
	 */
	public UserServiceImpl() {
		System.out.println("【构造器】调用UserServiceImpl的构造器实例化");
	}

	/**
	 * setter方法注入
	 *
	 * @param userDao dao
	 */
	public void setUserDao(UserDao userDao) {
		System.out.println("【注入属性】注入属性userDao");
		this.userDao = userDao;
	}

	@Override
	public String findByName(String name) {
		return userDao.findByName(name);
	}

	public void init() {
		System.out.println("【init-method】调用<bean>init-method属性指定的初始化方法");
	}

	public void cleanup() {
		System.out.println("【destroy-method】调用<bean>的destroy-method属性指定的初始化方法");
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("【DiposibleBean接口】调用InitializingBean.destroy()");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("【InitializingBean接口】调用InitializingBean.afterPropertiesSet()");
	}

	@Override
	public void start() {
		this.running = true;
		System.out.println("【SmartLifecycle接口】SmartLifecycle.start()");
	}

	@Override
	public void stop() {
		this.running = false;
		System.out.println("【SmartLifecycle接口】SmartLifecycle.stop()");
	}

	@Override
	public boolean isRunning() {
		System.out.println("【SmartLifecycle接口】SmartLifecycle.isRunning()");
		return this.running;
	}

	@Override
	public boolean isAutoStartup() {
		System.out.println("【SmartLifecycle接口】SmartLifecycle.isAutoStartup()");
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		stop();
		new Thread(() -> {
			try {
				Thread.sleep(300L);
				System.out.println("【SmartLifecycle接口】SmartLifecycle.stop(Runnable callback)");
			} catch (InterruptedException e) {
				// ignore
			} finally {
				callback.run();
			}
		}).start();
	}

	@Override
	public int getPhase() {
		System.out.println("【SmartLifecycle接口】SmartLifecycle.getPhase()");
		return 0;
	}

	@PostConstruct
	public void postConstruct() {
		System.out.println("【@PostConstruct】调用注解配置的初始化方法");
	}

	@PreDestroy
	public void preDestroy() {
		System.out.println("【@PreDestroy】调用注解配置的初始化方法");
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("【BeanFactoryAware接口】调用BeanFactoryAware.setBeanFactory()，获取到当前的beanFactory");
	}

	@Override
	public void setBeanName(String s) {
		System.out.println("【BeanNameAware接口】调用BeanNameAware.setBeanName()，当前beanName：" + s);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("【ApplicationContextAware接口】调用ApplicationContext.setApplicationContext()，可以获取当前容器的上下文实例。进而可以进行一些依赖于上下文对象的操作，也可以获取上下文中的某些属性");
	}
}
