import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Aaron.Sun
 * @description Semaphore示例类
 * @date Created in 10:26 2020/5/14
 * @modified By
 */
public class SemaphoreDemo {

	private final static int CAR_NUM = 20;
	private final static int PARKING_PLACT_NUM = 5;

	public static void main(String[] args) {
		Thread[] threads = new Thread[CAR_NUM];
		Semaphore semaphore = new Semaphore(PARKING_PLACT_NUM);

		for (int i = 0; i < threads.length; i++) {
			int num = i;
			threads[i] = new Thread(() -> {
				try {
					semaphore.acquire(1);
					System.out.printf("%d号车辆进入停车场-->\n", num);
					TimeUnit.SECONDS.sleep(new Random().nextInt(5));
					System.out.printf("%d号车辆离开停车场<--\n", num);
					semaphore.release(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			threads[i].start();
		}
	}

}
