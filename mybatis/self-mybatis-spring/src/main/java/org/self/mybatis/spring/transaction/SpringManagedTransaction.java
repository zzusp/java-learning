package org.self.mybatis.spring.transaction;

import org.self.mybatis.transaction.Transaction;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Aaron.Sun
 * @description spring管理的事务
 * @date Created in 16:39 2020/5/24
 * @modified By
 */
public class SpringManagedTransaction implements Transaction {
	//	private static final Logger LOGGER = LoggerFactory.getLogger(SpringManagedTransaction.class);
	private final DataSource dataSource;
	private Connection connection;
	private boolean isConnectionTransactional;
	private boolean autoCommit;

	public SpringManagedTransaction(DataSource dataSource) {
		Assert.notNull(dataSource, "No DataSource specified");
		this.dataSource = dataSource;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (this.connection == null) {
			this.openConnection();
		}

		return this.connection;
	}

	private void openConnection() throws SQLException {
		this.connection = DataSourceUtils.getConnection(this.dataSource);
		this.autoCommit = this.connection.getAutoCommit();
		this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.connection, this.dataSource);
//		LOGGER.debug(() -> {
//			return "JDBC Connection [" + this.connection + "] will" + (this.isConnectionTransactional ? " " : " not ") + "be managed by Spring";
//		});
	}

	@Override
	public void commit() throws SQLException {
		if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
//			LOGGER.debug(() -> {
//				return "Committing JDBC Connection [" + this.connection + "]";
//			});
			this.connection.commit();
		}

	}

	@Override
	public void rollback() throws SQLException {
		if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
//			LOGGER.debug(() -> {
//				return "Rolling back JDBC Connection [" + this.connection + "]";
//			});
			this.connection.rollback();
		}

	}

	@Override
	public void close() throws SQLException {
		DataSourceUtils.releaseConnection(this.connection, this.dataSource);
	}

	public Integer getTimeout() throws SQLException {
		ConnectionHolder holder = (ConnectionHolder) TransactionSynchronizationManager.getResource(this.dataSource);
		return holder != null && holder.hasTimeout() ? holder.getTimeToLiveInSeconds() : null;
	}
}

