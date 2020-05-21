package lifecycle.service.impl;


import lifecycle.dao.UserDao;
import lifecycle.service.UserService;

/**
 * @author Aaron.Sun
 * @description 学生用户服务接口实现类
 * @date Created in 11:48 2020/5/15
 * @modified By
 */
public class StudentServiceImpl implements UserService {

	public boolean running = false;
	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public String findByName(String name) {
		System.out.println(userDao);
		return userDao.findByName(name);
	}

	@Override
	public void start() {
		this.running = true;
		System.out.println("StudentServiceImpl--SmartLifecycle--start（调用顺序受getPhase()返回的值影响，优先调用返回值小的）");
	}

	@Override
	public void stop() {
		this.running = false;
		System.out.println("StudentServiceImpl--SmartLifecycle--stop（调用顺序受getPhase()返回的值影响，优先调用返回值大的）");
	}

	@Override
	public boolean isRunning() {
		System.out.println("StudentServiceImpl--SmartLifecycle--isRunning--false");
		return this.running;
	}

	@Override
	public boolean isAutoStartup() {
		System.out.println("StudentServiceImpl--SmartLifecycle--isAutoStartup--true");
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		stop();
		new Thread(() -> {
			try {
				Thread.sleep(300L);
				System.out.println("StudentServiceImpl--SmartLifecycle--stop--callback（调用顺序受getPhase()返回的值影响，优先调用返回值大的）");
			} catch (InterruptedException e) {
				// ignore
			} finally {
				callback.run();
			}
		}).start();
	}

	@Override
	public int getPhase() {
		System.out.println("StudentServiceImpl--SmartLifecycle--getPhase--1");
		return 1;
	}
}
