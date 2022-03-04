# kafka
* [快速启动](./QUICKSTART.md)

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

## Follower故障及恢复场景
* Follower发生故障后会被临时踢出ISR（进入OSR）
* 这个期间Leader和正常的Follower继续接收数据
* 待故障Follower恢复后，Follower会读取本地磁盘记录的上次的HW，并将log文件高于HW的部分截取掉，从HW开始向Leader进行同步
* 等到故障Follower的LEO大于等于该Partition的HW，即Follower追上Leader之后，就可以重新加入ISR了

## Leader故障
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
    * Range策略
        * 计算`分区数`%`消费者组内的消费者数`，如果余0，则将分区按顺序等分，每个消费者负责一部分；如果除不尽，则顺序靠前的消费者依次多负责一个
        * 如：有8个分区：p0,p1,p2,p3,p4,p5,p6,p7，3个消费者：c0,c1,c2；则结果为：[c0->p0,p1,p2][c1->p3,p4,p5][c2->p6,p7]
        * c0宕机场景：再平衡触发之前，c0负责的分区，全部由c1代为处理，即：[c1->p0,p1,p2,p3,p4,p5][c2->p6,p7]；再平衡后：
        [c1->p0,p1,p2,p3][c2->p4,p5,p6,p7]
        * 分配不均衡场景：如果有3个消费者订阅了10个topic，每个topic都有8个分区，则其中两个消费者会比另一个消费者多消费10个分区
    * 轮询策略
        * 将所有可用partitions和consumers展开(字典排序)，以轮询的方式将partitions依次分配给consumers
        * 如：有8个分区：p0,p1,p2,p3,p4,p5,p6,p7，3个消费者：c0,c1,c2；则结果为：[c0->p0,p3,p6][c1->p1,p4,p7][c2->p2,p5]
        * c0宕机场景：再平衡触发之前，c0负责的分区，按照排序，轮询分配：[c1->p0,p1,p4,p6,p7][c2->p2,p3,p5]；再平衡后：
        [c1->p0,p2,p4,p6][c2->p1,p3,p5,p7]
        * 分配不均衡场景：3个消费者：c0,c1,c2，3个topic：t0(分区：p0),t1(分区：p0,p1),t2(分区：p0,p1,p2)，c0订阅（t0），
        c1订阅（t0,t1），c2订阅（t0,t1,t2），则结果为：[c0->t0p0][c1->t1p0][c2->t1p1,t2p0,t2p1,t2p2]
    * 粘性策略
        * 随机分配，尽可能保证分区分配均衡(即分配给consumers的分区数最大相差为1)，再平衡触发时，尽可能保存上次分配结果
        * 如：有8个分区：p0,p1,p2,p3,p4,p5,p6,p7，3个消费者：c0,c1,c2；则结果可能为：[c0->p0,p3,p5][c1->p2,p4,p6][c2->p1,p7]
        * c0宕机场景：再平衡触发之前，c0负责的分区，打散随机分配：[c1->p2,p4,p6,p0][c2->p1,p7,p3,p5]；再平衡后：
        [c1->p2,p4,p6,p3][c2->p1,p7,p0,p5]
    * 合作粘性策略
    * 自定义策略

