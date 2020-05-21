package lifecycle.service;

import org.springframework.context.SmartLifecycle;

/**
 * @author Aaron.Sun
 * @description 用户服务接口
 * @date Created in 11:48 2020/5/15
 * @modified By
 */
public interface UserService extends SmartLifecycle {

	/**
	 * 由姓名查找用户
	 *
	 * @param name 姓名
	 * @return 用户信息
	 */
	String findByName(String name);

}
