package kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

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
		consumer.subscribe(Collections.singleton(TOPIC));
	}

	private static Properties initConfig() {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", BROKER_LIST);
		properties.put("group.id", "test");
		properties.put("key.deserializer", IntegerDeserializer.class.getName());
		properties.put("value.deserializer", StringDeserializer.class.getName());
		properties.setProperty("enable.auto.commit", "true");
		properties.setProperty("auto.offset.reset", "earliest");
		return properties;
	}

	public static void main(String[] args) {
		while (true) {
			ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofMillis(10));
			for (ConsumerRecord<Integer, String> record : records) {
				System.out.println(record);
			}
		}
	}

}
