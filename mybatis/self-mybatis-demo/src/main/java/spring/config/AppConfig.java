package spring.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.self.mybatis.session.defaults.SqlSessionFactory;
import org.self.mybatis.spring.SqlSessionFactoryBean;
import org.self.mybatis.spring.annotation.SelfMapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import spring.mapper.UserMapper;

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
@SelfMapperScan("spring.mapper")
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
		org.self.mybatis.session.Configuration configuration = new org.self.mybatis.session.Configuration();
		configuration.addMapper(UserMapper.class);
		factoryBean.setConfiguration(configuration);
		return factoryBean.getObject();
	}

}
