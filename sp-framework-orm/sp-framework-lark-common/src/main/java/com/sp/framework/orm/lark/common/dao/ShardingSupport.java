package com.sp.framework.orm.lark.common.dao;
/**
 * 所有水平分库的manager需要继承此接口
 * @author Alex Lu
 *
 */
public interface ShardingSupport {
	/**
	 * 公共库的关键字
	 */
	String COMMON = "common";
}
