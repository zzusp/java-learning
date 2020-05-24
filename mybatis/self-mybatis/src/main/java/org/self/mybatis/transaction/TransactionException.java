package org.self.mybatis.transaction;

import org.self.mybatis.exceptions.PersistenceException;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 21:19 2020/5/24
 * @modified By
 */
public class TransactionException extends PersistenceException {
	private static final long serialVersionUID = -433589569461084605L;

	public TransactionException() {
	}

	public TransactionException(String message) {
		super(message);
	}

	public TransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransactionException(Throwable cause) {
		super(cause);
	}
}
