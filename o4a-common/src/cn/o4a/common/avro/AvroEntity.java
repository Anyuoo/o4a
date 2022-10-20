package cn.o4a.common.avro;

import java.lang.annotation.*;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/29 10:31
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface AvroEntity {
    String value() default "";
}
