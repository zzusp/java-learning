package annotation;

import annotation.config.AppConfig;
import annotation.service.UserService;
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
		// 通过type获取bean，
		// 重点：此处不能使用context.getBean(UserServiceImpl.class)获取bean！！！
		// 因为此处获取到的bean为JDK动态代理后的代理类，并不是UserServiceImpl.class类型的，而是一个动态生成的、继承Proxy类、且实现UserService接口的类
		// UserService userService2 = context.getBean(UserServiceImpl.class);
		UserService userService2 = (UserService) context.getBean("userServiceImpl", UserService.class);
		System.out.println(userService1.findByName("张三"));
		System.out.println(userService2.findByName("李四"));
		UserService studentService1 = context.getBean("studentServiceImpl", UserService.class);
		UserService studentService2 = context.getBean("studentServiceImpl", UserService.class);
		System.out.println(studentService1.findByName("王五"));
		System.out.println(studentService2.findByName("赵六"));
	}
}
