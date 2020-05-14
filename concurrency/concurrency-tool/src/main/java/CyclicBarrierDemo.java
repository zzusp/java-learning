import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @author Aaron.Sun
 * @description CyclicBarrier示例类
 * @date Created in 9:42 2020/5/14
 * @modified By
 */
public class CyclicBarrierDemo {

	private static List<String> players = Arrays.asList("1号玩家", "2号玩家", "3号玩家", "4号玩家", "5号玩家");
	private static Integer num = 0;

	public static void main(String[] args) {
		Thread[] threads = new Thread[players.size()];
		CyclicBarrier cyclicBarrier = new CyclicBarrier(players.size());


		System.out.println("队伍人数达到5人即可开始游戏");
		System.out.println("==============================");
		for (int i = 0; i < threads.length; i++) {
			String player = players.get(i);
			threads[i] = new Thread(() -> {
				System.out.printf("%s上线\n", player);
				try {
					TimeUnit.SECONDS.sleep(new Random().nextInt(5));
					// ++操作不具有原子性
					synchronized (num) {
						++num;
					}
					System.out.printf("%s进入队伍，当前队伍人数（%d/5）\n", player, num);
					cyclicBarrier.await();
					TimeUnit.SECONDS.sleep(new Random().nextInt(5));
					System.out.printf("%s进入游戏\n", player);
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
			});
			threads[i].start();
		}
	}

}
