package org.self.mybatis.spring.transaction;

import org.self.mybatis.session.TransactionIsolationLevel;
import org.self.mybatis.transaction.Transaction;
import org.self.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author Aaron.Sun
 * @description spring管理的事务工厂
 * @date Created in 16:40 2020/5/24
 * @modified By
 */
public class SpringManagedTransactionFactory implements TransactionFactory {
	public SpringManagedTransactionFactory() {
	}

	@Override
	public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
		return new SpringManagedTransaction(dataSource);
	}

	public Transaction newTransaction(Connection conn) {
		throw new UnsupportedOperationException("New Spring transactions require a DataSource");
	}

	public void setProperties(Properties props) {
	}
}
