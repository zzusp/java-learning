package org.self.mybatis.transaction;

import org.self.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Aaron.Sun
 * @description JDBC的事务
 * @date Created in 18:51 2020/5/22
 * @modified By
 */
public class JdbcTransaction implements Transaction {

	protected Connection connection;
	protected DataSource dataSource;
	protected TransactionIsolationLevel level;
	protected boolean autoCommit;

	public JdbcTransaction(DataSource ds, TransactionIsolationLevel desiredLevel, boolean desiredAutoCommit) {
		this.dataSource = ds;
		this.level = desiredLevel;
		this.autoCommit = desiredAutoCommit;
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
		if (this.connection != null && !this.connection.getAutoCommit()) {
//			if (log.isDebugEnabled()) {
//				log.debug("Committing JDBC Connection [" + this.connection + "]");
//			}
			this.connection.commit();
		}
	}

	@Override
	public void rollback() throws SQLException {
		if (this.connection != null && !this.connection.getAutoCommit()) {
//			if (log.isDebugEnabled()) {
//				log.debug("Rolling back JDBC Connection [" + this.connection + "]");
//			}
			this.connection.rollback();
		}
	}

	@Override
	public void close() throws SQLException {
		if (this.connection != null) {
			this.resetAutoCommit();
//			if (log.isDebugEnabled()) {
//				log.debug("Closing JDBC Connection [" + this.connection + "]");
//			}
			this.connection.close();
		}
	}

	protected void setDesiredAutoCommit(boolean desiredAutoCommit) {
		try {
			if (this.connection.getAutoCommit() != desiredAutoCommit) {
//				if (log.isDebugEnabled()) {
//					log.debug("Setting autocommit to " + desiredAutoCommit + " on JDBC Connection [" + this.connection + "]");
//				}
				this.connection.setAutoCommit(desiredAutoCommit);
			}
		} catch (SQLException var3) {
			throw new TransactionException("Error configuring AutoCommit.  Your driver may not support getAutoCommit() or setAutoCommit(). Requested setting: " + desiredAutoCommit + ".  Cause: " + var3, var3);
		}
	}

	protected void resetAutoCommit() {
		try {
			if (!this.connection.getAutoCommit()) {
				this.connection.setAutoCommit(true);
			}
		} catch (SQLException var2) {
			System.out.println("Error resetting autocommit to true before closing the connection.  Cause: " + var2);
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
		this.setDesiredAutoCommit(this.autoCommit);
	}
}
