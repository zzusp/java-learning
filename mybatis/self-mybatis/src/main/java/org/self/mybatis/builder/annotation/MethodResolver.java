package org.self.mybatis.builder.annotation;

import java.lang.reflect.Method;

/**
 * @author Aaron.Sun
 * @description mapper接口中的方法解析
 * @date Created in 9:49 2020/5/23
 * @modified By
 */
public class MethodResolver {

	private final MapperAnnotationBuilder annotationBuilder;
	private final Method method;

	public MethodResolver(MapperAnnotationBuilder annotationBuilder, Method method) {
		this.annotationBuilder = annotationBuilder;
		this.method = method;
	}

	public void resolve() throws Exception {
		this.annotationBuilder.parseStatement(this.method);
	}

}
