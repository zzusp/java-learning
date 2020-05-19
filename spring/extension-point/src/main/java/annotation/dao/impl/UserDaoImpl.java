package annotation.dao.impl;

import annotation.dao.UserDao;
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
	public void init() {
		System.out.println("生命周期回调-->@PostConstruct bean: " + this);
	}

	@PreDestroy
	public void destory() {
		System.out.println("生命周期回调-->@PreDestroy bean: " + this);
	}
}
