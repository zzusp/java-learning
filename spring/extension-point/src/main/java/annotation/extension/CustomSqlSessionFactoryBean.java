package annotation.extension;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 15:25 2020/5/19
 * @modified By
 */
public class CustomSqlSessionFactoryBean implements FactoryBean<CustomSqlSessionFactory> {

	private CustomSqlSessionFactory sqlSessionFactory;
	private String datasource;

	@Override
	public CustomSqlSessionFactory getObject() throws Exception {
		if (this.sqlSessionFactory == null) {
			// 在这里做初始化处理（极简化版）
			// 1.判断datasource非空
			Assert.notNull(this.datasource, "Property 'dataSource' is required");
			// 2. 创建xml解析对象，解析xml配置文件
			// 3. 实例化CustomSqlSessionFactory对象（mybatis中使用sqlSessionFactoryBuilder创建，这里简化）
			this.sqlSessionFactory = new CustomSqlSessionFactory();
			this.sqlSessionFactory.setDatasource(datasource);
		}
		return this.sqlSessionFactory;
	}

	/**
	 * 如果是单例模式，该方法可以不用重写
	 *
	 * @return 是否为单例模式
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public Class<?> getObjectType() {
		return this.sqlSessionFactory == null ? CustomSqlSessionFactory.class : this.sqlSessionFactory.getClass();
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
}
