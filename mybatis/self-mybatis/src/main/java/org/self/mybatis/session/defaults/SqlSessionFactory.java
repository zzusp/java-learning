package org.self.mybatis.session.defaults;

import org.self.mybatis.session.Configuration;
import org.self.mybatis.session.SqlSession;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 19:00 2020/5/22
 * @modified By
 */
public interface SqlSessionFactory {

	SqlSession openSession() throws Exception;

	Configuration getConfiguration();

}
