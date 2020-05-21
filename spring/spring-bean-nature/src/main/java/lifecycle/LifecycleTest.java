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
		// AnnotationConfigApplicationContext不支持显示调用refresh()方法
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		// ClassPathXmlApplicationContext不支持@PostConstruct、@PreDestroy注解
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		// 为上下文添加一个关闭钩子，当Main方法执行结束前，正常关闭容器，并调用所有singleton bean的所有destroy方法
		context.registerShutdownHook();
		UserService studentService = context.getBean(StudentServiceImpl.class);
		UserService userService = context.getBean(UserServiceImpl.class);
		System.out.println("=======================================");
		System.out.println(userService.findByName("张三"));
		System.out.println(studentService.findByName("李四"));
		System.out.println("=======================================");
		System.out.println("显示调用容器close()方法，SmartLifecycle实现类中的stop()、stop(Runnable run)方法会被触发");
		System.out.println("触发顺序受getPhase()返回的值影响，优先调用返回值大的");
		context.close();
	}

}
