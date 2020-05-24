package org.self.mybatis.session.defaults;

import org.self.mybatis.executor.Executor;
import org.self.mybatis.mapping.MappedStatement;
import org.self.mybatis.session.Configuration;
import org.self.mybatis.session.SqlSession;

import java.sql.Connection;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 默认的SqlSession实现
 * @date Created in 16:12 2020/5/22
 * @modified By
 */
public class DefaultSqlSession implements SqlSession {

	private final Configuration configuration;
	private final Executor executor;
	private final boolean autoCommit;

	public DefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
		this.configuration = configuration;
		this.executor = executor;
		this.autoCommit = autoCommit;
	}

	@Override
	public <T> T selectOne(String statement, Object parameter) throws Exception {
		List<T> list = this.selectList(statement, parameter);
		if (list.size() == 1) {
			return list.get(0);
		} else if (list.size() > 1) {
			throw new Exception("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
		} else {
			return null;
		}
	}

	@Override
	public Configuration getConfiguration() {
		return this.configuration;
	}

	@Override
	public <T> T getMapper(Class<T> type) throws Exception {
		return this.configuration.getMapper(type, this);
	}

	@Override
	public Connection getConnection() throws Exception {
//		try {
//			return this.executor.getTransaction().getConnection();
//		} catch (SQLException var2) {
//			throw new Exception("Error getting a new connection.  Cause: " + var2, var2);
//		}
		return null;
	}

	@Override
	public void close() {

	}

	public <E> List<E> selectList(String statement, Object parameter) throws Exception {
		// 此处rowBounds参数类型原为org.apache.ibatis.session.RowBounds类型
		return this.selectList(statement, parameter, null);
	}

	public <E> List<E> selectList(String statement, Object parameter, Object rowBounds) throws Exception {
		List<E> var5;
		try {
			MappedStatement ms = this.configuration.getMappedStatement(statement);
			var5 = this.executor.query(ms, parameter);
		} catch (Exception var9) {
			throw new Exception("Error querying database.  Cause: " + var9, var9);
		}
		return var5;
	}
}
