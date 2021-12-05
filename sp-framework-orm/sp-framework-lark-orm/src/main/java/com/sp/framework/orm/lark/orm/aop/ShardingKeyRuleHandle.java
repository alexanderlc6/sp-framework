package com.sp.framework.orm.lark.orm.aop;

import java.util.List;

/**
 * 数据库执分规则
 * 各项目需要自行实现此接口
 * @author Alex Lu
 *
 */
public interface ShardingKeyRuleHandle {

	/**
	 * 获取当前的key规则
	 * @param shardingkeyList 当前数据key的列表
	 * @param currentKeyValue 表示当前执行关键字具体的数值
	 * @return 其中一个key做为当前事务的数据库，返回null，则表示不做选择，直接进入公共库
	 */
	String calcKeyRule(List<Object> shardingkeyList,Object currentKeyValue);
}
