import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Aaron.Sun
 * @description 线程池
 * @date Created in 16:22 2020/5/14
 * @modified By
 */
public class Main {

	static class CustomThreadFactory implements ThreadFactory {
		private final AtomicInteger threadNumber = new AtomicInteger(1);

		public CustomThreadFactory() {
		}

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r,
					"custom-thread" + threadNumber.getAndIncrement());
		}
	}

	/**
	 * 测试线程池，无返回结果
	 */
	public void testThreadPool() {
		// 线程池中线程的名称
		ThreadFactory namedThreadFactory = new CustomThreadFactory();
		// 创建一个固定大小的线程池
		ExecutorService threadPool = new ThreadPoolExecutor(13, 13, 0L,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), namedThreadFactory);
		for (int i = 0; i < 13; i++) {
			threadPool.execute(() -> {
				System.out.println(Thread.currentThread().getName() + " is running");
			});
		}
		threadPool.shutdown();
	}

	/**
	 * 测试线程池，有返回结果
	 */
	public void testThreadPoolWithFuture() {
		// 线程池中线程的名称
		ThreadFactory namedThreadFactory = new CustomThreadFactory();
		// 创建一个固定大小的线程池
		ExecutorService threadPool = new ThreadPoolExecutor(13, 13, 0L,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), namedThreadFactory);
		// 接收线程返回的结果
		List<Future<String>> futures = new ArrayList<>();
		for (int i = 0; i < 13; i++) {
			futures.add(threadPool.submit(() -> Thread.currentThread().getName() + " is running"));
		}
		// 遍历线程返回结果
		for (Future<String> future : futures) {
			try {
				System.out.println(future.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		threadPool.shutdown();
	}

	/**
	 * 测试线程池（使用CompletionService），可获取到线程执行结果
	 */
	public void testThreadPoolWithCompletionService() {
		// 线程池中线程的名称
		ThreadFactory namedThreadFactory = new CustomThreadFactory();
		// 创建一个固定大小的线程池
		ExecutorService threadPool = new ThreadPoolExecutor(13, 13, 0L,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), namedThreadFactory);
		// 开启一个线程完成服务对象
		CompletionService<String> completionService = new ExecutorCompletionService<>(threadPool);
		// 接收线程返回的结果
		List<Future<String>> futures = new ArrayList<>();
		for (int i = 0; i < 13; i++) {
			Future<String> future = completionService.submit(() -> {
				try {
					Thread.sleep((long) (Math.random() * 10) * 1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return Thread.currentThread().getName() + " is running";
			});
			futures.add(future);
		}
		threadPool.shutdown();
		// 遍历线程返回结果（按照任务执行速度）
		for (int i = 0; i < 13; i++) {
			try {
				System.out.println(completionService.take().get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		// 遍历线程返回结果（按照任务提交顺序）
//		for (Future<String> future : futures) {
//			try {
//				System.out.println(future.get());
//			} catch (InterruptedException | ExecutionException e) {
//				e.printStackTrace();
//			}
//		}
		threadPool.shutdown();
	}

	/**
	 * 测试线程池（使用CompletableFuture），可获取到线程执行结果
	 */
	public void testThreadPoolWithCompletableFuture() {
		// 线程池中线程的名称
		ThreadFactory namedThreadFactory = new CustomThreadFactory();
		// 创建一个固定大小的线程池
		ExecutorService threadPool = new ThreadPoolExecutor(13, 13, 0L,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), namedThreadFactory);
		// 接收线程返回的结果
		List<CompletableFuture<String>> futures = new ArrayList<>();
		for (int i = 0; i < 13; i++) {
			futures.add(CompletableFuture.supplyAsync(() -> {
				try {
					Thread.sleep((long) (Math.random() * 10) * 1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return Thread.currentThread().getName() + " is running";
			}, threadPool));
		}
		// 遍历线程返回结果
		futures.forEach((future) -> {
			try {
				System.out.println(future.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
		threadPool.shutdown();
	}

	public static void main(String[] args) {
		Main main = new Main();
//		main.testThreadPool();
//		main.testThreadPoolWithFuture();
		main.testThreadPoolWithCompletionService();
//		main.testThreadPoolWithCompletableFuture();
	}

}
