package org.self.mybatis.spring;

import org.self.mybatis.mapping.Environment;
import org.self.mybatis.session.Configuration;
import org.self.mybatis.session.defaults.DefaultSqlSessionFactory;
import org.self.mybatis.session.defaults.SqlSessionFactory;
import org.self.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.self.mybatis.transaction.TransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Aaron.Sun
 * @description sqlSessionFactory的工厂bean（负责sqlSessionFactory对象的实例化）
 * @date Created in 16:53 2020/5/24
 * @modified By
 */
public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {
	//	private static final Logger LOGGER = LoggerFactory.getLogger(SqlSessionFactoryBean.class);
	private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
	private static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();
	private Resource configLocation;
	private Configuration configuration;
	private Resource[] mapperLocations;
	private DataSource dataSource;
	private TransactionFactory transactionFactory;
	private Properties configurationProperties;
	//	private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
	private SqlSessionFactory sqlSessionFactory;
	private String environment = SqlSessionFactoryBean.class.getSimpleName();
	private boolean failFast;
	//	private Interceptor[] plugins;
//	private TypeHandler<?>[] typeHandlers;
	private String typeHandlersPackage;
	private Class<?>[] typeAliases;
	private String typeAliasesPackage;
	private Class<?> typeAliasesSuperType;
	//	private LanguageDriver[] scriptingLanguageDrivers;
//	private Class<? extends LanguageDriver> defaultScriptingLanguageDriver;
//	private DatabaseIdProvider databaseIdProvider;
//	private Class<? extends VFS> vfs;
	private Cache cache;
	private ObjectFactory objectFactory;
//	private ObjectWrapperFactory objectWrapperFactory;

	public SqlSessionFactoryBean() {
	}

	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

//	public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory) {
//		this.objectWrapperFactory = objectWrapperFactory;
//	}

//	public DatabaseIdProvider getDatabaseIdProvider() {
//		return this.databaseIdProvider;
//	}

//	public void setDatabaseIdProvider(DatabaseIdProvider databaseIdProvider) {
//		this.databaseIdProvider = databaseIdProvider;
//	}

//	public Class<? extends VFS> getVfs() {
//		return this.vfs;
//	}

//	public void setVfs(Class<? extends VFS> vfs) {
//		this.vfs = vfs;
//	}

//	public Cache getCache() {
//		return this.cache;
//	}

//	public void setCache(Cache cache) {
//		this.cache = cache;
//	}

//	public void setPlugins(Interceptor... plugins) {
//		this.plugins = plugins;
//	}

	public void setTypeAliasesPackage(String typeAliasesPackage) {
		this.typeAliasesPackage = typeAliasesPackage;
	}

	public void setTypeAliasesSuperType(Class<?> typeAliasesSuperType) {
		this.typeAliasesSuperType = typeAliasesSuperType;
	}

	public void setTypeHandlersPackage(String typeHandlersPackage) {
		this.typeHandlersPackage = typeHandlersPackage;
	}

//	public void setTypeHandlers(TypeHandler<?>... typeHandlers) {
//		this.typeHandlers = typeHandlers;
//	}

	public void setTypeAliases(Class<?>... typeAliases) {
		this.typeAliases = typeAliases;
	}

	public void setFailFast(boolean failFast) {
		this.failFast = failFast;
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setMapperLocations(Resource... mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

	public void setConfigurationProperties(Properties sqlSessionFactoryProperties) {
		this.configurationProperties = sqlSessionFactoryProperties;
	}

	public void setDataSource(DataSource dataSource) {
		if (dataSource instanceof TransactionAwareDataSourceProxy) {
			this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
		} else {
			this.dataSource = dataSource;
		}

	}

//	public void setSqlSessionFactoryBuilder(SqlSessionFactoryBuilder sqlSessionFactoryBuilder) {
//		this.sqlSessionFactoryBuilder = sqlSessionFactoryBuilder;
//	}

	public void setTransactionFactory(TransactionFactory transactionFactory) {
		this.transactionFactory = transactionFactory;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

//	public void setScriptingLanguageDrivers(LanguageDriver... scriptingLanguageDrivers) {
//		this.scriptingLanguageDrivers = scriptingLanguageDrivers;
//	}

//	public void setDefaultScriptingLanguageDriver(Class<? extends LanguageDriver> defaultScriptingLanguageDriver) {
//		this.defaultScriptingLanguageDriver = defaultScriptingLanguageDriver;
//	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.dataSource, "Property 'dataSource' is required");
//		Assert.notNull(this.sqlSessionFactoryBuilder, "Property 'sqlSessionFactoryBuilder' is required");
		Assert.state(this.configuration == null && this.configLocation == null || this.configuration == null || this.configLocation == null, "Property 'configuration' and 'configLocation' can not specified with together");
		this.sqlSessionFactory = this.buildSqlSessionFactory();
	}

	protected SqlSessionFactory buildSqlSessionFactory() throws Exception {
		Configuration targetConfiguration;
		if (this.configuration != null) {
			targetConfiguration = this.configuration;
		} else {
			targetConfiguration = new Configuration();
		}

		targetConfiguration.setEnvironment(new Environment(this.environment, (TransactionFactory) (this.transactionFactory == null ? new SpringManagedTransactionFactory() : this.transactionFactory), this.dataSource));
		return new DefaultSqlSessionFactory(targetConfiguration);
//		XMLConfigBuilder xmlConfigBuilder = null;
//		Configuration targetConfiguration;
//		Optional var10000;
//		if (this.configuration != null) {
//			targetConfiguration = this.configuration;
//			if (targetConfiguration.getVariables() == null) {
//				targetConfiguration.setVariables(this.configurationProperties);
//			} else if (this.configurationProperties != null) {
//				targetConfiguration.getVariables().putAll(this.configurationProperties);
//			}
//		} else if (this.configLocation != null) {
//			xmlConfigBuilder = new XMLConfigBuilder(this.configLocation.getInputStream(), (String)null, this.configurationProperties);
//			targetConfiguration = xmlConfigBuilder.getConfiguration();
//		} else {
//			LOGGER.debug(() -> {
//				return "Property 'configuration' or 'configLocation' not specified, using default MyBatis Configuration";
//			});
//			targetConfiguration = new Configuration();
//			var10000 = Optional.ofNullable(this.configurationProperties);
//			Objects.requireNonNull(targetConfiguration);
//			var10000.ifPresent(targetConfiguration::setVariables);
//		}
//
//		var10000 = Optional.ofNullable(this.objectFactory);
//		Objects.requireNonNull(targetConfiguration);
//		var10000.ifPresent(targetConfiguration::setObjectFactory);
//		var10000 = Optional.ofNullable(this.objectWrapperFactory);
//		Objects.requireNonNull(targetConfiguration);
//		var10000.ifPresent(targetConfiguration::setObjectWrapperFactory);
//		var10000 = Optional.ofNullable(this.vfs);
//		Objects.requireNonNull(targetConfiguration);
//		var10000.ifPresent(targetConfiguration::setVfsImpl);
//		Stream var24;
//		if (StringUtils.hasLength(this.typeAliasesPackage)) {
//			var24 = this.scanClasses(this.typeAliasesPackage, this.typeAliasesSuperType).stream().filter((clazz) -> {
//				return !clazz.isAnonymousClass();
//			}).filter((clazz) -> {
//				return !clazz.isInterface();
//			}).filter((clazz) -> {
//				return !clazz.isMemberClass();
//			});
//			TypeAliasRegistry var10001 = targetConfiguration.getTypeAliasRegistry();
//			Objects.requireNonNull(var10001);
//			var24.forEach(var10001::registerAlias);
//		}
//
//		if (!ObjectUtils.isEmpty(this.typeAliases)) {
//			Stream.of(this.typeAliases).forEach((typeAlias) -> {
//				targetConfiguration.getTypeAliasRegistry().registerAlias(typeAlias);
//				LOGGER.debug(() -> {
//					return "Registered type alias: '" + typeAlias + "'";
//				});
//			});
//		}
//
//		if (!ObjectUtils.isEmpty(this.plugins)) {
//			Stream.of(this.plugins).forEach((plugin) -> {
//				targetConfiguration.addInterceptor(plugin);
//				LOGGER.debug(() -> {
//					return "Registered plugin: '" + plugin + "'";
//				});
//			});
//		}
//
//		if (StringUtils.hasLength(this.typeHandlersPackage)) {
//			var24 = this.scanClasses(this.typeHandlersPackage, TypeHandler.class).stream().filter((clazz) -> {
//				return !clazz.isAnonymousClass();
//			}).filter((clazz) -> {
//				return !clazz.isInterface();
//			}).filter((clazz) -> {
//				return !Modifier.isAbstract(clazz.getModifiers());
//			});
//			TypeHandlerRegistry var25 = targetConfiguration.getTypeHandlerRegistry();
//			Objects.requireNonNull(var25);
//			var24.forEach(var25::register);
//		}
//
//		if (!ObjectUtils.isEmpty(this.typeHandlers)) {
//			Stream.of(this.typeHandlers).forEach((typeHandler) -> {
//				targetConfiguration.getTypeHandlerRegistry().register(typeHandler);
//				LOGGER.debug(() -> {
//					return "Registered type handler: '" + typeHandler + "'";
//				});
//			});
//		}
//
//		if (!ObjectUtils.isEmpty(this.scriptingLanguageDrivers)) {
//			Stream.of(this.scriptingLanguageDrivers).forEach((languageDriver) -> {
//				targetConfiguration.getLanguageRegistry().register(languageDriver);
//				LOGGER.debug(() -> {
//					return "Registered scripting language driver: '" + languageDriver + "'";
//				});
//			});
//		}
//
//		var10000 = Optional.ofNullable(this.defaultScriptingLanguageDriver);
//		Objects.requireNonNull(targetConfiguration);
//		var10000.ifPresent(targetConfiguration::setDefaultScriptingLanguage);
//		if (this.databaseIdProvider != null) {
//			try {
//				targetConfiguration.setDatabaseId(this.databaseIdProvider.getDatabaseId(this.dataSource));
//			} catch (SQLException var23) {
//				throw new NestedIOException("Failed getting a databaseId", var23);
//			}
//		}
//
//		var10000 = Optional.ofNullable(this.cache);
//		Objects.requireNonNull(targetConfiguration);
//		var10000.ifPresent(targetConfiguration::addCache);
//		if (xmlConfigBuilder != null) {
//			try {
//				xmlConfigBuilder.parse();
//				LOGGER.debug(() -> {
//					return "Parsed configuration file: '" + this.configLocation + "'";
//				});
//			} catch (Exception var21) {
//				throw new NestedIOException("Failed to parse config resource: " + this.configLocation, var21);
//			} finally {
//				ErrorContext.instance().reset();
//			}
//		}
//
//		targetConfiguration.setEnvironment(new Environment(this.environment, (TransactionFactory)(this.transactionFactory == null ? new SpringManagedTransactionFactory() : this.transactionFactory), this.dataSource));
//		if (this.mapperLocations != null) {
//			if (this.mapperLocations.length == 0) {
//				LOGGER.warn(() -> {
//					return "Property 'mapperLocations' was specified but matching resources are not found.";
//				});
//			} else {
//				Resource[] var3 = this.mapperLocations;
//				int var4 = var3.length;
//
//				for(int var5 = 0; var5 < var4; ++var5) {
//					Resource mapperLocation = var3[var5];
//					if (mapperLocation != null) {
//						try {
//							XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(), targetConfiguration, mapperLocation.toString(), targetConfiguration.getSqlFragments());
//							xmlMapperBuilder.parse();
//						} catch (Exception var19) {
//							throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", var19);
//						} finally {
//							ErrorContext.instance().reset();
//						}
//
//						LOGGER.debug(() -> {
//							return "Parsed mapper file: '" + mapperLocation + "'";
//						});
//					}
//				}
//			}
//		} else {
//			LOGGER.debug(() -> {
//				return "Property 'mapperLocations' was not specified.";
//			});
//		}
//
//		return this.sqlSessionFactoryBuilder.build(targetConfiguration);
	}

	@Override
	public SqlSessionFactory getObject() throws Exception {
		if (this.sqlSessionFactory == null) {
			this.afterPropertiesSet();
		}

		return this.sqlSessionFactory;
	}

	@Override
	public Class<? extends SqlSessionFactory> getObjectType() {
		return this.sqlSessionFactory == null ? SqlSessionFactory.class : this.sqlSessionFactory.getClass();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
//		if (this.failFast && event instanceof ContextRefreshedEvent) {
//			this.sqlSessionFactory.getConfiguration().getMappedStatementNames();
//		}

	}

//	private Set<Class<?>> scanClasses(String packagePatterns, Class<?> assignableType) throws IOException {
//		Set<Class<?>> classes = new HashSet();
//		String[] packagePatternArray = StringUtils.tokenizeToStringArray(packagePatterns, ",; \t\n");
//		String[] var5 = packagePatternArray;
//		int var6 = packagePatternArray.length;
//
//		for(int var7 = 0; var7 < var6; ++var7) {
//			String packagePattern = var5[var7];
//			Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources("classpath*:" + ClassUtils.convertClassNameToResourcePath(packagePattern) + "/**/*.class");
//			Resource[] var10 = resources;
//			int var11 = resources.length;
//
//			for(int var12 = 0; var12 < var11; ++var12) {
//				Resource resource = var10[var12];
//
//				try {
//					ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource).getClassMetadata();
//					Class<?> clazz = Resources.classForName(classMetadata.getClassName());
//					if (assignableType == null || assignableType.isAssignableFrom(clazz)) {
//						classes.add(clazz);
//					}
//				} catch (Throwable var16) {
//					LOGGER.warn(() -> {
//						return "Cannot load the '" + resource + "'. Cause by " + var16.toString();
//					});
//				}
//			}
//		}
//
//		return classes;
//	}
}
