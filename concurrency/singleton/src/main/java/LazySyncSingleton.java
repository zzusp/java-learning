/**
 * @author Aaron.Sun
 * @description 单例-懒汉模式+synchronized（线程安全，不保证实例对象唯一性）
 * @date Created in 10:02 2020/5/13
 * @modified By
 */
public class LazySyncSingleton {

	private static LazySyncSingleton instance = null;

	private LazySyncSingleton() {
	}

	public static LazySyncSingleton getInstance() {
		if (instance == null) {
			synchronized (LazySyncSingleton.class) {
				instance = new LazySyncSingleton();
			}
		}
		return instance;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			new Thread(() -> {
				System.out.println(LazySyncSingleton.getInstance());
			}).start();
		}
	}

}
