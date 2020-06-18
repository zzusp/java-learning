## 快速使用
1. 下载地址：http://kafka.apache.org/downloads（注意要下载编译后的代码`Binary downloads`）
2. 如果要运行kafka的生产者、消费者，需要先进行以下步骤：
    1. 启动zookeeper服务
        * （可选项）修改zookeeper配置，配置文件：`kafka的根目录\config\zookeeper.properties`
            * 日志目录：`dataDir=指定路径`
            * 端口：`clientPort=2181`
        * `windows`下，在kafka的根目录运行以下命令：
        ```text
        .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
       ```
    2. 启动kafka服务
        * 修改kafka-server配置，配置文件：`kafka的根目录\config\server.properties`
            * Broker的ID（值为数值类型，集群配置时不可重复）：`broker.id=0`
            * 日志目录（`也是topic和偏移量的目录`）：`log.dirs=指定路径`
            * kafka-server监听地址（windows下有可能受hosts文件影响，所以建议修改）：`listeners=PLAINTEXT://localhost:9092`
            * zookeeper连接（与上边zookeeper端口保持一致，地址为zookeeper服务器地址，配置集群时多个地址逗号分割）：`zookeeper.connect=localhost:2181`
        * `windows`下，在kafka的根目录运行以下命令：
        ```text
        .\bin\windows\kafka-server-start.bat .\config\server.properties
       ```
* 注意：
    * JDK版本需要1.8及以上
    * JAVA_HOME的路径不要有空格
    * kafka存放路径不要有空格
    * 生产者、消费者的`topic`一致
    * 生产者、消费者的`bootstrap.servers`与`server.properties`文件中的`listeners`的值的ip和端口保持一致；如果`advertised.listeners`有值，则与其保持一致

## 常用脚本及命令
所有命令默认在kafka根目录执行，windows环境下，需要使用.\bin\windows目录下的bat脚本，注意斜杠
### kafka提供的zookeeper启动脚本：zookeeper-server-start.sh
```text
# 启动命令
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```
### Broker启动脚本：kafka-server-start.sh
```text
# 启动命令
./bin/kafka-server-start.sh ./config/server.properties
```
### Topic脚本：kafka-topics.sh
```text
以下命令中的 --zookeeper [zookeeper server:port] 可以改为 --bootstrap-server [broker server:port]

# 查看指定服务器所有的topic
./bin/kafka-topics.sh --list --zookeeper localhost:2181
# 创建一个名为first_msg的topic，zookeeper服务器为localhost，分区数为2，副本数（含原文件）为2
./bin/kafka-topics.sh --create --zookeeper localhost:2181 --partitions 2 --replication-factor 2 --topic [topic name]
# 查看topic是否存在
./bin/kafka-topics.sh --list --zookeeper localhost:2181|grep [topic name]
# 删除指定topic
./bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic [topic name]
# 查看topic的详细信息
./bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic [topic name]
# 以下为详细信息内容（Partition的值为分区号，Leader、Replicas、Isr的值都是broker服务器的ID）
Topic: [topic name]    PartitionCount: 2       ReplicationFactor: 2    Configs:
        Topic: [topic name]    Partition: 0    Leader: 0       Replicas: 0,2     Isr: 0,2 
        Topic: [topic name]    Partition: 1    Leader: 1       Replicas: 1,0     Isr: 1,0
```
### Producer脚本：kafka-console-producer.sh
```text
# 生产一条消息
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic [topic name]
>此处输入消息内容，回车发送
```
### Consumer脚本：kafka-console-consumer.sh
```text
--zookeeper [zookeeper server:port] 已过时，改为 --bootstrap-server [broker server:port]

# 接收消息（只能接收到consumer启动后的消息）
./bin/kafka-console-producer.sh --bootstrap-server localhost:2181 --topic [topic name]
# 接收topic中的所有消息
./bin/kafka-console-producer.sh --bootstrap-server localhost:2181 --topic [topic name] --from-beginning
```
## 完全删除一个Topic
* 删除前先确认配置文件`./config/server.properties`中的配置
```text
# 以下配置如果没有则手动新增

# 该项必须为true
delete.topic.enable=true
# 这一项如果为false，就可以直接进行下面的操作
# 如果为true，则必须将生产者停了，再进行下面的操作
auto.create.topics.enable = false
```
* 通过`kafka-topics.sh`脚本删除`topic`
```text
以下命令中的 --zookeeper [zookeeper server:port] 可以改为 --bootstrap-server [broker server:port]

# 查看topic是否存在
./bin/kafka-topics.sh --list --zookeeper [zookeeper server:port]|grep [topic name]
# 删除topic
./bin/kafka-topics.sh --delete --zookeeper [zookeeper server:port] --topic [topic name]
# 再次查看topic是否存在
./bin/kafka-topics.sh --list --zookeeper [zookeeper server:port]|grep [topic name]
此时会看到
Topic [topic name] is marked for deletion.
```
* 删除zookeeper服务器中的`topic`
```text
注：kafka_2.12-2.5.0版本自带的zookeeper中的rmr命令已过时，替代命令为deleteall

# 使用kafka提供的zookeeper-shell.sh脚本连接对应的zookeeper服务器
./bin/zookeeper-shell.sh [zookeeper server:port]
# 以下为zookeeper命令
# 列出/broker/topics目录下所有topic
ls /brokers/topics
# 删除topic
deleteall /brokers/topics/[topic name]
# 列出/config/topics目录下所有topic
ls /config/topics
# 删除topic
deleteall /config/topics/[topic name]

# 另外所有被标记为marked for deletion的topic都可以通过以下命令找到
ls /admin/delete_topics/[topic name]
# 手动删除kafka存储目录（server.properties文件log.dirs配置，默认为"/tmp/kafka-logs"） 相关topic目录

# 得到kafka的leader机器
get /controller
# 重启kakfa即可
# ps：如果不重启kafka，就重建一模一样的topic，则不会再在log.dirs配置的目录下创建新的topic的物理文件以及目录
```
