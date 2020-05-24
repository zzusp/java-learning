package org.self.mybatis.transaction;

import org.self.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;

/**
 * @author Aaron.Sun
 * @description
 * @date Created in 18:54 2020/5/22
 * @modified By
 */
public interface TransactionFactory {

	Transaction newTransaction(DataSource var1, TransactionIsolationLevel var2, boolean var3);

}
