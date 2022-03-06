package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author Aaron.Sun
 * @description kafka生产者
 * @date Created in 14:47 2020/5/26
 * @modified By
 */
public class Producer {
    private static final String TOPIC = "milo2";
    private static final String BROKER_LIST = "127.0.0.1:9092";
    private static KafkaProducer<Integer, String> producer;

    /*
    初始化生产者
     */
    static {
        Properties configs = initConfig();
        producer = new KafkaProducer<>(configs);
    }

    /*
    初始化配置
     */
    private static Properties initConfig() {
        Properties properties = new Properties();
        // broker地址配置
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        // 重试次数。默认0
        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
        // 键的序列化类配置
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        // 值的序列化类配置
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // ---------------- 吞吐量提高 ----------------

        // 每批消息的最大值，单位字节。默认16384（16KB int），达到最大值时发送该批消息
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 发送消息的时间间隔，单位毫秒。每隔一段时间就发送一批消息，一般不超过100ms
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        // 压缩类型，选项：'gzip', 'snappy', 'lz4', 'zstd'。默认producer，其意思是保留由生产者设置的原始压缩编解码器
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "producer");
        // 待发送消息总大小限制，单位字节。默认33554432（32MB long），超出限制，则抛出异常
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432L);

        // ---------------- 保证消息不丢失也不重复 ----------------

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

        // ---------------- 保证消息有序 ----------------

        // 在阻塞之前，客户端在单个连接上发送的最大未确认请求数。默认5
        // 如果为1则不需要判断是否开启幂等性，即可保证消息有序
        // 如果<=5并且开启幂等性，也可保证消息有序；如果>5不保证消息有序。（每个broker最多缓存5个请求）
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

        return properties;
    }

    public static void main(String[] args) {
        producer.initTransactions();

        producer.beginTransaction();

        try {
            // 消息实体
            ProducerRecord<Integer, String> record;
            for (int i = 0; i < 10; i++) {
                record = new ProducerRecord<>(TOPIC, i, "value" + (int) (10 * (Math.random())));
                // 同步发送（阻塞，直到消息发送完成）：send(xxx).get();send(xxx,callback).get();
                // 异步发送：send(xxx);send(xxx,callback);
                // 发送消息
                producer.send(record, (recordMetadata, e) -> {
                    if (null != e) {
                        System.out.println(e.getMessage());
                    } else {
                        System.out.println(String.format("offset:%s,partition:%s", recordMetadata.offset(), recordMetadata.partition()));
                    }
                });
            }
            producer.commitTransaction();
        } catch (Exception e) {
            producer.abortTransaction();
        } finally {
            producer.close();
        }
    }
}
