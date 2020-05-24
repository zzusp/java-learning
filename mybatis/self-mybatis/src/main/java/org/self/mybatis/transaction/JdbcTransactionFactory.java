package org.self.mybatis.transaction;

import org.self.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 18:55 2020/5/22
 * @modified By
 */
public class JdbcTransactionFactory implements TransactionFactory {
	public JdbcTransactionFactory() {
	}

	@Override
	public Transaction newTransaction(DataSource ds, TransactionIsolationLevel level, boolean autoCommit) {
		return new JdbcTransaction(ds, level, autoCommit);
	}
}
