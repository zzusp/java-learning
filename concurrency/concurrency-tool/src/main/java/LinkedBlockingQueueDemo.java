import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Aaron.Sun
 * @description ArrayBlockingQueue实现生产者、消费者模式
 * @date Created in 11:03 2020/5/14
 * @modified By
 */
public class LinkedBlockingQueueDemo {
	private final static int MESSAGE_SUM = 20;
	private final static long PRODUCER_SPEED = 100;
	private final static long CONSUMER_SPEED = 500;
	private static LinkedBlockingQueue<String> messageList = new LinkedBlockingQueue<>(10);

	static class Producer implements Runnable {

		@Override
		public void run() {
			int i = 0;
			while (i < MESSAGE_SUM) {
				try {
					messageList.put(String.format("第%d条消息", i));
					Thread.sleep(PRODUCER_SPEED);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
			}
		}
	}

	static class Consumer implements Runnable {

		@Override
		public void run() {
			int i = 0;
			while (i < MESSAGE_SUM) {
				try {
					System.out.println(messageList.take() + "被消费<--");
					System.out.printf("剩余未消费消息数量：%d\n", messageList.size());
					Thread.sleep(CONSUMER_SPEED);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
			}
		}
	}

	public static void main(String[] args) {
		Thread producer = new Thread(new Producer());
		Thread consumer = new Thread(new Consumer());
		producer.start();
		consumer.start();
	}

}
