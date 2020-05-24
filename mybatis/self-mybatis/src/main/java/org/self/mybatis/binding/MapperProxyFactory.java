package org.self.mybatis.binding;

import org.self.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

/**
 * @author Aaron.Sun
 * @description 生产mapper代理对象的工厂
 * @date Created in 14:49 2020/5/22
 * @modified By
 */
public class MapperProxyFactory<T> {

	private final Class<T> mapperInterface;

	public MapperProxyFactory(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	@SuppressWarnings("unchecked")
	protected T newInstance(MapperProxy<T> mapperProxy) {
		return (T) Proxy.newProxyInstance(this.mapperInterface.getClassLoader(), new Class[]{this.mapperInterface}, mapperProxy);
	}

	public T newInstance(SqlSession sqlSession) {
		MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, this.mapperInterface);
		return this.newInstance(mapperProxy);
	}

}
