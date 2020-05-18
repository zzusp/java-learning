package xml;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import xml.service.UserService;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 16:32 2020/5/15
 * @modified By
 */
public class XmlConfigration {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		// 通过name获取bean
		UserService userService1 = context.getBean("userService", UserService.class);
		UserService userService2 = context.getBean("studentService", UserService.class);
		System.out.println(userService1.findByName("张三"));
		System.out.println(userService2.findByName("李四"));
	}

}
