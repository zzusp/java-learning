package javacode.service.impl;

import javacode.dao.UserDao;
import javacode.service.UserService;

/**
 * @author Aaron.Sun
 * @description 用户服务接口实现类
 * @date Created in 11:48 2020/5/15
 * @modified By
 */
public class UserServiceImpl implements UserService {

	private final UserDao userDao;

	/**
	 * 构造方法注入，此处@Autowired可加可不加
	 *
	 * @param userDao dao
	 */
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	public String findByName(String name) {
		System.out.println(name);
		return userDao.findByName(name);
	}

}
