package mybatis;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import mybatis.mapper.UserMapper;
import org.self.mybatis.mapping.Environment;
import org.self.mybatis.session.Configuration;
import org.self.mybatis.session.SqlSession;
import org.self.mybatis.session.defaults.DefaultSqlSessionFactory;
import org.self.mybatis.session.defaults.SqlSessionFactory;
import org.self.mybatis.transaction.JdbcTransactionFactory;
import org.self.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Aaron.Sun
 * @description mybatis测试类
 * @date Created in 9:13 2020/5/22
 * @modified By
 */
public class MybatisTest {

	public static void main(String[] args) throws Exception {
		Properties properties = new Properties();
		properties.setProperty("driverClassName", "com.mysql.jdbc.Driver");
		properties.setProperty("url", "jdbc:mysql://122.152.221.117:3306/mybatis");
		properties.setProperty("username", "mybatis");
		properties.setProperty("password", "12345678");
		DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
		TransactionFactory transactionFactory =
				new JdbcTransactionFactory();
		Environment environment =
				new Environment("development", transactionFactory, dataSource);
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(UserMapper.class);
		SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			UserMapper userMapper = session.getMapper(UserMapper.class);
			System.out.println(userMapper.queryUserName("1"));
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
