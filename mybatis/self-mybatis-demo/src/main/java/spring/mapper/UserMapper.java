package spring.mapper;

import org.self.mybatis.annotations.SelfSelect;

/**
 * @author Aaron.Sun
 * @description 用户信息数据库访问接口
 * @date Created in 9:13 2020/5/22
 * @modified By
 */
public interface UserMapper {

	@SelfSelect("SELECT name FROM user WHERE id = #{id}")
	String queryUserName(String id);

}
