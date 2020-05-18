package javacode;

import javacode.config.AppConfig;
import javacode.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Aaron.Sun
 * @description 测试类
 * @date Created in 11:43 2020/5/15
 * @modified By
 */
public class JavaConfigration {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		// 通过name获取bean
		UserService userService1 = context.getBean("userService", UserService.class);
		// 通过type获取bean
		UserService userService2 = context.getBean("userService", UserService.class);
		// 通过name获取bean
		UserService studentService1 = context.getBean("studentService", UserService.class);
		// 通过type获取bean
		UserService studentService2 = context.getBean("studentService", UserService.class);
		System.out.println(userService1.findByName("张三"));
		System.out.println(userService2.findByName("李四"));
		System.out.println(studentService1.findByName("王五"));
		System.out.println(studentService2.findByName("赵六"));
	}
}
