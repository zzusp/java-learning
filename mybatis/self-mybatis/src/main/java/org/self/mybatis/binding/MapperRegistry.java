package org.self.mybatis.binding;

import org.self.mybatis.builder.annotation.MapperAnnotationBuilder;
import org.self.mybatis.session.Configuration;
import org.self.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description mapper注册器
 * @date Created in 14:29 2020/5/22
 * @modified By
 */
public class MapperRegistry {
	private final Configuration config;
	private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

	public MapperRegistry(Configuration config) {
		this.config = config;
	}

	public <T> T getMapper(Class<T> type, SqlSession sqlSession) throws Exception {
		MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory) this.knownMappers.get(type);
		if (mapperProxyFactory == null) {
			throw new Exception("Type " + type + " is not known to the MapperRegistry.");
		} else {
			try {
				return mapperProxyFactory.newInstance(sqlSession);
			} catch (Exception var5) {
				throw new Exception("Error getting mapper instance. Cause: " + var5, var5);
			}
		}
	}

	public <T> boolean hasMapper(Class<T> type) {
		return this.knownMappers.containsKey(type);
	}

	public <T> void addMapper(Class<T> type) throws Exception {
		if (type.isInterface()) {
			if (this.hasMapper(type)) {
				throw new Exception("Type " + type + " is already known to the MapperRegistry.");
			}
			boolean loadCompleted = false;

			try {
				this.knownMappers.put(type, new MapperProxyFactory(type));
				MapperAnnotationBuilder parser = new MapperAnnotationBuilder(this.config, type);
				parser.parse();
				loadCompleted = true;
			} finally {
				if (!loadCompleted) {
					this.knownMappers.remove(type);
				}

			}
		}

	}

}
