package org.self.mybatis.builder;

import org.self.mybatis.mapping.MappedStatement;
import org.self.mybatis.mapping.SqlSource;
import org.self.mybatis.session.Configuration;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 10:27 2020/5/23
 * @modified By
 */
public class MapperBuilderAssistant extends BaseBuilder {

	public MapperBuilderAssistant(Configuration configuration) {
		super(configuration);
	}

	public MappedStatement addMappedStatement(String id, SqlSource sqlSource) {
		MappedStatement.Builder statementBuilder = (new MappedStatement.Builder(this.configuration, id, sqlSource));

		MappedStatement statement = statementBuilder.build();
		this.configuration.addMappedStatement(statement);
		return statement;
	}

}
