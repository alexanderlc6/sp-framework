package com.sp.framework.orm.lark.orm.dao;

public class MoreStatusHolder {

	private static final ThreadLocal<String> moreSourceStatusHolder = new ThreadLocal<String>();
	
	public static void setMoreDataSourceStatus(String status){
		moreSourceStatusHolder.set(status);
	}
	
	public static String getMoreDataSourceStatus(){
		return moreSourceStatusHolder.get();
	}
	
	public static void clearMoreDataSourceStatus(){
		moreSourceStatusHolder.remove();
	}
}
