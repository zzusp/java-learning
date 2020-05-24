package org.self.mybatis.transaction.managed;

import org.self.mybatis.session.TransactionIsolationLevel;
import org.self.mybatis.transaction.Transaction;
import org.self.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author Aaron.Sun
 * @description 管理的事务的工厂（当环境配置为null或环境配置中没有配置事务工厂对象时，使用该对象作为事务工厂对象）
 * @date Created in 21:25 2020/5/24
 * @modified By
 */
public class ManagedTransactionFactory implements TransactionFactory {
	private boolean closeConnection = true;

	public ManagedTransactionFactory() {
	}

	public void setProperties(Properties props) {
		if (props != null) {
			String closeConnectionProperty = props.getProperty("closeConnection");
			if (closeConnectionProperty != null) {
				this.closeConnection = Boolean.parseBoolean(closeConnectionProperty);
			}
		}

	}

	public Transaction newTransaction(Connection conn) {
		return new ManagedTransaction(conn, this.closeConnection);
	}

	@Override
	public Transaction newTransaction(DataSource ds, TransactionIsolationLevel level, boolean autoCommit) {
		return new ManagedTransaction(ds, level, this.closeConnection);
	}
}
