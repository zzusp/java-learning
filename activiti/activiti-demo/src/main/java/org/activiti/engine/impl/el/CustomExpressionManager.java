package org.activiti.engine.impl.el;

import org.activiti.engine.delegate.Expression;

import javax.el.ValueExpression;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 自定义表达式管理器。为返回准确的、非封装后的自定义异常WorkflowException
 * @date Created in 19:16 2020/8/9
 * @modified By
 */
public class CustomExpressionManager extends ExpressionManager {

	public CustomExpressionManager(Map<Object, Object> beans) {
		super(beans);
	}

	@Override
	public Expression createExpression(String expression) {
		ValueExpression valueExpression = this.expressionFactory.createValueExpression(this.parsingElContext, expression.trim(), Object.class);
		return new CustomJuelExpression(valueExpression, expression);
	}


}
