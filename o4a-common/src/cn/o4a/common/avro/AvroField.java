package cn.o4a.common.avro;

import java.lang.annotation.*;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/29 9:31
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AvroField {
    String value() default "";

    String type() default "";
}
