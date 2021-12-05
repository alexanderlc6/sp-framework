package com.sp.framework.orm.lark.orm.dao;

public class ShardingStatusHolder {

	private static final ThreadLocal<String> shardingSourceStatusHolder = new ThreadLocal<String>();
	
	public static void setShardingDataSourceStatus(String status){
		shardingSourceStatusHolder.set(status);
	}
	
	public static String getShardingDataSourceStatus(){
		return shardingSourceStatusHolder.get();
	}
	
	public static void clearShardingDataSourceStatus(){
		shardingSourceStatusHolder.remove();
	}
}
