package spring.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Aaron.Sun
 * @description spring配置
 * @date Created in 11:11 2020/5/22
 * @modified By
 */
@Configuration
@ComponentScan("spring")
@MapperScan("spring.mapper")
public class AppConfig {

	@Bean
	public DataSource dataSource() throws Exception {
		Properties properties = new Properties();
		properties.setProperty("driverClassName", "com.mysql.jdbc.Driver");
		properties.setProperty("url", "jdbc:mysql://122.152.221.117:3306/mybatis");
		properties.setProperty("username", "mybatis");
		properties.setProperty("password", "12345678");
		return DruidDataSourceFactory.createDataSource(properties);
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		return factoryBean.getObject();
	}

}
