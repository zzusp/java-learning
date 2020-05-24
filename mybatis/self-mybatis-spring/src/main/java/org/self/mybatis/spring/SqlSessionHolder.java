package org.self.mybatis.spring;

import org.self.mybatis.session.ExecutorType;
import org.self.mybatis.session.SqlSession;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.util.Assert;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 16:36 2020/5/24
 * @modified By
 */
public final class SqlSessionHolder extends ResourceHolderSupport {
	private final SqlSession sqlSession;
	private final ExecutorType executorType;
	private final PersistenceExceptionTranslator exceptionTranslator;

	public SqlSessionHolder(SqlSession sqlSession, ExecutorType executorType, PersistenceExceptionTranslator exceptionTranslator) {
		Assert.notNull(sqlSession, "SqlSession must not be null");
		Assert.notNull(executorType, "ExecutorType must not be null");
		this.sqlSession = sqlSession;
		this.executorType = executorType;
		this.exceptionTranslator = exceptionTranslator;
	}

	public SqlSession getSqlSession() {
		return this.sqlSession;
	}

	public ExecutorType getExecutorType() {
		return this.executorType;
	}

	public PersistenceExceptionTranslator getPersistenceExceptionTranslator() {
		return this.exceptionTranslator;
	}
}
