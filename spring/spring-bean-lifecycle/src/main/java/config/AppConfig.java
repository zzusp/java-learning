package config;

import dao.UserDao;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.*;
import service.UserService;
import service.impl.UserServiceImpl;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 10:59 2020/5/16
 * @modified By
 */
@Configuration()
//@ImportResource("classpath:spring.xml")
@ComponentScan({"processor", "dao"})
public class AppConfig {

	@Bean(initMethod = "init", destroyMethod = "cleanup")
	public UserServiceImpl userService(UserDao userDao) {
		UserServiceImpl userService = new UserServiceImpl();
		userService.setUserDao(userDao);
		return userService;
	}

}
