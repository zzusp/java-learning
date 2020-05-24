package org.self.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Aaron.Sun
 * @description 事务接口
 * @date Created in 18:50 2020/5/22
 * @modified By
 */
public interface Transaction {

	Connection getConnection() throws SQLException;

	void commit() throws SQLException;

	void rollback() throws SQLException;

	void close() throws SQLException;
}
