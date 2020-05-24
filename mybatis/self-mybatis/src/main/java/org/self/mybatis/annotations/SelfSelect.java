package org.self.mybatis.annotations;

import java.lang.annotation.*;

/**
 * @author Aaron.Sun
 * @description 查询注解
 * @date Created in 17:33 2020/5/22
 * @modified By
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SelfSelect {
	String[] value();
}
