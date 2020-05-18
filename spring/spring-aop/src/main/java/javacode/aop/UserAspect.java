package javacode.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * @author Aaron.Sun
 * @description 用户切面
 * @date Created in 18:13 2020/5/17
 * @modified By
 */
@Aspect
public class UserAspect {

	@Pointcut("(execution(* javacode.service.impl.UserServiceImpl.*(..)))")
	public void userServiceCut() {
	}

	@Pointcut("(execution(* javacode.service.impl.StudentServiceImpl.*(..)))")
	public void studentServiceCut() {
	}

	@Before("userServiceCut()")
	public void before() {
		System.out.println("来自切面的通知-->before");
	}

	@After("userServiceCut()")
	public void after() {
		System.out.println("来自切面的通知-->after");
	}

	@AfterReturning(value = "userServiceCut()", returning = "retVal")
	public void afterReturning(Object retVal) {
		System.out.println("来自切面的通知-->after-returning " + retVal);
	}

	@AfterThrowing("userServiceCut()")
	public void afterThrowing() {
		System.out.println("来自切面的通知-->after-throwing");
	}

	@Around("studentServiceCut()")
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
