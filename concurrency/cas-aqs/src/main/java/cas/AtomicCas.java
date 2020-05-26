package cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author Aaron.Sun
 * @description 使用AtomicXxxx类进行原子操作（cas）
 * @date Created in 16:57 2020/5/13
 * @modified By
 */
public class AtomicCas {

	private static AtomicInteger atomicI = new AtomicInteger(100);
	private static AtomicStampedReference<Integer> asr = new AtomicStampedReference<>(100, 1);

	public AtomicCas() {
	}

	public static void main(String[] args) throws InterruptedException {
		// ABA问题
		new Thread(() -> {
			System.out.println(atomicI.compareAndSet(100, 110));
		}).start();
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(2L);
			} catch (InterruptedException var1) {
				var1.printStackTrace();
			}

			System.out.println(atomicI.compareAndSet(110, 100));
		}).start();
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(3L);
			} catch (InterruptedException var1) {
				var1.printStackTrace();
			}

			System.out.println(atomicI.compareAndSet(100, 120));
		}).start();

		// 解决ABA（添加版本号，即stamp）
		Thread tf1 = new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(4L);
			} catch (InterruptedException var1) {
				var1.printStackTrace();
			}

			System.out.println(asr.compareAndSet(100, 110, asr.getStamp(), asr.getStamp() + 1));
			System.out.println(asr.compareAndSet(110, 100, asr.getStamp(), asr.getStamp() + 1));
		});
		Thread tf2 = new Thread(() -> {
			int stamp = asr.getStamp();

			try {
				TimeUnit.SECONDS.sleep(5L);
			} catch (InterruptedException var2) {
				var2.printStackTrace();
			}

			System.out.println(asr.compareAndSet(100, 120, stamp, stamp + 1));
		});
		tf1.start();
		tf2.start();
	}

}
