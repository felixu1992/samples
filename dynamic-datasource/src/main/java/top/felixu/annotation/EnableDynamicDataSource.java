package top.felixu.annotation;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import top.felixu.dynamic.DynamicDataSourceRegister;

import java.lang.annotation.*;

/**
 * Enable模式开启动态数据源
 *
 * @author felixu
 * @date 2019.07.23
 */
@Inherited
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration(DynamicDataSourceRegister.class)
public @interface EnableDynamicDataSource {

}
