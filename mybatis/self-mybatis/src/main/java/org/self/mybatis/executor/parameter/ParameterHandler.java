package org.self.mybatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 15:59 2020/5/23
 * @modified By
 */
public interface ParameterHandler {

	Object getParameterObject();

	void setParameters(PreparedStatement var1) throws SQLException;

}
