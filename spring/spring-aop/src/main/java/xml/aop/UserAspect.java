package xml.aop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Aaron.Sun
 * @description 用户切面
 * @date Created in 18:13 2020/5/17
 * @modified By
 */
public class UserAspect {

	public void before() {
		System.out.println("来自切面的通知-->before");
	}

	public void after() {
		System.out.println("来自切面的通知-->after");
	}

	public void afterReturning(Object retVal) {
		System.out.println("来自切面的通知-->after-returning " + retVal);
	}

	public void afterThrowing() {
		System.out.println("来自切面的通知-->after-throwing");
	}

	public Object around(ProceedingJoinPoint joinPoint) {
		System.out.println("来自切面的通知-->around-->before");
		Object object;
		try {
			object = joinPoint.proceed();
			System.out.println("来自切面的通知-->around-->after");
			System.out.println("来自切面的通知-->around-->after-returning " + object);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			System.out.println("来自切面的通知-->around-->after");
			System.out.println("来自切面的通知-->around-->after-throwing");
			return null;
		}
		return object;
	}

}
