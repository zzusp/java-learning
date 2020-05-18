package xml.service.impl;

import xml.dao.UserDao;
import xml.service.UserService;

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
		System.out.println(name);
		return userDao.findByName(name);
	}

}
