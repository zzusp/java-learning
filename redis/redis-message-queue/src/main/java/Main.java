import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
//		RedisMessageProducer producer = new RedisMessageProducer();
//		producer.produceMessage("myQueue", "Hello, Redis!");
//
//		RedisMessageConsumer consumer = new RedisMessageConsumer();
//		consumer.consumeMessage("myQueue");

		List<String> workerIds = new ArrayList<>();
		for (int i = 0; i < 111; i++) {
			workerIds.add("u" + i);
		}
		workerIds.add("u100");
		int batchSize = 13;

		int num = workerIds.size() % batchSize > 0 ? workerIds.size() / batchSize + 1 : workerIds.size() / batchSize;
		System.out.println(num);

		for (int i = 0; i < workerIds.size(); i += batchSize) {
			System.out.println(String.join(",", workerIds.subList(i, Math.min(i + batchSize, workerIds.size()))));
		}
	}
}