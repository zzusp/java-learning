package lifecycle.service.impl;

import lifecycle.dao.UserDao;
import lifecycle.service.UserService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * @author Aaron.Sun
 * @description 用户服务接口实现类
 * @date Created in 11:48 2020/5/15
 * @modified By
 */
@Service
public class UserServiceImpl implements UserService, InitializingBean, DisposableBean {

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
		System.out.println(userDao);
		return userDao.findByName(name);
	}

	public void destroy() throws Exception {
		System.out.println("回调接口定义的方法--销毁--destroy");
	}

	public void afterPropertiesSet() throws Exception {
		System.out.println("回调接口定义的方法--初始化--afterPropertiesSet");
	}
}
