package org.self.mybatis.spring;

import org.self.mybatis.exceptions.PersistenceException;
import org.self.mybatis.session.Configuration;
import org.self.mybatis.session.ExecutorType;
import org.self.mybatis.session.SqlSession;
import org.self.mybatis.session.defaults.SqlSessionFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 16:04 2020/5/24
 * @modified By
 */
public class SqlSessionTemplate implements SqlSession, DisposableBean {
	private final SqlSessionFactory sqlSessionFactory;
	private final ExecutorType executorType;
	private final SqlSession sqlSessionProxy;
	private final PersistenceExceptionTranslator exceptionTranslator;

	public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		this(sqlSessionFactory, sqlSessionFactory.getConfiguration().getDefaultExecutorType());
	}

	public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
		this(sqlSessionFactory, executorType, new MyBatisExceptionTranslator(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(), true));
	}

	public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType, PersistenceExceptionTranslator exceptionTranslator) {
		Assert.notNull(sqlSessionFactory, "Property 'sqlSessionFactory' is required");
		Assert.notNull(executorType, "Property 'executorType' is required");
		this.sqlSessionFactory = sqlSessionFactory;
		this.executorType = executorType;
		this.exceptionTranslator = exceptionTranslator;
		this.sqlSessionProxy = (SqlSession) Proxy.newProxyInstance(SqlSessionFactory.class.getClassLoader(), new Class[]{SqlSession.class}, new SqlSessionTemplate.SqlSessionInterceptor());
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return this.sqlSessionFactory;
	}

	public ExecutorType getExecutorType() {
		return this.executorType;
	}

	public PersistenceExceptionTranslator getPersistenceExceptionTranslator() {
		return this.exceptionTranslator;
	}

//	public Object selectOne(String statement) {
//		return this.sqlSessionProxy.selectOne(statement);
//	}

	@Override
	public <T> T selectOne(String statement, Object parameter) throws Exception {
		return this.sqlSessionProxy.selectOne(statement, parameter);
	}

//	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
//		return this.sqlSessionProxy.selectMap(statement, mapKey);
//	}

//	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
//		return this.sqlSessionProxy.selectMap(statement, parameter, mapKey);
//	}

//	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
//		return this.sqlSessionProxy.selectMap(statement, parameter, mapKey, rowBounds);
//	}

//	public <T> Cursor<T> selectCursor(String statement) {
//		return this.sqlSessionProxy.selectCursor(statement);
//	}

//	public <T> Cursor<T> selectCursor(String statement, Object parameter) {
//		return this.sqlSessionProxy.selectCursor(statement, parameter);
//	}

//	public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
//		return this.sqlSessionProxy.selectCursor(statement, parameter, rowBounds);
//	}

//	public <E> List<E> selectList(String statement) {
//		return this.sqlSessionProxy.selectList(statement);
//	}

//	public <E> List<E> selectList(String statement, Object parameter) {
//		return this.sqlSessionProxy.selectList(statement, parameter);
//	}

//	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
//		return this.sqlSessionProxy.selectList(statement, parameter, rowBounds);
//	}

//	public void select(String statement, ResultHandler handler) {
//		this.sqlSessionProxy.select(statement, handler);
//	}

//	public void select(String statement, Object parameter, ResultHandler handler) {
//		this.sqlSessionProxy.select(statement, parameter, handler);
//	}

//	public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
//		this.sqlSessionProxy.select(statement, parameter, rowBounds, handler);
//	}

//	public int insert(String statement) {
//		return this.sqlSessionProxy.insert(statement);
//	}

//	public int insert(String statement, Object parameter) {
//		return this.sqlSessionProxy.insert(statement, parameter);
//	}

//	public int update(String statement) {
//		return this.sqlSessionProxy.update(statement);
//	}

//	public int update(String statement, Object parameter) {
//		return this.sqlSessionProxy.update(statement, parameter);
//	}

//	public int delete(String statement) {
//		return this.sqlSessionProxy.delete(statement);
//	}

//	public int delete(String statement, Object parameter) {
//		return this.sqlSessionProxy.delete(statement, parameter);
//	}

	@Override
	public <T> T getMapper(Class<T> type) throws Exception {
		return this.getConfiguration().getMapper(type, this);
	}

	public void commit() {
		throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
	}

	public void commit(boolean force) {
		throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
	}

	public void rollback() {
		throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
	}

	public void rollback(boolean force) {
		throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
	}

	@Override
	public void close() {
		throw new UnsupportedOperationException("Manual close is not allowed over a Spring managed SqlSession");
	}

//	public void clearCache() {
//		this.sqlSessionProxy.clearCache();
//	}

	@Override
	public Configuration getConfiguration() {
		return this.sqlSessionFactory.getConfiguration();
	}

	@Override
	public Connection getConnection() throws Exception {
		return this.sqlSessionProxy.getConnection();
	}

//	public List<BatchResult> flushStatements() {
//		return this.sqlSessionProxy.flushStatements();
//	}

	@Override
	public void destroy() throws Exception {
	}

	private class SqlSessionInterceptor implements InvocationHandler {
		private SqlSessionInterceptor() {
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			SqlSession sqlSession = SqlSessionUtils.getSqlSession(SqlSessionTemplate.this.sqlSessionFactory, ExecutorType.SIMPLE, SqlSessionTemplate.this.exceptionTranslator);

			Object unwrapped;
			try {
				Object result = method.invoke(sqlSession, args);
//				if (!SqlSessionUtils.isSqlSessionTransactional(sqlSession, SqlSessionTemplate.this.sqlSessionFactory)) {
//					sqlSession.commit(true);
//				}

				unwrapped = result;
			} catch (Throwable var11) {
//				unwrapped = ExceptionUtil.unwrapThrowable(var11);
				unwrapped = var11;
				if (SqlSessionTemplate.this.exceptionTranslator != null && unwrapped instanceof PersistenceException) {
					SqlSessionUtils.closeSqlSession(sqlSession, SqlSessionTemplate.this.sqlSessionFactory);
					sqlSession = null;
					Throwable translated = SqlSessionTemplate.this.exceptionTranslator.translateExceptionIfPossible((PersistenceException) unwrapped);
					if (translated != null) {
						unwrapped = translated;
					}
				}

				throw (Throwable) unwrapped;
			} finally {
				if (sqlSession != null) {
					SqlSessionUtils.closeSqlSession(sqlSession, SqlSessionTemplate.this.sqlSessionFactory);
				}

			}

			return unwrapped;
		}
	}
}
