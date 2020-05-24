package org.self.mybatis.session;

/**
 * @author Aaron.Sun
 * @description 事务隔离等级枚举类
 * @date Created in 21:12 2020/5/24
 * @modified By
 */
public enum TransactionIsolationLevel {
	NONE(0),
	READ_COMMITTED(2),
	READ_UNCOMMITTED(1),
	REPEATABLE_READ(4),
	SERIALIZABLE(8);

	private final int level;

	private TransactionIsolationLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return this.level;
	}
}

