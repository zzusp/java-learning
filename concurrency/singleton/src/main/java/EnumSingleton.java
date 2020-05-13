/**
 * @author Aaron.Sun
 * @description 单例-枚举类（线程安全，且保证实例化对象唯一性）
 * @date Created in 10:40 2020/5/13
 * @modified By
 */
public class EnumSingleton {

	/**
	 * 内部枚举类
	 * 1. 加载机制与内部静态类一致
	 * 2. 所有成员被预设为public final，且均为static类型
	 */
	private enum Holder {
		/** 调用方法用的中间变量 */
		INSTANCE;
		private EnumSingleton instance = null;

		Holder() {
			this.instance = new EnumSingleton();
		}

		public EnumSingleton getInstance() {
			return this.instance;
		}
	}

	public static EnumSingleton getInstance() {
		return Holder.INSTANCE.getInstance();
	}

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			new Thread(() -> {
				System.out.println(EnumSingleton.getInstance());
			}).start();
		}
	}

}
