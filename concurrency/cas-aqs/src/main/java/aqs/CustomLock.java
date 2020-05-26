package aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Aaron.Sun
 * @description 使用AQS实现的一个自定义锁
 * @date Created in 17:37 2020/5/13
 * @modified By
 */
public class CustomLock implements Lock {

	private Helper helper;

	public CustomLock() {
		this.helper = new Helper();
	}

	private class Helper extends AbstractQueuedSynchronizer {

		/**
		 * 获取锁
		 *
		 * @param arg 可作为锁状态（state）修改时的步长
		 * @return 加锁是否成功
		 */
		@Override
		protected boolean tryAcquire(int arg) {
			// 锁状态
			int state = getState();
			// 判断是否已加锁
			if (state == 0) {
				// 加锁（利用CAS原理修改state）
				if (compareAndSetState(0, arg)) {
					// 设置当前线程占有资源
					setExclusiveOwnerThread(Thread.currentThread());
					return true;
				}
			} else if (getExclusiveOwnerThread() == Thread.currentThread()) {
				setState(getState() + arg);
				return true;
			}
			return false;
		}

		/**
		 * 释放锁
		 *
		 * @param arg 可作为锁状态（state）修改时的步长，需与加锁时的值一致
		 * @return 释放锁是否成功
		 */
		@Override
		protected boolean tryRelease(int arg) {
			int state = getState() - arg;
			// 判断释放后是否为0
			if (state == 0) {
				setExclusiveOwnerThread(null);
				setState(state);
				return true;
			}
			setState(state);
			return false;
		}

		/**
		 * 返回一个条件，可用于加锁
		 *
		 * @return 条件
		 */
		public Condition getConditionObjecct() {
			return new ConditionObject();
		}
	}

	/**
	 * 独占模式，加锁
	 * 忽视线程中断，调用tryAcquire尝试获取锁，失败时线程将排队，并可能反复阻塞和解除阻塞，然后调用，直到tryAcquire成功
	 */
	@Override
	public void lock() {
		helper.acquire(1);
	}

	/**
	 * 独占模式，不断尝试加锁
	 * tryAcquire失败时线程将排队，并可能反复阻塞和解除阻塞，然后调用，直到成功或线程被中断为止
	 *
	 * @throws InterruptedException 中断异常
	 */
	@Override
	public void lockInterruptibly() throws InterruptedException {
		helper.acquireInterruptibly(1);
	}

	/**
	 * 独占模式，尝试加锁，成功返回true，失败返回false
	 *
	 * @return 加锁是否成功
	 */
	@Override
	public boolean tryLock() {
		return helper.tryAcquire(1);
	}

	/**
	 * 独占模式，尝试加锁并指定加锁超时时间
	 * tryAcquire失败时线程将排队，并可能反复阻塞和解除阻塞，然后调用，直到成功、达到加锁超时时间或线程被中断为止
	 *
	 * @param time 超时时长
	 * @param unit 时间单位
	 * @return 加锁是否成功
	 * @throws InterruptedException 中断异常
	 */
	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return helper.tryAcquireNanos(1, unit.toNanos(time));
	}

	/**
	 * 释放锁，释放资源
	 */
	@Override
	public void unlock() {
		helper.tryRelease(1);
	}

	@Override
	public Condition newCondition() {
		return helper.getConditionObjecct();
	}

}
