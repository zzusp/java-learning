/**
 * @author Aaron.Sun
 * @description 线程
 * @date Created in 15:39 2020/5/14
 * @modified By
 */
public class Main {

	public static void main(String[] args) throws InterruptedException {
		// 新建状态（New）
		Thread thread = new Thread(() -> {
			// 阻塞状态（Blocked），阻塞两秒
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 运行状态（Running）
			System.out.println(Thread.currentThread().getName() + " is running");
			// 执行结束，死亡（Dead）
		});
		// 就绪状态（Runnable）
		thread.start();
		// 阻塞状态（Blocked），join()等待线程终止或者超时，然后转为就绪状态
		thread.join();
	}

}
