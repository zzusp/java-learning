package processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;

/**
 * @author Aaron.Sun
 * @description 自定义bean实例化感知后置处理
 * @date Created in 22:10 2020/5/28
 * @modified By
 */
@Component
public class CustomInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

	public CustomInstantiationAwareBeanPostProcessor() {
		super();
		System.out.println("【InstantiationAwareBeanPostProcessorAdapter接口】调用InstantiationAwareBeanPostProcessorAdapter实例化方法");
	}

	@Override
	public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
		return super.predictBeanType(beanClass, beanName);
	}

	@Override
	public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
		return super.determineCandidateConstructors(beanClass, beanName);
	}

	@Override
	public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
		return super.getEarlyBeanReference(bean, beanName);
	}

	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		return super.postProcessBeforeInstantiation(beanClass, beanName);
	}

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		return super.postProcessAfterInstantiation(bean, beanName);
	}

	@Override
	public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
		if ("userService".equals(beanName)) {
			System.out.println("【InstantiationAwareBeanPostProcessorAdapter接口】" +
					"调用InstantiationAwareBeanPostProcessorAdapter.postProcessProperties()");
		}
		return super.postProcessProperties(pvs, bean, beanName);
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if ("userService".equals(beanName)) {
			System.out.println("【InstantiationAwareBeanPostProcessorAdapter接口】" +
					"调用InstantiationAwareBeanPostProcessorAdapter.postProcessBeforeInitialization()");
		}
		return super.postProcessBeforeInitialization(bean, beanName);
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if ("userService".equals(beanName)) {
			System.out.println("【InstantiationAwareBeanPostProcessorAdapter接口】" +
					"调用InstantiationAwareBeanPostProcessorAdapter.postProcessAfterInitialization()");

		}
		return super.postProcessAfterInitialization(bean, beanName);
	}
}
