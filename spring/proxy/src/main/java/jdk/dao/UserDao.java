package jdk.dao;

/**
 * @author Aaron.Sun
 * @description 用户信息数据库访问接口
 * @date Created in 11:49 2020/5/15
 * @modified By
 */
public interface UserDao {

	/**
	 * 由姓名查找用户
	 *
	 * @param name 姓名
	 * @return 用户信息
	 */
	String findByName(String name);

}
