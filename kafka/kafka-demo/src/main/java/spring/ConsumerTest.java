package spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.config.ConsumerCofig;

/**
 * @author Aaron.Sun
 * @description 消费者测试
 * @date Created in 22:14 2020/5/26
 * @modified By
 */
public class ConsumerTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerCofig.class);
	}

}
