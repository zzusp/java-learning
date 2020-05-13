/**
 * @author Aaron.Sun
 * @description 单例-holder模式（静态内部类模式 线程安全，且保证实例化对象唯一性）
 * @date Created in 10:15 2020/5/13
 * @modified By
 */
public class HolderSingleton {

	private HolderSingleton() {
	}

	static {
		System.out.println("init HolderSingleton");
	}

	/**
	 * 利用静态内部类使用时才会加载的特性，实现懒加载机制
	 * 目前holder模式是使用最广泛的一种单例模式
	 */
	private static class Holder {
		private static HolderSingleton instance = new HolderSingleton();

		static {
			System.out.println("init Holder");
		}
	}

	public static HolderSingleton getInstance() {
		return Holder.instance;
	}


	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			new Thread(() -> {
				System.out.println(HolderSingleton.getInstance());
			}).start();
		}
	}

}
