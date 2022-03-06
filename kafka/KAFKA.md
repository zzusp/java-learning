# kafka
* [快速启动](./QUICKSTART.md)

## 组件
* 生产者（producer）
    * producer
        * DefaultPartitioner（默认分区器）负责判断消息发往哪个分区
        * RecordAccumulator（累加器）
            * DQueue（双向队列）用来临时存储消息，满足发送条件时会一批一批的取出。每个DQueue对应一个分区
            * DQueue（双向队列）
            * ...
        * Sender线程，从DQueue中取出数据，发送到broker
* Broker
    * __consumer_offsets（内置topic，0.9版本开始出现）
        * 用来存放消费者当前已消费的offset（0.9版本之前存在zookeeper中）
        * 默认50个分区
    * topic
        * partition
        * partition
    * topic
        * partition
    * coordinator（协调器）
        * 选择消费者组中的某一个消费者作为leader
        * 接收消费者leader发送的分区方案，然后分发给组中所有的消费者
        * 组内消费者超时时，移除，然后触发再平衡
* 消费者（consumer）
    * consumer group
       * consumer
           * completeFetches
               * 用来存储从broker抓取到的消息的队列，满足条件后，才返回数据给监听方法
       * consumer
    * consumer group
       * consumer

## 术语
* AR：分区中的所有副本统称为AR（Assigned Replicas）。
* ISR：所有与leader副本保持一定程度同步的副本（包括leader副本在内）称为ISR（In-sync Replicas）。ISR集合是AR的一个子集，
并且可以看作是与leader副本数据比较同步的副本。
* OSR：与leader副本同步滞后过多的副本（不包括leader副本）称为OSR（Out-sync Replicas）。OSR集合也是AR的一个子集。OSR是与ISR互斥的。
* LEO（Log End Offset）：每个副本的最后一个offset，LEO其实就是最新的offset+1
* HW（High Watemark）：所有副本中最小的LEO

## Topic
* topic
    * partition（命名规则，topic+分区号）
        * segment（Partition全局的第一个Segment从0开始，后续每个Segment文件名为文件中第一条消息的offset值。
        数值最大为64位long大小，19位数字字符长度，没有数字用0填充。如00000000000000368769.index和00000000000000368769.log）
            * index file(.index)（稀疏索引，每4kb消息插入一条索引）
                * 相对offset：相对于当前索引文件的偏移量
                * position：message 的物理地址
            * data file(.log)
                * offset：表示Message在这个partition中的偏移量
                * MessageSize：消息内容data的大小
                * data：data为Message的具体内容
            * 时间戳索引(.timeindex)
            * 其他
            * 查看.index/.log/.timeindex文件，可用命令：kafka-run-class.sh kafka.tools.DumpLogSegments --files 文件路径/xxxx.index

## 生产者默认分区策略
* 如果设置了分区，那么就发往设置的分区
* 如果没有设置分区，但是设置了key，那就用key的hashcode对分区数取模，找到对应的分区
* 如果key和分区都没有设置，则选择粘性分区，直到这批数据发送完毕再切换分区

## 提高吞吐量
```text
// ---------------- 生产者配置 ----------------

// 每批消息的最大值，单位字节。默认16384（16KB int），达到最大值时发送该批消息
properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
// 发送消息的时间间隔，单位毫秒。每隔一段时间就发送一批消息，一般不超过100ms
properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
// 压缩类型，选项：'gzip', 'snappy', 'lz4', 'zstd'。默认producer，其意思是保留由生产者设置的原始压缩编解码器
properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "producer");
// 待发送消息总大小限制，单位字节。默认33554432（32MB long），超出限制，则抛出异常
properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432L);

// ---------------- Broker配置 ----------------

增加topic的分区数

// ---------------- 消费者配置 ----------------

增加消费者数量

// 每批次消息返回的最小数据量，默认1，单位byte
properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1);
// 满足最小数据量的最大等待时长，超时未满足则直接返回，默认500，单位毫秒
properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);
// 每批次消息返回的最大数据量，默认57671680（55MB），单位byte。最小不能小于1024（1KB）
properties.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 57671680);
// 每次拉取数据返回消息的最大条数，默认500
properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
// 手动提交offset
properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
```

## 消息精确一次（不丢失且不重复）
```text
// ---------------- 生产者配置 ----------------

// broker的leader的响应配置，all（-1等同all）表示将等待全套同步副本（ISR）确认记录。默认all
properties.put(ProducerConfig.ACKS_CONFIG, "all");
// 开启幂等性。默认true
// 开启幂等性后，发送消息时会包含<ProducerID,Partition,SequenceNumber>这三个信息，根据这三个信息来判断消息是否重复
// ProducerID：在每个新的Producer初始化时，会被分配一个唯一的ProducerID，这个ProducerID对客户端使用者是不可见的。
// （通过org.apache.kafka.clients.producer.internals.Sender#maybeWaitForPid方法生成ProducerID）
// Partition：分区号
// SequenceNumber：对于每个ProducerID，Producer发送数据的每个Topic和Partition都对应一个从0开始单调递增的SequenceNumber值。
properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
// ProducerID重启就会变化，同时不同的Partition也具有不同主键，所以幂等性无法保证跨分区跨会话的Exactly Once。所以需要引入事务。
// 事务ID，需全局唯一
properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "xxxxx");


// ---------------- 消费者配置 ----------------
将消费过程和提交offset过程做原子绑定，所以需要使用事务，且下游消费者必须支持事务
```

## 消息有序
```text
// ---------------- 生产者配置 ----------------

// 在阻塞之前，客户端在单个连接上发送的最大未确认请求数。默认5
// 如果为1则不需要判断是否开启幂等性，即可保证消息有序
// 如果<=5并且开启幂等性，也可保证消息有序；如果>5不保证消息有序。（每个broker最多缓存5个请求）
properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

```

## Follower故障及恢复场景
* Follower发生故障后会被临时踢出ISR（进入OSR）
* 这个期间Leader和正常的Follower继续接收数据
* 待故障Follower恢复后，Follower会读取本地磁盘记录的上次的HW，并将log文件高于HW的部分截取掉，从HW开始向Leader进行同步
* 等到故障Follower的LEO大于等于该Partition的HW，即Follower追上Leader之后，就可以重新加入ISR了

## Leader故障（选举策略）
* Leader发生故障之后，会从AR中，按照AR顺序从第一个副本开始，选出一个存在于ISR中的副本（即存活的）作为新的Leader
* 为保证多个副本之间的数据一致性，其余的Follower会先将各自的log文件高于HW的部分截掉，然后从新的Leader同步数据

注：这只能保证副本之间的数据一致性，并不能保证数据不丢失或不重复

## 高效读写数据
* kafka本身是分布式集群，可以采用分区技术，并行度高
* 读数据采用稀疏索引，可以快速定位要消费的数据
* 顺序写磁盘。写入方式为在文件末端追加，省去大量磁头寻址（机械硬盘）的时间。官方数据表明：同样的磁盘，顺序写能到600M/s，随机写只有100K/s
* 页缓存（PageCache）。linux利用空闲内存，缓存最近使用的硬盘数据，从而减少磁盘I/O操作,提高性能
* 零拷贝技术（完全依赖于操作系统是否支持）
    * kafka集群接收到consumer的请求
    * 调用系统内核拉取消息（从PageCache或磁盘）
    * 系统内核直接返回消息到consumer端（不需要再将数据拷贝到kafka集群，然后由kafka集群返回）
    
## 消费者组再平衡
* 分区分配策略
    * Range策略（org.apache.kafka.clients.consumer.RangeAssignor）
        * 计算`分区数`%`消费者组内的消费者数`，如果余0，则将分区按顺序等分，每个消费者负责一部分；如果除不尽，则顺序靠前的消费者依次多负责一个
        * 如：有8个分区：p0,p1,p2,p3,p4,p5,p6,p7，3个消费者：c0,c1,c2；则结果为：[c0->p0,p1,p2][c1->p3,p4,p5][c2->p6,p7]
        * c0宕机场景：再平衡触发之前，c0负责的分区，全部由c1代为处理，即：[c1->p0,p1,p2,p3,p4,p5][c2->p6,p7]；再平衡后：
        [c1->p0,p1,p2,p3][c2->p4,p5,p6,p7]
        * 分配不均衡场景：如果有3个消费者订阅了10个topic，每个topic都有8个分区，则其中两个消费者会比另一个消费者多消费10个分区
    * 轮询策略（org.apache.kafka.clients.consumer.RoundRobinAssignor）
        * 将所有可用partitions和consumers展开(字典排序)，以轮询的方式将partitions依次分配给consumers
        * 如：有8个分区：p0,p1,p2,p3,p4,p5,p6,p7，3个消费者：c0,c1,c2；则结果为：[c0->p0,p3,p6][c1->p1,p4,p7][c2->p2,p5]
        * c0宕机场景：再平衡触发之前，c0负责的分区，按照排序，轮询分配：[c1->p0,p1,p4,p6,p7][c2->p2,p3,p5]；再平衡后：
        [c1->p0,p2,p4,p6][c2->p1,p3,p5,p7]
        * 分配不均衡场景：3个消费者：c0,c1,c2，3个topic：t0(分区：p0),t1(分区：p0,p1),t2(分区：p0,p1,p2)，c0订阅（t0），
        c1订阅（t0,t1），c2订阅（t0,t1,t2），则结果为：[c0->t0p0][c1->t1p0][c2->t1p1,t2p0,t2p1,t2p2]
    * 粘性策略（org.apache.kafka.clients.consumer.StickyAssignor）
        * 随机分配，尽可能保证分区分配均衡(即分配给consumers的分区数最大相差为1)，再平衡触发时，尽可能保存上次分配结果
        * 如：有8个分区：p0,p1,p2,p3,p4,p5,p6,p7，3个消费者：c0,c1,c2；则结果可能为：[c0->p0,p3,p5][c1->p2,p4,p6][c2->p1,p7]
        * c0宕机场景：再平衡触发之前，c0负责的分区，打散随机分配：[c1->p2,p4,p6,p0][c2->p1,p7,p3,p5]；再平衡后：
        [c1->p2,p4,p6,p3][c2->p1,p7,p0,p5]
    * 合作粘性策略（org.apache.kafka.clients.consumer.CooperativeStickyAssignor）
        * 上述三种分区分配策略均是基于eager协议，Kafka2.4.0开始引入CooperativeStickyAssignor策略。CooperativeStickyAssignor
        与之前的StickyAssignor虽然都是维持原来的分区分配方案，最大的区别是：StickyAssignor仍然是基于eager协议，分区重分配时候，
        都需要consumers先放弃当前持有的分区，重新加入consumer group；而CooperativeStickyAssignor基于cooperative协议，
        该协议将原来的一次全局分区重平衡，改成多次小规模分区重平衡。
    * 自定义策略（org.apache.kafka.clients.consumer.ConsumerPartitionAssignor）
        * 实现ConsumerPartitionAssignor接口，可创建自定义分区策略

