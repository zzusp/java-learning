package processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author Aaron.Sun
 * @description 自定义bean工厂后置处理器
 * @date Created in 21:50 2020/5/28
 * @modified By
 */
@Component
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	public CustomBeanFactoryPostProcessor() {
		System.out.println("【BeanFactoryPostProcessor接口】调用BeanFactoryPostProcessor实例化方法");
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("【BeanFactoryPostProcessor接口】调用BeanFactoryPostProcessor.postProcessBeanFactory()，" +
				"在需要修改`bean`实例的配置元数据时使用（如：数据源配置（即将${datasource.url}等替换为properties文件中对应的值））");
	}
}
