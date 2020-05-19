package annotation.extension;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ResourceBundle;

/**
 * @author Aaron.Sun
 * @description 数据源后置beanFactory处理器
 * @date Created in 18:31 2020/5/18
 * @modified By
 */
@Component
public class DatasourceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	private ResourceBundle resource = ResourceBundle.getBundle("resource");

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("beanFactoryPostProcessor...");
		String[] bdNames = beanFactory.getBeanDefinitionNames();
		BeanDefinition bd;
		// 遍历所有BeanDefinition的name
		for (int i = 0; i < bdNames.length; i++) {
			bd = beanFactory.getBeanDefinition(bdNames[i]);
			// 判断bd是否包含property值
			if (bd.hasPropertyValues()) {
				MutablePropertyValues propertyValues = bd.getPropertyValues();
				// 包含则更新
				visitPropertyValues(propertyValues);
			}
		}
	}

	/**
	 * 修改配置元文件（查找properties文件，并替换指定值）
	 * <p>
	 * copy form org.springframework.beans.factory.config.BeanDefinitionVisitor
	 *
	 * @param pvs
	 */
	protected void visitPropertyValues(MutablePropertyValues pvs) {
		PropertyValue[] pvArray = pvs.getPropertyValues();
		PropertyValue[] var3 = pvArray;
		int var4 = pvArray.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			PropertyValue pv = var3[var5];
			// 获取value的逻辑重写了，演示用，简单获取resource.properties文件中的数据
			Object newVal = this.resolveValue(pv.getValue());
			if (!ObjectUtils.nullSafeEquals(newVal, pv.getValue())) {
				pvs.add(pv.getName(), newVal);
			}
		}

	}

	protected Object resolveValue(Object value) {
		String stringValue;
		if (value instanceof TypedStringValue) {
			TypedStringValue typedStringValue = (TypedStringValue) value;
			stringValue = typedStringValue.getValue();
			if (stringValue != null) {
				String visitedString = resource.getString(stringValue.replace("${", "").replace("}", ""));
				;
				typedStringValue.setValue(visitedString);
			}
		}
		return value;
	}

}
