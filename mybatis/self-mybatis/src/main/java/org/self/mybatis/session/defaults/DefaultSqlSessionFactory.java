package org.self.mybatis.session.defaults;

import org.self.mybatis.executor.Executor;
import org.self.mybatis.mapping.Environment;
import org.self.mybatis.session.Configuration;
import org.self.mybatis.session.ExecutorType;
import org.self.mybatis.session.SqlSession;
import org.self.mybatis.session.TransactionIsolationLevel;
import org.self.mybatis.transaction.Transaction;
import org.self.mybatis.transaction.TransactionFactory;
import org.self.mybatis.transaction.managed.ManagedTransactionFactory;

import java.sql.SQLException;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 19:01 2020/5/22
 * @modified By
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
	private final Configuration configuration;

	public DefaultSqlSessionFactory(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public SqlSession openSession() throws Exception {
		return this.openSessionFromDataSource(this.configuration.getDefaultExecutorType(), (TransactionIsolationLevel) null, false);
	}

	@Override
	public Configuration getConfiguration() {
		return this.configuration;
	}

	private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) throws Exception {
		Transaction tx = null;

		DefaultSqlSession var8;
		try {
			Environment environment = this.configuration.getEnvironment();
			TransactionFactory transactionFactory = this.getTransactionFactoryFromEnvironment(environment);
			tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
			Executor executor = this.configuration.newExecutor(tx, execType);
			var8 = new DefaultSqlSession(this.configuration, executor, autoCommit);
		} catch (Exception var12) {
			this.closeTransaction(tx);
			throw new Exception("Error opening session.  Cause: " + var12, var12);
		} finally {
//			ErrorContext.instance().reset();
		}

		return var8;
	}

	/**
	 * 从环境配置中获取事务工厂对象
	 * 如果环境配置为null或环境配置中没有配置事务工厂，则返回ManagedTransactionFactory对象作为事务工厂
	 *
	 * @param environment 环境配置
	 * @return 事务工厂
	 */
	private TransactionFactory getTransactionFactoryFromEnvironment(Environment environment) {
		return (TransactionFactory) (environment != null && environment.getTransactionFactory() != null ? environment.getTransactionFactory() : new ManagedTransactionFactory());
	}

	private void closeTransaction(Transaction tx) {
		if (tx != null) {
			try {
				tx.close();
			} catch (SQLException var3) {
				System.out.println("Error closing transaction. Cause: " + var3);
			}
		}

	}
}
