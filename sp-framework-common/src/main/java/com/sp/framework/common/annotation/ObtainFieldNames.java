package com.sp.framework.common.annotation;

import java.lang.annotation.*;

/**
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ObtainFieldNames {
    boolean value() default true;
}
