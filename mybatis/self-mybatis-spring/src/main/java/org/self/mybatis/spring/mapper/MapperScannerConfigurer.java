package org.self.mybatis.spring.mapper;

import org.self.mybatis.session.defaults.SqlSessionFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

/**
 * @author Aaron.Sun
 * @description mapper扫描配置器
 * @date Created in 10:27 2020/5/24
 * @modified By
 */
public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {
	private String basePackage;
	private boolean addToConfig = true;
	private String lazyInitialization;
	private SqlSessionFactory sqlSessionFactory;
	//	private SqlSessionTemplate sqlSessionTemplate;
	private String sqlSessionFactoryBeanName;
	private String sqlSessionTemplateBeanName;
	private Class<? extends Annotation> annotationClass;
	private Class<?> markerInterface;
	//	private Class<? extends MapperFactoryBean> mapperFactoryBeanClass;
	private ApplicationContext applicationContext;
	private String beanName;
	private boolean processPropertyPlaceHolders;
	private BeanNameGenerator nameGenerator;

	public MapperScannerConfigurer() {
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	public void setLazyInitialization(String lazyInitialization) {
		this.lazyInitialization = lazyInitialization;
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public void setMarkerInterface(Class<?> superClass) {
		this.markerInterface = superClass;
	}

	/** @deprecated  */
//	@Deprecated
//	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
//		this.sqlSessionTemplate = sqlSessionTemplate;
//	}
	public void setSqlSessionTemplateBeanName(String sqlSessionTemplateName) {
		this.sqlSessionTemplateBeanName = sqlSessionTemplateName;
	}

	/** @deprecated  */
	@Deprecated
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public void setSqlSessionFactoryBeanName(String sqlSessionFactoryName) {
		this.sqlSessionFactoryBeanName = sqlSessionFactoryName;
	}

	public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders) {
		this.processPropertyPlaceHolders = processPropertyPlaceHolders;
	}

//	public void setMapperFactoryBeanClass(Class<? extends MapperFactoryBean> mapperFactoryBeanClass) {
//		this.mapperFactoryBeanClass = mapperFactoryBeanClass;
//	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	public BeanNameGenerator getNameGenerator() {
		return this.nameGenerator;
	}

	public void setNameGenerator(BeanNameGenerator nameGenerator) {
		this.nameGenerator = nameGenerator;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.basePackage, "Property 'basePackage' is required");
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
	}

	/**
	 * bd注册回调
	 * 在MapperScannerRegistrar方法中有注册一个MapperScannerConfigurer类型的bd实例，所以该方法会触发
	 * BeanDefinitionBuilder设置的属性，会在bd注册完成后，设置在实例对象的对应属性上
	 *
	 * @param registry bd注册器
	 */
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
		if (this.processPropertyPlaceHolders) {
			this.processPropertyPlaceHolders();
		}

		// 创建扫描器，并将bd注册器传入
		ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
		// 设置属性
//		scanner.setAddToConfig(this.addToConfig);
//		scanner.setAnnotationClass(this.annotationClass);
//		scanner.setMarkerInterface(this.markerInterface);
//		scanner.setSqlSessionFactory(this.sqlSessionFactory);
//		scanner.setSqlSessionTemplate(this.sqlSessionTemplate);
//		scanner.setSqlSessionFactoryBeanName(this.sqlSessionFactoryBeanName);
//		scanner.setSqlSessionTemplateBeanName(this.sqlSessionTemplateBeanName);
//		scanner.setResourceLoader(this.applicationContext);
//		scanner.setBeanNameGenerator(this.nameGenerator);
//		scanner.setMapperFactoryBeanClass(this.mapperFactoryBeanClass);
//		if (StringUtils.hasText(this.lazyInitialization)) {
//			scanner.setLazyInitialization(Boolean.valueOf(this.lazyInitialization));
//		}
		// 注册过滤器
		scanner.registerFilters();
		scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ",; \t\n"));
	}

	/**
	 * 处理property文件的读取
	 */
	private void processPropertyPlaceHolders() {
//		Map<String, PropertyResourceConfigurer> prcs = this.applicationContext.getBeansOfType(PropertyResourceConfigurer.class);
//		if (!prcs.isEmpty() && this.applicationContext instanceof ConfigurableApplicationContext) {
//			BeanDefinition mapperScannerBean = ((ConfigurableApplicationContext)this.applicationContext).getBeanFactory().getBeanDefinition(this.beanName);
//			DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
//			factory.registerBeanDefinition(this.beanName, mapperScannerBean);
//			Iterator var4 = prcs.values().iterator();
//
//			while(var4.hasNext()) {
//				PropertyResourceConfigurer prc = (PropertyResourceConfigurer)var4.next();
//				prc.postProcessBeanFactory(factory);
//			}
//
//			PropertyValues values = mapperScannerBean.getPropertyValues();
//			this.basePackage = this.updatePropertyValue("basePackage", values);
//			this.sqlSessionFactoryBeanName = this.updatePropertyValue("sqlSessionFactoryBeanName", values);
//			this.sqlSessionTemplateBeanName = this.updatePropertyValue("sqlSessionTemplateBeanName", values);
//			this.lazyInitialization = this.updatePropertyValue("lazyInitialization", values);
//		}
//
//		Optional var10001 = Optional.ofNullable(this.basePackage);
//		Environment var10002 = this.getEnvironment();
//		Objects.requireNonNull(var10002);
//		this.basePackage = (String)var10001.map(var10002::resolvePlaceholders).orElse((Object)null);
//		var10001 = Optional.ofNullable(this.sqlSessionFactoryBeanName);
//		var10002 = this.getEnvironment();
//		Objects.requireNonNull(var10002);
//		this.sqlSessionFactoryBeanName = (String)var10001.map(var10002::resolvePlaceholders).orElse((Object)null);
//		var10001 = Optional.ofNullable(this.sqlSessionTemplateBeanName);
//		var10002 = this.getEnvironment();
//		Objects.requireNonNull(var10002);
//		this.sqlSessionTemplateBeanName = (String)var10001.map(var10002::resolvePlaceholders).orElse((Object)null);
//		var10001 = Optional.ofNullable(this.lazyInitialization);
//		var10002 = this.getEnvironment();
//		Objects.requireNonNull(var10002);
//		this.lazyInitialization = (String)var10001.map(var10002::resolvePlaceholders).orElse((Object)null);
	}

	/**
	 * 读取容器的环境配置
	 *
	 * @return 容器的环境配置
	 */
	private Environment getEnvironment() {
		return this.applicationContext.getEnvironment();
	}

	/**
	 * 由property名称获取property值
	 *
	 * @param propertyName property名称
	 * @param values       property配置信息
	 * @return property值
	 */
	private String updatePropertyValue(String propertyName, PropertyValues values) {
		PropertyValue property = values.getPropertyValue(propertyName);
		if (property == null) {
			return null;
		} else {
			Object value = property.getValue();
			if (value == null) {
				return null;
			} else if (value instanceof String) {
				return value.toString();
			} else {
				return value instanceof TypedStringValue ? ((TypedStringValue) value).getValue() : null;
			}
		}
	}
}
