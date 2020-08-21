package top.felixu.annotation;

import java.lang.annotation.*;

/**
 * @author felixu
 * @since 2020.08.21
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {

    String version();
}
