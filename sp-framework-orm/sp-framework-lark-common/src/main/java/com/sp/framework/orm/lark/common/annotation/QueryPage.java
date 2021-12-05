package com.sp.framework.orm.lark.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分页查询时，通过此注解设置对应count的id名称
 * @author Alex Lu
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryPage {
	String value() default "";
	
}
