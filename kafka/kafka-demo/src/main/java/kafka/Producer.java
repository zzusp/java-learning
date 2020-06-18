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
	private static KafkaProducer<Integer, String> producer = null;

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
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		properties.put(ProducerConfig.RETRIES_CONFIG, 0);
		properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return properties;
	}

	public static void main(String[] args) {
		// 消息实体
		ProducerRecord<Integer, String> record = null;
		for (int i = 0; i < 10; i++) {
			record = new ProducerRecord<>(TOPIC, i, "value" + (int) (10 * (Math.random())));
			// 发送消息
			producer.send(record, (recordMetadata, e) -> {
				if (null != e) {
					System.out.println(e.getMessage());
				} else {
					System.out.println(String.format("offset:%s,partition:%s", recordMetadata.offset(), recordMetadata.partition()));
				}
			});
		}
		producer.close();
	}
}
