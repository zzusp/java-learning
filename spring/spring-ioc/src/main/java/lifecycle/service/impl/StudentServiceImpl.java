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

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public String findByName(String name) {
		System.out.println(userDao);
		return userDao.findByName(name);
	}

	public void init() {
		System.out.println("自定义配置方法--初始化--init");
	}

	public void cleanup() {
		System.out.println("自定义配置方法--销毁--cleanup");
	}
}
