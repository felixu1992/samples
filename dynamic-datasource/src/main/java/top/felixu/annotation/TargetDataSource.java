package top.felixu.annotation;

import java.lang.annotation.*;

/**
 * 目标数据源注解
 * 用于切换数据源
 *
 * @author felixu
 * @date 2019.07.23
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    /**
     * 指定的数据源id
     *
     * @return 指定的数据源id
     */
    String name() default "";
}
