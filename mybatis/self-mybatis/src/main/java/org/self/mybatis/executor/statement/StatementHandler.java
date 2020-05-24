package org.self.mybatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 16:35 2020/5/22
 * @modified By
 */
public interface StatementHandler {

	Statement prepare(Connection connection) throws Exception;

	void parameterize(Statement var1) throws SQLException;

	<E> List<E> query(Statement var1) throws SQLException;

}
