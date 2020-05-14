import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Aaron.Sun
 * @description ConcurrentLinkedDeque示例类
 * @date Created in 14:21 2020/5/14
 * @modified By
 */
public class ConcurrentLinkedDequeDemo {

	public static void main(String[] args) throws InterruptedException {
		ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<>();
		// 添加数据
		Thread[] add = new Thread[100];
		for (int i = 0; i < 100; i++) {
			add[i] = new Thread(() -> {
				for (int j = 0; j < 10000; j++) {
					// 并发过程中list的size大小仅供参考，并不一定准确
					list.add(Thread.currentThread().getName() + ":Element " + j);
				}
			});
			add[i].start();
			add[i].join();
		}
		System.out.println("after add size:" + list.size());

		// 移除数据
		Thread[] poll = new Thread[100];
		for (int i = 0; i < 100; i++) {
			poll[i] = new Thread(() -> {
				for (int j = 0; j < 5000; j++) {
					// 并发过程中list的size大小仅供参考，并不一定准确
					list.pollFirst();
					list.pollLast();
				}
			});
			poll[i].start();
			poll[i].join();
		}
		System.out.println("after poll size:" + list.size());
	}

}
