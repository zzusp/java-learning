package mybatis;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import mybatis.mapper.UserMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

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
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
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
