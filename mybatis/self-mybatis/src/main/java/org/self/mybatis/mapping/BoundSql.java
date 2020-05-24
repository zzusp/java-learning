package org.self.mybatis.mapping;

/**
 * @author Aaron.Sun
 * @description sql对象
 * @date Created in 18:11 2020/5/22
 * @modified By
 */
public class BoundSql {

	private final String sql;
	private final Object parameterObject;

	public BoundSql(String sql, Object parameterObject) {
		this.sql = sql;
		this.parameterObject = parameterObject;
	}

	public String getSql() {
		return sql;
	}

	public Object getParameters() {
		return parameterObject;
	}
}
