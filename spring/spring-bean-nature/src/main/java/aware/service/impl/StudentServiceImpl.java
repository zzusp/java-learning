package aware.service.impl;


import aware.dao.UserDao;
import aware.service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * @author Aaron.Sun
 * @description 学生用户服务接口实现类
 * @date Created in 11:48 2020/5/15
 * @modified By
 */
public class StudentServiceImpl implements UserService {

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
	public void setBeanName(String s) {
		System.out.println("感知接口--BeanNameAware--setBeanName 定义的beanName：" + s);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("感知接口--ApplicationContextAware--setApplicationContext 手动执行setter方法注入");
		this.setUserDao(applicationContext.getBean("userDao", UserDao.class));
	}
}
