package org.self.mybatis.spring;

import org.springframework.dao.UncategorizedDataAccessException;

/**
 * @author Aaron.Sun
 * @description mybatis的系统异常
 * @date Created in 16:30 2020/5/24
 * @modified By
 */
public class MyBatisSystemException extends UncategorizedDataAccessException {
	private static final long serialVersionUID = 1L;

	public MyBatisSystemException(Throwable cause) {
		super((String) null, cause);
	}
}
