/**
 * @author Aaron.Sun
 * @description 测试类
 * @date Created in 18:36 2020/5/12
 * @modified By
 */
public class Main {

	private static volatile boolean isOver = false;

	public static void main(String[] args) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (!isOver) {
					System.out.println(Thread.currentThread().getName() + " is runing");
				}
			}
		});
		thread.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		isOver = true;
	}

}
