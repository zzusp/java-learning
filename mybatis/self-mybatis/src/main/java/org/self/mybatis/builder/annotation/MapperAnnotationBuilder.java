package org.self.mybatis.builder.annotation;


import org.self.mybatis.annotations.SelfSelect;
import org.self.mybatis.builder.MapperBuilderAssistant;
import org.self.mybatis.builder.StaticSqlSource;
import org.self.mybatis.mapping.SqlSource;
import org.self.mybatis.session.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Aaron.Sun
 * @description 注解配置sql的mapper的builder
 * @date Created in 17:24 2020/5/22
 * @modified By
 */
public class MapperAnnotationBuilder {
	private final Configuration configuration;
	private final MapperBuilderAssistant assistant;
	private final Class<?> type;

	public MapperAnnotationBuilder(Configuration configuration, Class<?> type) {
		this.assistant = new MapperBuilderAssistant(configuration);
		this.configuration = configuration;
		this.type = type;
	}

	public void parse() {
		Method[] methods = this.type.getMethods();
		Method[] var3 = methods;
		int var4 = methods.length;
		for (int var5 = 0; var5 < var4; ++var5) {
			Method method = var3[var5];
			try {
				// 判断是否不为桥接方法
				if (!method.isBridge()) {
					this.parseStatement(method);
				}
			} catch (Exception var8) {
				// 如果解析时有异常，通过configuration的addIncompleteMethod方法
				// 将有问题的方法放入configuration中的不完整方法集合中
				// this.configuration.addIncompleteMethod(new MethodResolver(this, method));
				System.out.println("Error parse statement, Method: " + method.getName());
			}
		}
	}

	void parseStatement(Method method) throws Exception {
		// 此处原代码会针对方法参数类型进行处理
		// 多个参数会将多个参数的类型封装到内部对象（org.apache.ibatis.binding.ParamMap，org.apache.ibatis.binding.MapperMethod的内部抽象类）中
		// 在这里做简化操作，只取第一个参数的类型
		SqlSource sqlSource = this.getSqlSourceFromAnnotations(method, method.getParameterTypes()[0]);
		if (sqlSource != null) {
			String mappedStatementId = this.type.getName() + "." + method.getName();
			this.assistant.addMappedStatement(mappedStatementId, sqlSource);
		}
	}

	/**
	 * 从注解中获取sqlSource
	 *
	 * @param method        mapper方法
	 * @param parameterType 参数类型
	 * @return 存储sql语句及参数的对象
	 * @throws Exception
	 */
	private SqlSource getSqlSourceFromAnnotations(Method method, Class<?> parameterType) throws Exception {
		try {
			// 获取方法上的注解，如果是支持的类型则返回，不支持的类型返回null
			Class<? extends Annotation> sqlAnnotationType = this.chooseAnnotationType(method);
			Annotation sqlProviderAnnotation;
			// 判断注解类型是否不为null
			if (sqlAnnotationType != null) {
				sqlProviderAnnotation = method.getAnnotation(sqlAnnotationType);
				String[] strings = (String[]) sqlProviderAnnotation.getClass().getMethod("value").invoke(sqlProviderAnnotation);
				return this.buildSqlSourceFromStrings(strings, parameterType);
			} else {
				return null;
			}
		} catch (Exception var8) {
			throw new Exception("Could not find value method on SQL annotation.  Cause: " + var8, var8);
		}
	}

	private SqlSource buildSqlSourceFromStrings(String[] strings, Class<?> parameterTypeClass) {
		StringBuilder sql = new StringBuilder();
		String[] var5 = strings;
		int var6 = strings.length;

		for (int var7 = 0; var7 < var6; ++var7) {
			String fragment = var5[var7];
			sql.append(fragment);
			sql.append(" ");
		}
		// 此处替换sql中的#{}为?
		// 在mybatis中是使用LanguageDriver的对象进行这部分处理，此处简化
		String sqlStr = sql.toString();
		int startIndex = sqlStr.indexOf("#{");
		if (startIndex > 0) {
			int endIndex = sqlStr.indexOf("}", startIndex);
			sqlStr = sqlStr.substring(0, startIndex) + "?" + sqlStr.substring(endIndex + 1);
		}
		return new StaticSqlSource(sqlStr);
	}

	/**
	 * 获取方法上的注解，如果是支持的类型则返回，不支持的类型返回null
	 *
	 * @param method mapper方法
	 * @return 注解类型
	 */
	private Class<? extends Annotation> chooseAnnotationType(Method method) {
		Set<Class<? extends Annotation>> types = new HashSet<>(1);
		types.add(SelfSelect.class);
		Iterator<Class<? extends Annotation>> var3 = types.iterator();

		Class<? extends Annotation> type;
		Annotation annotation;
		do {
			if (!var3.hasNext()) {
				return null;
			}
			type = var3.next();
			annotation = method.getAnnotation(type);
		} while (annotation == null);

		return type;
	}
}
