package org.self.mybatis.binding;

import org.self.mybatis.session.Configuration;
import org.self.mybatis.session.SqlSession;

import java.lang.reflect.Method;

/**
 * @author Aaron.Sun
 * @description mapper的方法调用（JDK动态代理）及处理
 * @date Created in 15:31 2020/5/22
 * @modified By
 */
public class MapperMethod {
	private final Class<?> mapperInterface;
	private final Method method;

	public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
		this.mapperInterface = mapperInterface;
		this.method = method;
	}

	public Object execute(SqlSession sqlSession, Object[] args) throws Exception {
		return sqlSession.selectOne(mapperInterface.getName() + "." + this.method.getName(), args[0]);
	}

}
