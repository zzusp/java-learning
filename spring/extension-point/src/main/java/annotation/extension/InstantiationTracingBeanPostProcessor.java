package annotation.extension;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author Aaron.Sun
 * @description bean初始化追踪 后置处理
 * @date Created in 16:43 2020/5/18
 * @modified By
 */
@Component
public class InstantiationTracingBeanPostProcessor implements BeanPostProcessor {

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("beanPostProcessor-->before-->Bean '" + beanName + "' created : " + bean.toString());
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("beanPostProcessor-->after-->Bean '" + beanName + "' created : " + bean.toString());
		return bean;
	}
}
