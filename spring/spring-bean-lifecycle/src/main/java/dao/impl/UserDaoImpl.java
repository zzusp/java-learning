package dao.impl;

import dao.UserDao;
import org.springframework.stereotype.Repository;

/**
 * @author Aaron.Sun
 * @description 用户信息数据库访问接口实现类
 * @date Created in 11:49 2020/5/15
 * @modified By
 */
@Repository
public class UserDaoImpl implements UserDao {
	@Override
	public String findByName(String name) {
		return "find user " + name;
	}

}
