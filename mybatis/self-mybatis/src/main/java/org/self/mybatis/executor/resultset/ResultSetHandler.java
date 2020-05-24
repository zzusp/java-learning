package org.self.mybatis.executor.resultset;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 结果集处理器接口
 * @date Created in 15:44 2020/5/23
 * @modified By
 */
public interface ResultSetHandler {

	<E> List<E> handleResultSets(Statement statement) throws SQLException;

}
