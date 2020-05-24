package org.self.mybatis.executor.statement;

import org.self.mybatis.executor.parameter.ParameterHandler;
import org.self.mybatis.executor.resultset.ResultSetHandler;
import org.self.mybatis.mapping.BoundSql;
import org.self.mybatis.mapping.MappedStatement;
import org.self.mybatis.session.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 15:16 2020/5/23
 * @modified By
 */
public class SelfStatementHandler implements StatementHandler {
	protected final Configuration configuration;
	protected final ParameterHandler parameterHandler;
	protected final ResultSetHandler resultSetHandler;
	protected BoundSql boundSql;

	public SelfStatementHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
		this.configuration = mappedStatement.getConfiguration();
		if (boundSql == null) {
			boundSql = mappedStatement.getBoundSql(parameterObject);
		}
		this.boundSql = boundSql;
		this.parameterHandler = this.configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
		this.resultSetHandler = this.configuration.newResultSetHandler();
	}

	public BoundSql getBoundSql() {
		return this.boundSql;
	}

	@Override
	public Statement prepare(Connection connection) throws Exception {
		Statement statement = null;
		try {
			statement = this.instantiateStatement(connection);
			return statement;
		} catch (SQLException var5) {
			this.closeStatement(statement);
			throw var5;
		} catch (Exception var6) {
			this.closeStatement(statement);
			throw new Exception("Error preparing statement.  Cause: " + var6, var6);
		}
	}

	@Override
	public <E> List<E> query(Statement statement) throws SQLException {
		PreparedStatement ps = (PreparedStatement) statement;
		ps.execute();
		// 结果集处理
		return this.resultSetHandler.handleResultSets(statement);
	}

	protected Statement instantiateStatement(Connection connection) throws SQLException {
		String sql = this.boundSql.getSql();
		return connection.prepareStatement(sql);
	}

	protected void closeStatement(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException var3) {
		}
	}

	@Override
	public void parameterize(Statement statement) throws SQLException {
		this.parameterHandler.setParameters((PreparedStatement) statement);
	}
}
