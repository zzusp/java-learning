package org.activiti.engine.exception;

/**
 * @author Aaron.Sun
 * @description 自定义工作流异常。该异常返回时可不被activiti框架封装
 * @date Created in 19:16 2020/8/9
 * @modified By
 */
public class WorkflowException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WorkflowException() {
	}

	public WorkflowException(String message) {
		super(message);
	}

	public WorkflowException(Throwable cause) {
		super(cause);
	}

	public WorkflowException(String message, Throwable cause) {
		super(message, cause);
	}

}
