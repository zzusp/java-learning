package aware.config;

import aware.dao.UserDao;
import aware.dao.impl.UserDaoImpl;
import aware.service.impl.StudentServiceImpl;
import aware.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 10:59 2020/5/16
 * @modified By
 */
@Configuration
@ComponentScan("aware")
public class AppConfig {

	@Order(1)
	@Bean
	public UserDao userDao() {
		return new UserDaoImpl();
	}

	@Order(2)
	@Bean(initMethod = "init", destroyMethod = "cleanup")
	public UserServiceImpl userService() {
		return new UserServiceImpl(null);
	}

	@Order(2)
	@Bean
	public StudentServiceImpl studentService() {
		return new StudentServiceImpl();
	}

}
