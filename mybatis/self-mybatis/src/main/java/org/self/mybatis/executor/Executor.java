package org.self.mybatis.executor;

import org.self.mybatis.mapping.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 16:55 2020/5/22
 * @modified By
 */
public interface Executor {

	<E> List<E> query(MappedStatement var1, Object var2) throws SQLException;

}
