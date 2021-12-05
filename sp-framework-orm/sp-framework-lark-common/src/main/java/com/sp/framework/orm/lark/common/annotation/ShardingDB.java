package com.sp.framework.orm.lark.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示当前方法需要进行水平拆分数据源
 * 会根据相关的计算法则进行
 * @author Alex Lu
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ShardingDB {
	/**
	 * 表示拆分关键字处于方法参数中的位置
	 * 0为第一个index
	 * @return
	 */
	int value() default 0;
}
