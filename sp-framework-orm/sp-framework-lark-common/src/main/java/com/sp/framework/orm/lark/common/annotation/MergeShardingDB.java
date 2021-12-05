package com.sp.framework.orm.lark.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加上此注解后，会将所有拆分数据源中数据全部取出来，并整合返回
 * 业务方法必须返回List
 * 
 * @author Alex Lu
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MergeShardingDB {
	int value() default 0;
	
	/**
	 * 指定数据源key,如果不指定表示查询全部
	 * @return
	 */
	String[] dataSourceKeys() default {};
}
