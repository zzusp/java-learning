import redis.clients.jedis.Jedis;
import java.util.List;

public class RedisMessageConsumer {
	private Jedis jedis;

	public RedisMessageConsumer() {
		this.jedis = new Jedis("localhost", 6379); // 连接到本地 Redis 服务器
	}

	public void consumeMessage(String queueName) {
		List<String> result;
		while (true) {
			result = jedis.blpop(0, queueName); // 从队列中获取消息
			System.out.println(1111);
			System.out.println("Received message: " + result.get(1));
		}
//		return result.get(1);
	}
}