package org.self.mybatis.spring.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Aaron.Sun
 * @description 自定义mapper扫描器
 * @date Created in 10:14 2020/5/24
 * @modified By
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({MapperScannerRegistrar.class})
@Repeatable(SelfMapperScans.class)
public @interface SelfMapperScan {

	String[] value() default {};

}
