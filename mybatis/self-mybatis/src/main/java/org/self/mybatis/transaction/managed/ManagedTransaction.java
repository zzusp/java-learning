package org.self.mybatis.transaction.managed;

import org.self.mybatis.session.TransactionIsolationLevel;
import org.self.mybatis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Aaron.Sun
 * @description 主事务（当环境配置为null或环境配置中没有配置事务工厂对象时，使用该对象作为事务对象）
 * @date Created in 21:27 2020/5/24
 * @modified By
 */
public class ManagedTransaction implements Transaction {
	//	private static final Log log = LogFactory.getLog(ManagedTransaction.class);
	private DataSource dataSource;
	private TransactionIsolationLevel level;
	private Connection connection;
	private final boolean closeConnection;

	public ManagedTransaction(Connection connection, boolean closeConnection) {
		this.connection = connection;
		this.closeConnection = closeConnection;
	}

	public ManagedTransaction(DataSource ds, TransactionIsolationLevel level, boolean closeConnection) {
		this.dataSource = ds;
		this.level = level;
		this.closeConnection = closeConnection;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (this.connection == null) {
			this.openConnection();
		}
		return this.connection;
	}

	@Override
	public void commit() throws SQLException {
	}

	@Override
	public void rollback() throws SQLException {
	}

	@Override
	public void close() throws SQLException {
		if (this.closeConnection && this.connection != null) {
//			if (log.isDebugEnabled()) {
//				log.debug("Closing JDBC Connection [" + this.connection + "]");
//			}
			this.connection.close();
		}
	}

	protected void openConnection() throws SQLException {
//		if (log.isDebugEnabled()) {
//			log.debug("Opening JDBC Connection");
//		}
		this.connection = this.dataSource.getConnection();
		if (this.level != null) {
			this.connection.setTransactionIsolation(this.level.getLevel());
		}
	}

	public Integer getTimeout() throws SQLException {
		return null;
	}
}
