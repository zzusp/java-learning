package annotation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Aaron.Sun
 * @description spring扫描配置
 * @date Created in 11:48 2020/5/15
 * @modified By
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("annotation")
public class AppConfig {
}
