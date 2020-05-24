package org.self.mybatis.spring.annotation;

import org.self.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aaron.Sun
 * @description mapper扫描注册器
 * @date Created in 10:21 2020/5/24
 * @modified By
 */
public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

	public MapperScannerRegistrar() {
	}

	/** @deprecated  */
	@Deprecated
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
	}

	/**
	 * 用来注册额外的bd的回调方法
	 *
	 * @param importingClassMetadata 配置了@Import或@ImportSelector注解的类，生成的注解元数据。元数据的introspectedClass属性就是该类
	 * @param registry               bd注册器
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		// 获取SelfMapperScan注解中配置的属性，并存入mapperScanAttrs
		AnnotationAttributes mapperScanAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(SelfMapperScan.class.getName()));
		if (mapperScanAttrs != null) {
			// 如果SelfMapperScan注解中存在配置，那么开始注册
			this.registerBeanDefinitions(importingClassMetadata, mapperScanAttrs, registry, generateBaseBeanName(importingClassMetadata, 0));
		}

	}

	/**
	 * 注册bd
	 *
	 * @param annoMeta  配置了@Import或@ImportSelector注解的类，生成的注解元数据。元数据的introspectedClass属性就是该类
	 * @param annoAttrs 封装后的SelfMapperScan注解中的配置
	 * @param registry  注册器
	 * @param beanName  扫描注册器在容器中的bean name（注意：扫描注册器在容器中的实例是MapperScannerConfigurer类型的）
	 */
	void registerBeanDefinitions(AnnotationMetadata annoMeta, AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry, String beanName) {
		// 1.创建一个bd创建器
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
		builder.addPropertyValue("processPropertyPlaceHolders", true);
		// 2.读取annoAttrs中的属性，并设置到builder中
//		Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
//		if (!Annotation.class.equals(annotationClass)) {
//			builder.addPropertyValue("annotationClass", annotationClass);
//		}
//
//		Class<?> markerInterface = annoAttrs.getClass("markerInterface");
//		if (!Class.class.equals(markerInterface)) {
//			builder.addPropertyValue("markerInterface", markerInterface);
//		}
//
//		Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
//		if (!BeanNameGenerator.class.equals(generatorClass)) {
//			builder.addPropertyValue("nameGenerator", BeanUtils.instantiateClass(generatorClass));
//		}
//
//		Class<? extends MapperFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
//		if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
//			builder.addPropertyValue("mapperFactoryBeanClass", mapperFactoryBeanClass);
//		}
//
//		String sqlSessionTemplateRef = annoAttrs.getString("sqlSessionTemplateRef");
//		if (StringUtils.hasText(sqlSessionTemplateRef)) {
//			builder.addPropertyValue("sqlSessionTemplateBeanName", annoAttrs.getString("sqlSessionTemplateRef"));
//		}
//
//		String sqlSessionFactoryRef = annoAttrs.getString("sqlSessionFactoryRef");
//		if (StringUtils.hasText(sqlSessionFactoryRef)) {
//			builder.addPropertyValue("sqlSessionFactoryBeanName", annoAttrs.getString("sqlSessionFactoryRef"));
//		}
		// 3.读取注解中的扫描路径，并添加到一个集合中
		List<String> basePackages = new ArrayList<>();
		basePackages.addAll((Collection) Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
//		basePackages.addAll((Collection) Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
//		basePackages.addAll((Collection) Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName).collect(Collectors.toList()));
		if (basePackages.isEmpty()) {
			// 如果未配置扫描路径，则将截取annoMeta的className（className即introspectedClass属性的name）中的包路径放入扫描路径集合
			basePackages.add(getDefaultBasePackage(annoMeta));
		}
		// 4.读取annoAttrs中的lazyInitialization属性，并设置到builder中
		// 5.将扫描路径用英文逗号拼接为一个字符串，并存入builder中
		builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
		// 6.注册bd到容器
		registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
	}

	/**
	 * 生成一个base bean name
	 *
	 * @param importingClassMetadata spring封装后的注解元数据（className即introspectedClass属性的name）
	 * @param index                  序号（用于配置了多个SelfMapperScan注解时区分）
	 * @return base bean name
	 */
	private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
		return importingClassMetadata.getClassName() + "#" + MapperScannerRegistrar.class.getSimpleName() + "#" + index;
	}

	/**
	 * 截取注解元数据中的的className（className即introspectedClass属性的name）中的包路径
	 *
	 * @param importingClassMetadata 注解元数据
	 * @return 包路径
	 */
	private static String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
		return ClassUtils.getPackageName(importingClassMetadata.getClassName());
	}

	/**
	 * 静态内部类
	 * 用来处理SelfMapperScans注解的扫描注册
	 */
	static class RepeatingRegistrar extends MapperScannerRegistrar {
		RepeatingRegistrar() {
		}

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
			AnnotationAttributes mapperScansAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(SelfMapperScans.class.getName()));
			if (mapperScansAttrs != null) {
				AnnotationAttributes[] annotations = mapperScansAttrs.getAnnotationArray("value");

				for (int i = 0; i < annotations.length; ++i) {
					this.registerBeanDefinitions(importingClassMetadata, annotations[i], registry, MapperScannerRegistrar.generateBaseBeanName(importingClassMetadata, i));
				}
			}

		}
	}

}
