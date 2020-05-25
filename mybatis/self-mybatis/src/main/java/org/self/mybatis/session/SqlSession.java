package org.self.mybatis.session;

import java.sql.Connection;

/**
 * @author Aaron.Sun
 * @description sqlSession（提供查询、获取mapper、获取数据库连接等操作）
 * @date Created in 14:49 2020/5/22
 * @modified By
 */
public interface SqlSession {

	<T> T selectOne(String var1, Object var2) throws Exception;

	Configuration getConfiguration();

	<T> T getMapper(Class<T> var1) throws Exception;

	Connection getConnection() throws Exception;

	void close();
}
