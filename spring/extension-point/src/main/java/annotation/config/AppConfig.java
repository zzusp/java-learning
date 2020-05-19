package annotation.config;

import annotation.extension.CustomSqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Aaron.Sun
 * @description spring扫描配置
 * @date Created in 11:48 2020/5/15
 * @modified By
 */
@Configuration
@ComponentScan("annotation")
@ImportResource("classpath:spring.xml")
public class AppConfig {

	@Bean
	public String datasource(DatasourceConfig datasourceConfig) {
		return "模拟数据源创建" + datasourceConfig.createDatasource();
	}

	@Bean
	public CustomSqlSessionFactoryBean sqlSessionFactory(String datasource) throws Exception {
		CustomSqlSessionFactoryBean sqlSessionFactoryBean = new CustomSqlSessionFactoryBean();
		sqlSessionFactoryBean.setDatasource(datasource);
		return sqlSessionFactoryBean;
	}

}
