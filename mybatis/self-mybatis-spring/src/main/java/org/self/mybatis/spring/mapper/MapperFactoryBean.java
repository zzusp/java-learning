package org.self.mybatis.spring.mapper;

import org.self.mybatis.session.Configuration;
import org.self.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 17:08 2020/5/24
 * @modified By
 */
public class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T> {
	private Class<T> mapperInterface;
	private boolean addToConfig = true;

	public MapperFactoryBean() {
	}

	public MapperFactoryBean(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	@Override
	protected void checkDaoConfig() {
		super.checkDaoConfig();
		Assert.notNull(this.mapperInterface, "Property 'mapperInterface' is required");
		Configuration configuration = this.getSqlSession().getConfiguration();
		if (this.addToConfig && !configuration.hasMapper(this.mapperInterface)) {
			try {
				configuration.addMapper(this.mapperInterface);
			} catch (Exception var6) {
				this.logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", var6);
				throw new IllegalArgumentException(var6);
			} finally {
//				ErrorContext.instance().reset();
			}
		}

	}

	@Override
	public T getObject() throws Exception {
		return this.getSqlSession().getMapper(this.mapperInterface);
	}

	@Override
	public Class<T> getObjectType() {
		return this.mapperInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setMapperInterface(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	public Class<T> getMapperInterface() {
		return this.mapperInterface;
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	public boolean isAddToConfig() {
		return this.addToConfig;
	}
}
