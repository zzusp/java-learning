/**
 * @author Aaron.Sun
 * @description 单例-懒汉模式（线程安全，不保证实例对象唯一性）
 * @date Created in 9:47 2020/5/13
 * @modified By
 */
public class LazySingleton {

	public static LazySingleton instance = null;

	/**
	 * 将构造函数置为私有
	 */
	private LazySingleton() {
	}

	public static LazySingleton getInstance() {
		if (instance == null) {
			instance = new LazySingleton();
		}
		return instance;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			new Thread(() -> {
				System.out.println(LazySingleton.getInstance());
			}).start();
		}
	}

}
