package aqs;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 18:56 2020/5/13
 * @modified By
 */
public class Main {

	private CustomLock lock = new CustomLock();
	private volatile int n = 0;
	private volatile int m = 0;

	public void lock() {
		lock.lock();
		n++;
		reentrantLock();
		lock.unlock();
	}

	/**
	 * 重入锁
	 */
	public void reentrantLock() {
		lock.lock();
		m++;
		lock.unlock();
	}

	public void log() {
		System.out.println("n=" + n + "m=" + m);
	}

	public static void main(String[] args) {
		Main main = new Main();
		Thread[] th = new Thread[20];
		for (int i = 0; i < 20; i++) {
			th[i] = new Thread(() -> {
				main.lock();
				main.log();
			});
			th[i].start();
		}
	}

}
