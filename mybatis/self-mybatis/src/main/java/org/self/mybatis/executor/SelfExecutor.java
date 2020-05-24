package org.self.mybatis.executor;

import org.self.mybatis.executor.statement.StatementHandler;
import org.self.mybatis.mapping.BoundSql;
import org.self.mybatis.mapping.MappedStatement;
import org.self.mybatis.session.Configuration;
import org.self.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 16:26 2020/5/22
 * @modified By
 */
public class SelfExecutor implements Executor {

	protected Transaction transaction;
	protected Configuration configuration;

	public SelfExecutor(Configuration configuration, Transaction transaction) {
		this.transaction = transaction;
		this.configuration = configuration;
	}

	@Override
	public <E> List<E> query(MappedStatement ms, Object parameter) throws SQLException {
		BoundSql boundSql = ms.getBoundSql(parameter);
		return this.queryFromDatabase(ms, parameter, boundSql);
	}

	private <E> List<E> queryFromDatabase(MappedStatement ms, Object parameter, BoundSql boundSql) throws SQLException {
		return this.doQuery(ms, parameter, boundSql);
	}

	public <E> List<E> doQuery(MappedStatement ms, Object parameter, BoundSql boundSql) throws SQLException {
		Statement stmt = null;

		List<E> var9 = null;
		try {
			Configuration configuration = ms.getConfiguration();
			StatementHandler handler = configuration.newStatementHandler(ms, parameter, boundSql);
			stmt = this.prepareStatement(handler);
			var9 = handler.query(stmt);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(stmt);
		}

		return var9;
	}

	/**
	 * 该方法原在BaseExecutor抽象类中
	 *
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		// 原方法需要根据日志等级，判断是否需要创建一个有日志的连接
		return this.transaction.getConnection();
	}

	private Statement prepareStatement(StatementHandler handler) throws Exception {
		Connection connection = this.getConnection();
		// 原方法会根据this.transaction.getTimeout()的值，来设置事务超时时间
		Statement stmt = handler.prepare(connection);
		handler.parameterize(stmt);
		return stmt;
	}

	protected void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException var3) {
			}
		}
	}
}
