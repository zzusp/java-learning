import redis.clients.jedis.Jedis;

public class RedisMessageProducer {
	private Jedis jedis;

	public RedisMessageProducer() {
		this.jedis = new Jedis("localhost", 6379); // 连接到本地 Redis 服务器
	}

	public void produceMessage(String queueName, String message) {
		jedis.rpush(queueName, message); // 将消息推送到队列中
		jedis.rpush(queueName, message + "2"); // 将消息推送到队列中
		jedis.rpush(queueName, message + "3"); // 将消息推送到队列中
		jedis.rpush(queueName, message + "4"); // 将消息推送到队列中
		jedis.rpush(queueName, message + "5"); // 将消息推送到队列中
	}
}