package lifecycle.config;

import lifecycle.dao.UserDao;
import lifecycle.service.impl.StudentServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 10:59 2020/5/16
 * @modified By
 */
@Configuration
@ComponentScan("lifecycle")
public class AppConfig {

	@Bean(initMethod = "init", destroyMethod = "cleanup")
	public StudentServiceImpl studentService(UserDao userDao) {
		StudentServiceImpl studentService = new StudentServiceImpl();
		studentService.setUserDao(userDao);
		return studentService;
	}

}
