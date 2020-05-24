package org.self.mybatis.builder;

import org.self.mybatis.session.Configuration;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 10:27 2020/5/23
 * @modified By
 */
public abstract class BaseBuilder {
	protected final Configuration configuration;

	public BaseBuilder(Configuration configuration) {
		this.configuration = configuration;
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

}
