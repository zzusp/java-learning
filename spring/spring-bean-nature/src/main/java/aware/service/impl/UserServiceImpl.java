package aware.service.impl;

import aware.dao.UserDao;
import aware.service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Aaron.Sun
 * @description 用户服务接口实现类
 * @date Created in 11:48 2020/5/15
 * @modified By
 */
public class UserServiceImpl implements UserService, InitializingBean, DisposableBean, SmartLifecycle {

	private boolean running;
	private String beanName;
	private final UserDao userDao;

	/**
	 * 构造方法注入，此处@Autowired可加可不加
	 *
	 * @param userDao dao
	 */
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public String findByName(String name) {
		System.out.println(userDao);
		return userDao.findByName(name);
	}

	public void init() {
		System.out.println("自定义配置方法--初始化--init-method");
	}

	public void cleanup() {
		System.out.println("自定义配置方法--销毁--destroy-method");
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("回调接口定义的方法--销毁--destroy");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("回调接口定义的方法--初始化--afterPropertiesSet");
	}

	@PostConstruct
	public void postConstruct() {
		System.out.println("注释--初始化--@PostConstruct");
	}

	@PreDestroy
	public void preDestroy() {
		System.out.println("注释--销毁--@PreDestroy");
	}

	@Override
	public void setBeanName(String s) {
		System.out.println("感知接口--BeanNameAware--setBeanName 定义的beanName：" + s);
		this.beanName = s;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("感知接口--ApplicationContextAware--setApplicationContext 手动执行构造方法注入");
		AnnotationConfigApplicationContext context = (AnnotationConfigApplicationContext) applicationContext;
		UserService userService = new UserServiceImpl(context.getBean("userDao", UserDao.class));
		context.getBeanFactory().registerSingleton(this.beanName, userService);
	}

	@Override
	public void start() {
		this.running = true;
		System.out.println("UserServiceImpl--SmartLifecycle--start（调用顺序受getPhase()返回的值影响，优先调用返回值小的）");
	}

	@Override
	public void stop() {
		this.running = false;
		System.out.println("UserServiceImpl--SmartLifecycle--stop（调用顺序受getPhase()返回的值影响，优先调用返回值大的）");
	}

	@Override
	public boolean isRunning() {
		System.out.println("UserServiceImpl--SmartLifecycle--isRunning--false");
		return this.running;
	}

	@Override
	public boolean isAutoStartup() {
		System.out.println("UserServiceImpl--SmartLifecycle--isAutoStartup--true");
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		stop();
		new Thread(() -> {
			try {
				Thread.sleep(300L);
				System.out.println("UserServiceImpl--SmartLifecycle--stop--callback（调用顺序受getPhase()返回的值影响，优先调用返回值大的）");
			} catch (InterruptedException e) {
				// ignore
			} finally {
				callback.run();
			}
		}).start();
	}

	@Override
	public int getPhase() {
		System.out.println("UserServiceImpl--SmartLifecycle--getPhase--2");
		return 2;
	}
}
