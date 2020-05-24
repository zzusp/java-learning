package org.self.mybatis.spring;

import org.self.mybatis.exceptions.PersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.transaction.TransactionException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * @author Aaron.Sun
 * @description 异常处理
 * @date Created in 16:27 2020/5/24
 * @modified By
 */
public class MyBatisExceptionTranslator implements PersistenceExceptionTranslator {
	private final Supplier<SQLExceptionTranslator> exceptionTranslatorSupplier;
	private SQLExceptionTranslator exceptionTranslator;

	public MyBatisExceptionTranslator(DataSource dataSource, boolean exceptionTranslatorLazyInit) {
		this(() -> {
			return new SQLErrorCodeSQLExceptionTranslator(dataSource);
		}, exceptionTranslatorLazyInit);
	}

	public MyBatisExceptionTranslator(Supplier<SQLExceptionTranslator> exceptionTranslatorSupplier, boolean exceptionTranslatorLazyInit) {
		this.exceptionTranslatorSupplier = exceptionTranslatorSupplier;
		if (!exceptionTranslatorLazyInit) {
			this.initExceptionTranslator();
		}

	}

	@Override
	public DataAccessException translateExceptionIfPossible(RuntimeException e) {
		if (e instanceof PersistenceException) {
			if (((RuntimeException) e).getCause() instanceof PersistenceException) {
				e = (PersistenceException) ((RuntimeException) e).getCause();
			}

			if (((RuntimeException) e).getCause() instanceof SQLException) {
				this.initExceptionTranslator();
				return this.exceptionTranslator.translate(((RuntimeException) e).getMessage() + "\n", (String) null, (SQLException) ((RuntimeException) e).getCause());
			} else if (((RuntimeException) e).getCause() instanceof TransactionException) {
				throw (TransactionException) ((RuntimeException) e).getCause();
			} else {
				return new MyBatisSystemException((Throwable) e);
			}
		} else {
			return null;
		}
	}

	private synchronized void initExceptionTranslator() {
		if (this.exceptionTranslator == null) {
			this.exceptionTranslator = (SQLExceptionTranslator) this.exceptionTranslatorSupplier.get();
		}

	}
}
