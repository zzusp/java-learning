package org.self.mybatis.mapping;

import org.self.mybatis.session.Configuration;

/**
 * @author Aaron.Sun
 * @description 生成statement对象的信息
 * @date Created in 17:20 2020/5/22
 * @modified By
 */
public final class MappedStatement {
	private Configuration configuration;
	private String id;
	private SqlSource sqlSource;

	MappedStatement() {
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

	public String getId() {
		return this.id;
	}

	public BoundSql getBoundSql(Object parameterObject) {
		return this.sqlSource.getBoundSql(parameterObject);
	}

	public static class Builder {
		private MappedStatement mappedStatement = new MappedStatement();

		public Builder(Configuration configuration, String id, SqlSource sqlSource) {
			this.mappedStatement.configuration = configuration;
			this.mappedStatement.id = id;
			this.mappedStatement.sqlSource = sqlSource;
		}

		public String id() {
			return this.mappedStatement.id;
		}

		public MappedStatement build() {
			assert this.mappedStatement.configuration != null;
			assert this.mappedStatement.id != null;
			assert this.mappedStatement.sqlSource != null;
			return this.mappedStatement;
		}
	}
}
