import java.util.concurrent.TimeUnit;

/**
 * @author Aaron.Sun
 * @description 测试类
 * @date Created in 15:46 2020/5/12
 * @modified By
 */
public class Main {

	/**
	 * synchronized修饰静态方法
	 */
	public synchronized static void method1() {
		try {
			TimeUnit.SECONDS.sleep(2);
			System.out.println(Thread.currentThread().getName() + " is runing");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * synchronized修饰成员方法
	 */
	public synchronized void method2() {
		try {
			TimeUnit.SECONDS.sleep(2);
			System.out.println(Thread.currentThread().getName() + " is runing");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * synchronized修饰当前对象，并对当前对象加锁
	 */
	public void method3() {
		synchronized (this) {
			try {
				// 重入
				synchronized (this) {
					TimeUnit.SECONDS.sleep(2);
				}
				System.out.println(Thread.currentThread().getName() + " is runing");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * synchronized修饰当前Class，并对当前Class加锁
	 */
	public synchronized void method4() {
		// ClassLoader  class  ——》堆   Class   所有的对象
		synchronized (Main.class) {
			// 该Class的所有的对象都共同使用这一个锁
			try {
				TimeUnit.SECONDS.sleep(2);
				System.out.println(Thread.currentThread().getName() + " is runing");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		final Main main = new Main();
//		System.out.println("synchronized修饰静态方法测试");
//		for (int i = 0; i < 5; i++) {
//			new Thread(Main::method1).start();
//		}
		System.out.println("synchronized修饰成员方法");
		for (int i = 0; i < 3; i++) {
			new Thread(main::method2).start();
		}
//		System.out.println("synchronized修饰当前对象，并对当前Class加锁");
//		for (int i = 0; i < 5; i++) {
//			new Thread(new Main()::method3).start();
//		}
//		System.out.println("synchronized修饰当前Class，并对当前Class加锁");
//		for (int i = 0; i < 5; i++) {
//			new Thread(new Main()::method4).start();
//		}
	}

}
