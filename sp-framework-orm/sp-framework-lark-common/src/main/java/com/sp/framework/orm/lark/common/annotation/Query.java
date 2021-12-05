package com.sp.framework.orm.lark.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {
	String value() default "";
	boolean pagable() default false;
	boolean withGroupby() default false;
}
