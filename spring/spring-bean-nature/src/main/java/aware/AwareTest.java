package aware;

import aware.config.AppConfig;
import aware.service.UserService;
import aware.service.impl.StudentServiceImpl;
import aware.service.impl.UserServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Aaron.Sun
 * @description bean生命周期测试
 * @date Created in 11:00 2020/5/16
 * @modified By
 */
public class AwareTest {

	public static void main(String[] args) {
		// AnnotationConfigApplicationContext不支持显示调用refresh()方法
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		// 为上下文添加一个关闭钩子，当Main方法执行结束前，正常关闭容器，并调用所有singleton bean的所有destroy方法
		context.registerShutdownHook();
		UserService studentService = context.getBean(StudentServiceImpl.class);
		UserService userService = context.getBean(UserServiceImpl.class);
		System.out.println("=======================================");
		System.out.println(userService.findByName("张三"));
		System.out.println(studentService.findByName("李四"));
//		context.getBeanFactory().destroySingletons();
	}

}
