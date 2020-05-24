package org.self.mybatis.builder;

import org.self.mybatis.mapping.BoundSql;
import org.self.mybatis.mapping.SqlSource;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 18:31 2020/5/22
 * @modified By
 */
public class StaticSqlSource implements SqlSource {

	private final String sql;

	public StaticSqlSource(String sql) {
		this.sql = sql;
	}

	public BoundSql getBoundSql(String sql, Object parameterObject) {
		return new BoundSql(sql, parameterObject);
	}

	@Override
	public BoundSql getBoundSql(Object parameterObject) {
		return new BoundSql(this.sql, parameterObject);
	}
}
