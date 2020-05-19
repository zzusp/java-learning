/**
 * @author Aaron.Sun
 * @description 线程
 * @date Created in 15:39 2020/5/14
 * @modified By
 */
public class Main {

	public static void main(String[] args) throws InterruptedException {
		// 初始状态（NEW）
		Thread thread = new Thread(() -> {
			// 超时等待状态（TIME_WAITING），等待两秒
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 运行状态（RUNNING）
			System.out.println(Thread.currentThread().getName() + " is running");
			// 执行结束，死亡（TERMINATED）
		});
		// 运行状态（RUNNABLE）
		thread.start();
		// 等待状态（WAITING），join()等待线程终止或者超时，然后转为就绪状态
		thread.join();
	}

}
