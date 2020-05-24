package org.self.mybatis.exceptions;

/**
 * @author Aaron.Sun
 * @description 持久化异常
 * @date Created in 16:29 2020/5/24
 * @modified By
 */
public class PersistenceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PersistenceException() {
	}

	public PersistenceException(String message) {
		super(message);
	}

	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceException(Throwable cause) {
		super(cause);
	}
}
