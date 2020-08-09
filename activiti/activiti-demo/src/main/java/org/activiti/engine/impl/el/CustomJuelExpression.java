package org.activiti.engine.impl.el;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.exception.WorkflowException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.delegate.invocation.ExpressionGetInvocation;
import org.activiti.engine.impl.delegate.invocation.ExpressionSetInvocation;
import org.activiti.engine.impl.interceptor.DelegateInterceptor;

import javax.el.*;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 自定义juel表达式校验器。为返回准确的、非封装后的自定义异常WorkflowException
 * @date Created in 19:16 2020/8/9
 * @modified By
 */
public class CustomJuelExpression implements Expression {

	private static final String WORKFLOW_EXCEPTION = "org.activiti.engine.exception.WorkflowException";

	private String expressionText;
	private ValueExpression valueExpression;

	public CustomJuelExpression(ValueExpression valueExpression, String expressionText) {
		this.valueExpression = valueExpression;
		this.expressionText = expressionText;
	}

	@Override
	public Object getValue(VariableScope variableScope) {
		ELContext elContext = Context.getProcessEngineConfiguration().getExpressionManager().getElContext(variableScope);
		return this.getValueFromContext(elContext, Context.getProcessEngineConfiguration().getDelegateInterceptor());
	}

	@Override
	public void setValue(Object value, VariableScope variableScope) {
		ELContext elContext = Context.getProcessEngineConfiguration().getExpressionManager().getElContext(variableScope);

		try {
			ExpressionSetInvocation invocation = new ExpressionSetInvocation(this.valueExpression, elContext, value);
			Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
		} catch (Exception var5) {
			throw new ActivitiException("Error while evaluating expression: " + this.expressionText, var5);
		}
	}

	@Override
	public String toString() {
		return this.valueExpression != null ? this.valueExpression.getExpressionString() : super.toString();
	}

	@Override
	public String getExpressionText() {
		return this.expressionText;
	}

	@Override
	public Object getValue(ExpressionManager expressionManager, DelegateInterceptor delegateInterceptor, Map<String, Object> availableVariables) {
		ELContext elContext = expressionManager.getElContext(availableVariables);
		return this.getValueFromContext(elContext, delegateInterceptor);
	}

	private Object getValueFromContext(ELContext elContext, DelegateInterceptor delegateInterceptor) {
		try {
			ExpressionGetInvocation invocation = new ExpressionGetInvocation(this.valueExpression, elContext);
			delegateInterceptor.handleInvocation(invocation);
			return invocation.getInvocationResult();
		} catch (PropertyNotFoundException var4) {
			throw new ActivitiException("Unknown property used in expression: " + this.expressionText, var4);
		} catch (MethodNotFoundException var5) {
			throw new ActivitiException("Unknown method used in expression: " + this.expressionText, var5);
		} catch (Exception var6) {
			// 如果包含自定义工作流异常，则返回准确信息
			if (var6.getMessage().contains(WORKFLOW_EXCEPTION)) {
				throw new WorkflowException(var6.getCause().getMessage(), var6);
			}
			throw new ActivitiException("Error while evaluating expression: " + this.expressionText, var6);
		}
	}

}
