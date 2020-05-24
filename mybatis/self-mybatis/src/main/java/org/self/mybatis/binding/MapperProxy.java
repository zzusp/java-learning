package org.self.mybatis.binding;

import org.self.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 14:52 2020/5/22
 * @modified By
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

	private final SqlSession sqlSession;
	private final Class<T> mapperInterface;

	public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
		this.sqlSession = sqlSession;
		this.mapperInterface = mapperInterface;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// mybatis这里有一些判断及处理
		// 简化掉Object.class.equals(method.getDeclaringClass())判断
		// 简化掉m.isDefault()，方法是否为default方法的判断，如果是，则判断是调用jdk8的方法，还是jdk9的方法
		return new MapperProxy.PlainMethodInvoker(new MapperMethod(this.mapperInterface, method, this.sqlSession.getConfiguration())).invoke(proxy, method, args, this.sqlSession);
	}

	private static class PlainMethodInvoker implements MapperProxy.MapperMethodInvoker {
		private final MapperMethod mapperMethod;

		public PlainMethodInvoker(MapperMethod mapperMethod) {
			this.mapperMethod = mapperMethod;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args, SqlSession sqlSession) throws Throwable {
			return this.mapperMethod.execute(sqlSession, args);
		}
	}

	interface MapperMethodInvoker {
		Object invoke(Object var1, Method var2, Object[] var3, SqlSession var4) throws Throwable;
	}

}
