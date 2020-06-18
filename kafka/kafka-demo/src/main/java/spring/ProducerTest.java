package spring;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.kafka.core.KafkaFailureCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import spring.config.ProducerCofig;

import java.util.concurrent.CountDownLatch;

/**
 * @author Aaron.Sun
 * @description 生产者测试
 * @date Created in 22:14 2020/5/26
 * @modified By
 */
public class ProducerTest {
	private static final String TOPIC = "milo2";
	private static final CountDownLatch LATCH = new CountDownLatch(10);

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProducerCofig.class);
		KafkaTemplate<Integer, String> template = (KafkaTemplate<Integer, String>) context.getBean("kafkaTemplate");

		// 消息实体
		ProducerRecord<Integer, String> record;
		for (int i = 0; i < 10; i++) {
			record = new ProducerRecord<>(TOPIC, i, "value" + (int) (10 * (Math.random())));
			// 发送消息
			ListenableFuture<SendResult<Integer, String>> future = template.send(record);
			// 发送回调
			future.addCallback(result -> {
				assert result != null;
				System.out.println(String.format("offset:%s,partition:%s", result.getRecordMetadata().offset(),
						result.getRecordMetadata().partition()));
				LATCH.countDown();
			}, (KafkaFailureCallback<Integer, String>) ex -> {
				System.out.println(ex.getMessage());
				LATCH.countDown();
			});
		}
		try {
			LATCH.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			template.destroy();
		}

	}

}
