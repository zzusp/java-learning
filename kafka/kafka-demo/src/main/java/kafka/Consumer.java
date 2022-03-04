package kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * @author Aaron.Sun
 * @description kafka消费者
 * @date Created in 14:51 2020/5/26
 * @modified By
 */
public class Consumer {

    private static final String TOPIC = "milo2";
    private static final String BROKER_LIST = "127.0.0.1:9092";
    private static KafkaConsumer<Integer, String> consumer;

    static {
        Properties configs = initConfig();
        consumer = new KafkaConsumer<>(configs);
        // 订阅主题
        consumer.subscribe(Collections.singleton(TOPIC));
        // 订阅主题指定分区
        //List<TopicPartition> partitions = new ArrayList<>();
        //partitions.add(new TopicPartition(TOPIC, 1));
        //consumer.assign(partitions);
    }

    private static Properties initConfig() {
        Properties properties = new Properties();
        // broker地址配置
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        // 键的反序列化类配置
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        // 值的反序列化类配置
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // ---------------- 消费者拉取策略 ----------------

        // 每批次消息返回的最小数据量，默认1，单位byte
        properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1);
        // 满足最小数据量的最大等待时长，超时未满足则直接返回，默认500，单位毫秒
        properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);
        // 每批次消息返回的最大数据量，默认57671680（55MB），单位byte。最小不能小于1024（1KB）
        properties.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 57671680);
        // 每次拉取数据返回消息的最大条数，默认500
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        // 设置消费起始offset位置，默认latest
        // earliest：自动将偏移量重置为最早的偏移量，--from-beginning
        // latest：自动将偏移量重置为当前最新的偏移量
        // none：如果为找到消费者组的先前偏移量，则抛出异常
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        // ---------------- 消费者组重新平衡 ----------------

        // 分区分配策略（平衡策略），默认值[RangeAssignor, CooperativeStickyAssignor]
        // Range策略：org.apache.kafka.clients.consumer.RangeAssignor
        // 轮询策略：org.apache.kafka.clients.consumer.RoundRobinAssignor
        // 粘性策略：org.apache.kafka.clients.consumer.StickyAssignor
        // 合作粘性策略：org.apache.kafka.clients.consumer.CooperativeStickyAssignor
        // 自定义策略：org.apache.kafka.clients.consumer.ConsumerPartitionAssignor的自定义实现类
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.StickyAssignor");
        // 消费者组id，相同id的消费者为一组，共同消费。必须设置该项，不管是否使用消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        // 消费者会话超时时间，一旦超时就会促进消费者组的重新平衡，默认45000（45秒），单位毫秒
        // 该值大小需在 group.min.session.timeout.ms 和 group.max.session.timeout.ms 之间
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 45000);
        // 消费者与coordinator之间的心跳检测间隔时间，一旦超时就会促进消费者组的重新平衡，默认3000（3秒），单位毫秒
        // 该值大小需小于 session.timeout.ms，通常应小于 session.timeout.ms 的1/3
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 3000);
        // 消费者拉取消息的最大间隔配置，默认300000（5分钟），单位毫秒
        // 如果超过设置的时间，并不会直接重新平衡消费者组，而是停止发送心跳包，从而促进消费者组的重新平衡
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);

        // ---------------- 消费者提交offset策略 ----------------

        // 后台自动提交offset配置，默认true。生产环境建议设置为false，手动提交，因为自动提交会有延迟
        // 注：在控制台使用时，如果没有设置group.id时，该参数默认会被设置为false。官方给出的解释是为了避免污染consumer coordinator缓存，
        // 因为自动生成的组不太可能被其他消费者组使用
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // 自动提交offset的时间间隔，默认5000（5秒），单位毫秒
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000);

        return properties;
    }

    public static void main(String[] args) {
        /**
        // 指定offset进行消费
        // 获取消费的分区方案
        Set<TopicPartition> assignment = consumer.assignment();
        // 判断分区方案是否为空
        while (assignment.isEmpty()) {
            // 如果为空，则尝试拉取数据，以获取分区方案
            consumer.poll(Duration.ofMillis(10));
            // 重新获取分区方案
            assignment = consumer.assignment();
        }
        // 指定消费的offset
        for (TopicPartition partition : assignment) {
            consumer.seek(partition, 100);
        }
        */

        /**
        // 指定时间进行消费，获取1天前的数据
        // 获取消费的分区方案
        Set<TopicPartition> assignment = consumer.assignment();
        // 判断分区方案是否为空
        while (assignment.isEmpty()) {
            // 如果为空，则尝试拉取数据，以获取分区方案
            consumer.poll(Duration.ofMillis(10));
            // 重新获取分区方案
            assignment = consumer.assignment();
        }
        HashMap<TopicPartition, Long> map = new HashMap<>(16);
        for (TopicPartition partition : assignment) {
            map.put(partition, System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        }
        // 将时间转为offset
        Map<TopicPartition, OffsetAndTimestamp> topicPartitionOffsetAndTimestampMap = consumer.offsetsForTimes(map);
        // 指定消费的offset
        for (TopicPartition partition : assignment) {
            consumer.seek(partition, topicPartitionOffsetAndTimestampMap.get(partition).offset());
        }
        */

        while (true) {
            ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofMillis(10));
            for (ConsumerRecord<Integer, String> record : records) {
                System.out.println(record);
            }
            // 手动提交offset（同步）
            //consumer.commitSync();
            // 手动提交offset（异步）
            //consumer.commitAsync();
        }
    }

}
