package processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author Aaron.Sun
 * @description 自定义bean后置处理器
 * @date Created in 21:56 2020/5/28
 * @modified By
 */
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {

	public CustomBeanPostProcessor() {
		System.out.println("【BeanPostProcessor接口】调用BeanPostProcessor实例化方法");
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if ("userService".equals(beanName)) {
			System.out.println("【BeanPostProcessor接口】调用BeanPostProcessor.postProcessBeforeInitialization()，" +
					"可在此处修改bean属性。beanName：" + beanName);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if ("userService".equals(beanName)) {
			System.out.println("【BeanPostProcessor接口】调用BeanPostProcessor.postProcessAfterInitialization()，" +
					"可在此处修改bean属性。beanName：" + beanName);
		}
		return bean;
	}
}
