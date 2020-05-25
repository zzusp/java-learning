package spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.config.AppConfig;
import spring.mapper.UserMapper;

/**
 * @author Aaron.Sun
 * @description self-mybatis-spring测试类
 * @date Created in 9:14 2020/5/22
 * @modified By
 */
public class MybatisSpringTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserMapper userMapper = context.getBean("userMapper", UserMapper.class);
		System.out.println(userMapper.queryUserName("1"));
	}

}
