```
self-mybatis（简化版mybatis，主要用于学习mybatis的加载机制和执行原理）
└── java
     └── org.self.mybatis
          ├── annotations -- 注解相关的文件夹
          |    └── SelfSelect -- mapper接口中，方法上配置sql语句时，使用的注解
          ├── binding -- 绑定相关的文件夹
          |    ├── MapperMethod -- mapper中的方法调用（JDK动态代理调用）及处理
          |    ├── MapperProxy -- mapper接口动态代理配置（代理后，执行时调用MapperMethod）
          |    ├── MapperProxyFactory -- 为mapper接口动态代理的工厂（注册mapper时调用）
          |    └── MapperRegistry -- mapper注册器
          ├── builder -- builder相关的文件夹
          |    └── annotation -- 注解配置的builder的文件夹
          |         ├── MapperAnnotationBuilder -- 注解配置sql的mapper的builder
          |         └── MethodResolver -- mapper接口中的方法解析
          ├── exceptions -- 异常相关的文件夹
          |    └── PersistenceException -- 持久化异常
          ├── executor -- 执行器相关的文件夹
          |    ├── parameter -- 查询参数相关的文件夹
          |    |    ├── DefaultParameterHandler -- 默认参数处理器
          |    |    └── ParameterHandler -- 参数处理器接口
          |    ├── resultset -- 查询结果集相关的文件夹
          |    |    ├── DefaultResultSetHandler -- 默认结果集处理器
          |    |    └── ResultSetHandler -- 结果集处理器接口
          |    ├── statement -- 查询statement相关的文件夹
          |    |    ├── SelfStatementHandler -- 自定义statement处理器，用来创建statement（mybatis中有多个，此处为自定义的）
          |    |    └── StatementHandler -- statement处理器接口
          |    ├── Executor -- 执行器接口
          |    └── SelfExecutor -- 自定义执行器，用来执行数据库或缓存的查询（mybatis中有多个，此处为自定义的）
          ├── mapping -- 映射相关的文件夹
          |    ├── BoundSql -- 存储sql和查询参数的对象（类中属性被简化过）
          |    ├── Environment -- 环境配置（主要存储数据源和事务工厂对象）
          |    ├── MappedStatement -- 用来存储生成statement对象时，需要的信息
          |    └── SqlSource -- 获取（生成）BoundSql的接口
          ├── session -- 数据库session相关的文件夹
          |    └── defaults -- 默认的session相关的文件夹
          |         ├── DefaultSqlSession -- 默认的sqlSession
          |         ├── DefaultSqlSessionFactory -- 默认的sqlSession工厂
          |         └── SqlSessionFactory -- sqlSession工厂的接口，用来创建sqlSession
          └── transaction -- 事务相关的文件夹
               ├── managed -- 主事务（默认事务）相关的文件夹
               |    ├── ManagedTransaction -- 主事务（默认事务，未指定事务时使用）
               |    └── ManagedTransactionFactory -- 主事务（默认事务）工厂
               ├── JdbcTransaction -- Jdbc事务
               ├── JdbcTransactionFactory -- Jdbc事务工程
               ├── Transaction -- 事务接口（创建数据库连接，提交事务，事务回滚等方法）
               ├── TransactionException -- 事务一场
               └── TransactionFactory -- 事务工厂接口（创建事务）
```
