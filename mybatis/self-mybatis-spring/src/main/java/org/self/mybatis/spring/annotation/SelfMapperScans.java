package org.self.mybatis.spring.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Aaron.Sun
 * @description 多个自定义mapper扫描注解
 * @date Created in 10:19 2020/5/24
 * @modified By
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({MapperScannerRegistrar.RepeatingRegistrar.class})
public @interface SelfMapperScans {
	SelfMapperScan[] value();
}
