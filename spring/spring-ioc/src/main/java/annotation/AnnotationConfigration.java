package annotation;

import annotation.config.AppConfig;
import annotation.dao.UserDao;
import annotation.service.UserService;
import annotation.service.impl.StudentServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Aaron.Sun
 * @description 测试类
 * @date Created in 11:43 2020/5/15
 * @modified By
 */
public class AnnotationConfigration {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		// 通过name获取bean
		UserService userService1 = context.getBean("userServiceImpl", UserService.class);
		// 通过type获取bean
		UserService userService2 = context.getBean(UserService.class);
		System.out.println(userService1.findByName("张三"));
		System.out.println(userService2.findByName("李四"));
		StudentServiceImpl studentServiceImpl = new StudentServiceImpl();
		studentServiceImpl.setUserDao(context.getBean(UserDao.class));
		context.getBeanFactory().registerSingleton("studentService", studentServiceImpl);
		UserService studentService1 = context.getBean("studentService", UserService.class);
		UserService studentService2 = context.getBean("studentService", UserService.class);
		System.out.println(studentService1.findByName("王五"));
		System.out.println(studentService2.findByName("赵六"));
	}
}
