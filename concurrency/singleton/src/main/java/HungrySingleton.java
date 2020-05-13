/**
 * @author Aaron.Sun
 * @description 单例-饿汉模式（线程安全，且保证实例化对象唯一性）
 * @date Created in 9:43 2020/5/13
 * @modified By
 */
public class HungrySingleton {

	private static HungrySingleton instance = new HungrySingleton();

	/**
	 * 将构造函数置为私有
	 */
	private HungrySingleton() {
	}

	public static HungrySingleton getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			new Thread(() -> {
				System.out.println(HungrySingleton.getInstance());
			}).start();
		}
	}

}
