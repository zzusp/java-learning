package org.self.mybatis.mapping;

/**
 * @author Aaron.Sun
 * @description sql对象接口
 * @date Created in 17:21 2020/5/22
 * @modified By
 */
public interface SqlSource {

	BoundSql getBoundSql(Object var1);

}
