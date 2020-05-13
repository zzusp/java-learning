/**
 * @author Aaron.Sun
 * @description 单例-DCL（Double Check Locking 线程安全，且保证实例化对象唯一性）
 * @date Created in 10:10 2020/5/13
 * @modified By
 */
public class DoubleCheckLockSingleton {
	/** 添加volatile关键字，避免指令重排造成空指针异常 */
	public static volatile DoubleCheckLockSingleton instance = null;

	/**
	 * 将构造函数置为私有
	 */
	private DoubleCheckLockSingleton() {
	}

	public static DoubleCheckLockSingleton getInstance() {
		if (instance == null) {
			synchronized (DoubleCheckLockSingleton.class) {
				if (instance == null) {
					instance = new DoubleCheckLockSingleton();
				}
			}
		}
		return instance;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			new Thread(() -> {
				System.out.println(DoubleCheckLockSingleton.getInstance());
			}).start();
		}
	}

}
