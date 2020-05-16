package lifecycle.dao.impl;

import lifecycle.dao.UserDao;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Aaron.Sun
 * @description 用户信息数据库访问接口实现类
 * @date Created in 11:49 2020/5/15
 * @modified By
 */
@Repository
public class UserDaoImpl implements UserDao {
	public String findByName(String name) {
		return "find user " + name;
	}

	@PostConstruct
	public void postConstruct() {
		System.out.println("注释--初始化--@PostConstruct");
	}

	@PreDestroy
	public void preDestroy() {
		System.out.println("注释--销毁--@PreDestroy");
	}
}
