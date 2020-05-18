package annotation.service.impl;


import annotation.dao.UserDao;
import annotation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Aaron.Sun
 * @description 学生用户服务接口实现类
 * @date Created in 11:48 2020/5/15
 * @modified By
 */
@Service
public class StudentServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	public String findByName(String name) {
		System.out.println(name);
		return userDao.findByName(name);
	}

}
