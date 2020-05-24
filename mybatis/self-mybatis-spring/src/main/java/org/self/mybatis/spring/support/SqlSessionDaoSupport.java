package org.self.mybatis.spring.support;

import org.self.mybatis.session.SqlSession;
import org.self.mybatis.session.defaults.SqlSessionFactory;
import org.self.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;

/**
 * @author Aaron.Sun
 * @description sqlSessionDao帮助类
 * @date Created in 16:01 2020/5/24
 * @modified By
 */
public abstract class SqlSessionDaoSupport extends DaoSupport {

	private SqlSessionTemplate sqlSessionTemplate;

	public SqlSessionDaoSupport() {
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		if (this.sqlSessionTemplate == null || sqlSessionFactory != this.sqlSessionTemplate.getSqlSessionFactory()) {
			this.sqlSessionTemplate = this.createSqlSessionTemplate(sqlSessionFactory);
		}

	}

	protected SqlSessionTemplate createSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	public final SqlSessionFactory getSqlSessionFactory() {
		return this.sqlSessionTemplate != null ? this.sqlSessionTemplate.getSqlSessionFactory() : null;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public SqlSession getSqlSession() {
		return this.sqlSessionTemplate;
	}

	public SqlSessionTemplate getSqlSessionTemplate() {
		return this.sqlSessionTemplate;
	}

	@Override
	protected void checkDaoConfig() {
		Assert.notNull(this.sqlSessionTemplate, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
	}

}
