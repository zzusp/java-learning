package org.self.mybatis.executor.parameter;

import org.self.mybatis.mapping.BoundSql;
import org.self.mybatis.mapping.MappedStatement;
import org.self.mybatis.session.Configuration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 15:59 2020/5/23
 * @modified By
 */
public class DefaultParameterHandler implements ParameterHandler {
	private final MappedStatement mappedStatement;
	private final Object parameterObject;
	private final BoundSql boundSql;
	private final Configuration configuration;

	public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
		this.mappedStatement = mappedStatement;
		this.configuration = mappedStatement.getConfiguration();
		this.parameterObject = parameterObject;
		this.boundSql = boundSql;
	}

	@Override
	public Object getParameterObject() {
		return this.parameterObject;
	}

	@Override
	public void setParameters(PreparedStatement var1) throws SQLException {
		// 设置参数。mybatis在这里使用了TypeHandler接口及BaseTypeHandler抽象类的不同实现，对不同类型的参数进行不同的处理
		var1.setString(1, (String) this.parameterObject);
	}
}
