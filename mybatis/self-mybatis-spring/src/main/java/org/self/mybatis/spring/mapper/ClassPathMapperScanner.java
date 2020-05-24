package org.self.mybatis.spring.mapper;

import org.self.mybatis.session.defaults.SqlSessionFactory;
import org.self.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Aaron.Sun
 * @description mapper扫描器
 * @date Created in 15:38 2020/5/24
 * @modified By
 */
public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
	//	private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathMapperScanner.class);
	private boolean addToConfig = true;
	private boolean lazyInitialization;
	private SqlSessionFactory sqlSessionFactory;
	private SqlSessionTemplate sqlSessionTemplate;
	private String sqlSessionTemplateBeanName;
	private String sqlSessionFactoryBeanName;
	private Class<? extends Annotation> annotationClass;
	private Class<?> markerInterface;
	private Class<? extends MapperFactoryBean> mapperFactoryBeanClass = MapperFactoryBean.class;

	public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
		super(registry, false);
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public void setLazyInitialization(boolean lazyInitialization) {
		this.lazyInitialization = lazyInitialization;
	}

	public void setMarkerInterface(Class<?> markerInterface) {
		this.markerInterface = markerInterface;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public void setSqlSessionTemplateBeanName(String sqlSessionTemplateBeanName) {
		this.sqlSessionTemplateBeanName = sqlSessionTemplateBeanName;
	}

	public void setSqlSessionFactoryBeanName(String sqlSessionFactoryBeanName) {
		this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
	}

//	/** @deprecated */
//	@Deprecated
//	public void setMapperFactoryBean(MapperFactoryBean<?> mapperFactoryBean) {
//		this.mapperFactoryBeanClass = mapperFactoryBean == null ? MapperFactoryBean.class : mapperFactoryBean.getClass();
//	}

	public void setMapperFactoryBeanClass(Class<? extends MapperFactoryBean> mapperFactoryBeanClass) {
		this.mapperFactoryBeanClass = mapperFactoryBeanClass == null ? MapperFactoryBean.class : mapperFactoryBeanClass;
	}

	/**
	 * 注册过滤器
	 */
	public void registerFilters() {
		boolean acceptAllInterfaces = true;
		if (this.annotationClass != null) {
			// spring提供的注解过滤器
			this.addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
			acceptAllInterfaces = false;
		}

		if (this.markerInterface != null) {
			// spring提供的可转让（包含接口、超类等）过滤器
			this.addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
				@Override
				protected boolean matchClassName(String className) {
					return false;
				}
			});
			acceptAllInterfaces = false;
		}

		if (acceptAllInterfaces) {
			// 如果没有指定需要包含的注解或接口，则扫描所有
			this.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
				return true;
			});
		}

		// 过滤package-info类
		this.addExcludeFilter((metadataReader, metadataReaderFactory) -> {
			String className = metadataReader.getClassMetadata().getClassName();
			return className.endsWith("package-info");
		});
	}

	@Override
	public Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
		if (beanDefinitions.isEmpty()) {
//			LOGGER.warn(() -> {
//				return "No MyBatis mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.";
//			});
		} else {
			this.processBeanDefinitions(beanDefinitions);
		}

		return beanDefinitions;
	}

	private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
		GenericBeanDefinition definition;
		for (Iterator var3 = beanDefinitions.iterator(); var3.hasNext(); definition.setLazyInit(this.lazyInitialization)) {
			BeanDefinitionHolder holder = (BeanDefinitionHolder) var3.next();
			definition = (GenericBeanDefinition) holder.getBeanDefinition();
			String beanClassName = definition.getBeanClassName();
//			LOGGER.debug(() -> {
//				return "Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" + beanClassName + "' mapperInterface";
//			});
			definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
			definition.setBeanClass(this.mapperFactoryBeanClass);
//			definition.getPropertyValues().add("addToConfig", this.addToConfig);
			boolean explicitFactoryUsed = false;
			if (StringUtils.hasText(this.sqlSessionFactoryBeanName)) {
				definition.getPropertyValues().add("sqlSessionFactory", new RuntimeBeanReference(this.sqlSessionFactoryBeanName));
				explicitFactoryUsed = true;
			} else if (this.sqlSessionFactory != null) {
				definition.getPropertyValues().add("sqlSessionFactory", this.sqlSessionFactory);
				explicitFactoryUsed = true;
			}

			if (StringUtils.hasText(this.sqlSessionTemplateBeanName)) {
				if (explicitFactoryUsed) {
//					LOGGER.warn(() -> {
//						return "Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.";
//					});
				}

				definition.getPropertyValues().add("sqlSessionTemplate", new RuntimeBeanReference(this.sqlSessionTemplateBeanName));
				explicitFactoryUsed = true;
//			} else if (this.sqlSessionTemplate != null) {
//				if (explicitFactoryUsed) {
//					LOGGER.warn(() -> {
//						return "Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.";
//					});
//				}
//
//				definition.getPropertyValues().add("sqlSessionTemplate", this.sqlSessionTemplate);
//				explicitFactoryUsed = true;
			}

			if (!explicitFactoryUsed) {
//				LOGGER.debug(() -> {
//					return "Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName() + "'.";
//				});
				definition.setAutowireMode(2);
			}
		}

	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
	}

	@Override
	protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
		if (super.checkCandidate(beanName, beanDefinition)) {
			return true;
		} else {
//			LOGGER.warn(() -> {
//				return "Skipping MapperFactoryBean with name '" + beanName + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface. Bean already defined with the same name!";
//			});
			return false;
		}
	}
}
