package annotation;

import annotation.config.AppConfig;
import annotation.dao.UserDao;
import annotation.extension.CustomSqlSessionFactory;
import annotation.extension.CustomSqlSessionFactoryBean;
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
		UserDao userDao = context.getBean("userDaoImpl", UserDao.class);
		System.out.println(userDao.findByName("张三"));

		// 获取sqlSessionFactory实例
		CustomSqlSessionFactory sqlSessionFactory = context.getBean("sqlSessionFactory", CustomSqlSessionFactory.class);
		System.out.println(sqlSessionFactory.toString());
		// 获取sqlSessionFactoryBean实例
		CustomSqlSessionFactoryBean sqlSessionFactoryBean = context.getBean("&sqlSessionFactory", CustomSqlSessionFactoryBean.class);
		System.out.println(sqlSessionFactoryBean.toString());
		context.getBeanFactory().destroySingletons();
	}
}
