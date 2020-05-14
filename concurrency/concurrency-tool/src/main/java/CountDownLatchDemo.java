import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Aaron.Sun
 * @description CountDownLatch示例类
 * @date Created in 8:48 2020/5/14
 * @modified By
 */
public class CountDownLatchDemo {

	private static List<String> players = Arrays.asList("1号选手", "2号选手", "3号选手", "4号选手", "5号选手");

	public static void main(String[] args) throws InterruptedException {
		Thread[] threads = new Thread[players.size()];
		CountDownLatch latch = new CountDownLatch(1);

		System.out.println("10秒后比赛开始");
		for (int i = 0; i < threads.length; i++) {
			String player = players.get(i);
			threads[i] = new Thread(() -> {
				System.out.printf("%s开始准备\n", player);
				try {
					TimeUnit.SECONDS.sleep(new Random().nextInt(10));
					System.out.printf("%s准备完毕\n", player);
					latch.await();
					System.out.printf("%s起跑\n", player);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
				}
			});
			threads[i].start();
		}

		TimeUnit.SECONDS.sleep(10);
		System.out.println("==============================");
		System.out.println("比赛开始");
		latch.countDown();
	}

}
