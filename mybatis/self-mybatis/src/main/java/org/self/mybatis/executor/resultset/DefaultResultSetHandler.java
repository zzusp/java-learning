package org.self.mybatis.executor.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 15:44 2020/5/23
 * @modified By
 */
public class DefaultResultSetHandler implements ResultSetHandler {
	@Override
	public <E> List<E> handleResultSets(Statement statement) throws SQLException {
		List<E> result = new ArrayList<>();
		// 获取查询结果集
		ResultSet rs = statement.getResultSet();
		int i = 1;
		//循环遍历查询结果集
		while (rs.next()) {
			result.add((E) rs.getObject(i));
		}
		return result;
	}
}
