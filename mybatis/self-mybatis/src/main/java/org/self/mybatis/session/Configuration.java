package org.self.mybatis.session;

import org.self.mybatis.binding.MapperRegistry;
import org.self.mybatis.builder.annotation.MethodResolver;
import org.self.mybatis.executor.Executor;
import org.self.mybatis.executor.SelfExecutor;
import org.self.mybatis.executor.parameter.DefaultParameterHandler;
import org.self.mybatis.executor.parameter.ParameterHandler;
import org.self.mybatis.executor.resultset.DefaultResultSetHandler;
import org.self.mybatis.executor.resultset.ResultSetHandler;
import org.self.mybatis.executor.statement.SelfStatementHandler;
import org.self.mybatis.executor.statement.StatementHandler;
import org.self.mybatis.mapping.BoundSql;
import org.self.mybatis.mapping.Environment;
import org.self.mybatis.mapping.MappedStatement;
import org.self.mybatis.transaction.Transaction;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 记录mybatis主要配置的核心配置类
 * @date Created in 14:28 2020/5/22
 * @modified By
 */
public class Configuration {
	/** 环境配置 */
	protected Environment environment;
	/** 默认执行类型 */
	protected ExecutorType defaultExecutorType;
	/** mapper注册器 */
	protected final MapperRegistry mapperRegistry;
	protected final Map<String, MappedStatement> mappedStatements;
	/** 存放所有需要解析的mapper接口方法 */
	protected final Collection<MethodResolver> incompleteMethods;

	public Configuration(Environment environment) {
		this();
		this.environment = environment;
	}

	public Configuration() {
		this.defaultExecutorType = ExecutorType.SIMPLE;
		this.mappedStatements = new HashMap<>();
		this.mapperRegistry = new MapperRegistry(this);
		this.incompleteMethods = new LinkedList<>();
	}

	public Environment getEnvironment() {
		return this.environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public ExecutorType getDefaultExecutorType() {
		return this.defaultExecutorType;
	}

	public void setDefaultExecutorType(ExecutorType defaultExecutorType) {
		this.defaultExecutorType = defaultExecutorType;
	}

	public MapperRegistry getMapperRegistry() {
		return mapperRegistry;
	}

	public <T> void addMapper(Class<T> type) throws Exception {
		this.mapperRegistry.addMapper(type);
	}

	public <T> T getMapper(Class<T> type, SqlSession sqlSession) throws Exception {
		return this.mapperRegistry.getMapper(type, sqlSession);
	}

	public void addMappedStatement(MappedStatement ms) {
		this.mappedStatements.put(ms.getId(), ms);
	}

	public Map<String, MappedStatement> getMappedStatements() {
		return mappedStatements;
	}

	public MappedStatement getMappedStatement(String id) {
		return this.getMappedStatement(id, true);
	}

	public MappedStatement getMappedStatement(String id, boolean validateIncompleteStatements) {
		if (validateIncompleteStatements) {
			this.buildAllStatements();
		}
		return this.mappedStatements.get(id);
	}

	public boolean hasMapper(Class<?> type) {
		return this.mapperRegistry.hasMapper(type);
	}

	protected void buildAllStatements() {
		// 解析返回结果集
//		this.parsePendingResultMaps();
		// 解析缓存
//		if (!this.incompleteCacheRefs.isEmpty()) {
//			synchronized(this.incompleteCacheRefs) {
//				this.incompleteCacheRefs.removeIf((x) -> {
//					return x.resolveCacheRef() != null;
//				});
//			}
//		}
		// 解析mapper.xml文件内容
//		if (!this.incompleteStatements.isEmpty()) {
//			synchronized(this.incompleteStatements) {
//				this.incompleteStatements.removeIf((x) -> {
//					x.parseStatementNode();
//					return true;
//				});
//			}
//		}
		// 解析mapper接口中的方法（注解中的查询语句）
//		if (!this.incompleteMethods.isEmpty()) {
//			synchronized(this.incompleteMethods) {
//				this.incompleteMethods.removeIf((x) -> {
//					x.resolve();
//					return true;
//				});
//			}
//		}

	}

	public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
		// ParameterHandler parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement, parameterObject, boundSql);
		// parameterHandler = (ParameterHandler)this.interceptorChain.pluginAll(parameterHandler);
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
		return parameterHandler;
	}

	public ResultSetHandler newResultSetHandler() {
		ResultSetHandler resultSetHandler = new DefaultResultSetHandler();
		// ResultSetHandler resultSetHandler = (ResultSetHandler)this.interceptorChain.pluginAll(resultSetHandler);
		return resultSetHandler;
	}

	public StatementHandler newStatementHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
		StatementHandler statementHandler = new SelfStatementHandler(mappedStatement, parameterObject, boundSql);
		// StatementHandler statementHandler = (StatementHandler)this.interceptorChain.pluginAll(statementHandler);
		return statementHandler;
	}

	/**
	 * 创建默认执行器
	 *
	 * @param transaction 事务对象
	 * @return 执行器实例
	 */
	public Executor newExecutor(Transaction transaction) {
		return this.newExecutor(transaction, this.defaultExecutorType);
	}

	/**
	 * 创建执行器
	 * 此处mybatis源码中有多个执行器，此处简化，使用自定义的执行器
	 *
	 * @param transaction  事务对象
	 * @param executorType 执行器类型
	 * @return 执行器实例
	 */
	public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
//		executorType = executorType == null ? this.defaultExecutorType : executorType;
//		executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
//		Object executor;
//		if (ExecutorType.BATCH == executorType) {
//			executor = new BatchExecutor(this, transaction);
//		} else if (ExecutorType.REUSE == executorType) {
//			executor = new ReuseExecutor(this, transaction);
//		} else {
//			executor = new SimpleExecutor(this, transaction);
//		}
//
//		if (this.cacheEnabled) {
//			executor = new CachingExecutor((Executor)executor);
//		}
//
//		Executor executor = (Executor)this.interceptorChain.pluginAll(executor);
		return new SelfExecutor(this, transaction);
	}
}
