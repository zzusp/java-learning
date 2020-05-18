package javacode.config;

import javacode.aop.UserAspect;
import javacode.dao.UserDao;
import javacode.dao.impl.UserDaoImpl;
import javacode.service.UserService;
import javacode.service.impl.StudentServiceImpl;
import javacode.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Aaron.Sun
 * @description spring扫描配置
 * @date Created in 11:48 2020/5/15
 * @modified By
 */
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
	/**
	 * 写法1：
	 * <pre>{@code
	 *     @Bean
	 *     public UserService userService() {
	 * 	       return new UserServiceImpl(userDao());
	 *     }
	 * }</pre>
	 * <p>
	 * 写法2：
	 * <pre>{@code
	 *     @Bean
	 *     public UserService userService(@Qualifier("userDao") UserDao userDao) {
	 * 	       return new UserServiceImpl(userDao);
	 *     }
	 * }</pre>
	 *
	 * @param userDao userDao
	 */
	@Bean
	public UserService userService(UserDao userDao) {
		return new UserServiceImpl(userDao);
	}

	@Bean
	public UserService studentService(UserDao userDao) {
		StudentServiceImpl studentService = new StudentServiceImpl();
		studentService.setUserDao(userDao);
		return studentService;
	}

	@Bean
	public UserDao userDao() {
		return new UserDaoImpl();
	}

	@Bean
	public UserAspect userAspect() {
		return new UserAspect();
	}

}
