package lifecycle;

import lifecycle.config.AppConfig;
import lifecycle.service.UserService;
import lifecycle.service.impl.StudentServiceImpl;
import lifecycle.service.impl.UserServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Aaron.Sun
 * @description bean生命周期测试
 * @date Created in 11:00 2020/5/16
 * @modified By
 */
public class LifecycleTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		// 通过name获取bean
		UserService userService = context.getBean(UserServiceImpl.class);
		System.out.println(userService.findByName("张三"));
		UserService studentService = context.getBean(StudentServiceImpl.class);
		System.out.println(studentService.findByName("李四"));
		context.getBeanFactory().destroySingletons();
	}

}
