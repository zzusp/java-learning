package cas;

import sun.misc.Unsafe;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author Aaron.Sun
 * @description 原生写法（unsafe类不应该在JDK核心类库之外使用，写法参照ConcurrentHashMap内的用法）
 * @date Created in 16:25 2020/5/13
 * @modified By
 */
public class UnsafeCas implements Serializable {

	private transient volatile int num;

	public void test() {
		num = 0;
		System.out.println("num=" + num);
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				if (U.compareAndSwapInt(this, NUM, 0, num + 1)) {
					System.out.println(Thread.currentThread().getName() + " swap success");
				}
			}).start();
		}
		System.out.println("num=" + num);
	}

	public static void main(String[] args) {
		UnsafeCas main = new UnsafeCas();
		main.test();;
	}


	/**
	 * Unsafe mechanics
	 * sun.misc.Unsafe是JDK内部用的工具类。它通过暴露一些Java意义上说“不安全”的功能给Java层代码，来让JDK能够更多的使用Java
	 * 代码来实现一些原本是平台相关的、需要使用native语言（例如C或C++）才可以实现的功能。该类不应该在JDK核心类库之外使用
	 *
	 * JVM的实现可以自由选择如何实现Java对象的“布局”，也就是在内存里Java对象的各个部分放在哪里，包括对象的实例字段和一些元数据
	 * 之类。sun.misc.Unsafe里关于对象字段访问的方法把对象布局抽象出来，它提供了objectFieldOffset()方法用于获取某个字段相对
	 * Java对象的“起始地址”的偏移量，也提供了getInt、getLong、getObject之类的方法可以使用前面获取的偏移量来访问某个Java对象
	 * 的某个字段。
	 */
	private static final sun.misc.Unsafe U;
	private static final long NUM;

	static {
		try {
			// 使用反射获取Unsafe实例
			Field f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			U = (Unsafe) f.get(null);
			Class<?> k = UnsafeCas.class;
			// 获取Main类中，成员num的偏移量
			NUM = U.objectFieldOffset
					(k.getDeclaredField("num"));
		} catch (Exception e) {
			throw new Error(e);
		}
	}

}
