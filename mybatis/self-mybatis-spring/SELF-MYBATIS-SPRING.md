```
self-mybatis-spring（简化版mybatis-spring，主要用于学习在spring中，mybatis的加载机制和执行原理）
└── java
     └── org.self.mybatis.spring
          ├── annotations -- 注解相关的文件夹
          |    ├── MapperScannerRegistrar -- mapper扫描注册器
          |    ├── SelfMapperScan -- 自定义mapper扫描注解（配置需要扫描的mapper的路径）
          |    └── SelfMapperScans -- 自定义mapper扫描注解，可配置多个（配置需要扫描的mapper的路径）
          ├── mapper -- mapper相关的文件夹
          |    ├── ClassPathMapperScanner -- mapper扫描器
          |    ├── MapperFactoryBean -- mapper的工厂bean（用来生成mapper实例）
          |    └── MapperScannerConfigurer -- 配置mapper扫描器（并将ClassPathMapperScanner注入到spring容器中）
          ├── support -- 帮助相关的文件夹
          |    └── SqlSessionDaoSupport -- sqlSessionDao帮助类（继承spring的DaoSupport）
          ├── transaction -- 事务相关的文件夹
          |    ├── SpringManagedTransaction -- spring管理的事务
          |    └── SpringManagedTransactionFactory -- spring管理的事务的工厂
          ├── MyBatisExceptionTranslator -- 异常处理
          ├── MyBatisSystemException -- mybatis的系统异常
          ├── SqlSessionFactoryBean -- ssqlSessionFactory的工厂bean（负责sqlSessionFactory对象的实例化）
          ├── SqlSessionHolder -- 负责记录当前sqlSession的持有者
          ├── SqlSessionTemplate -- 主要负责代理所有sqlSession的查询
          └── SqlSessionUtils -- sqlSession的工具类（使用sqlSessionFactory创建sqlSession，记录、切换SqlSessionHolder等）
```
