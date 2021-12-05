package com.sp.framework.utilities.type;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/************************************************
 * Copyright (c)  by ysc
 * All right reserved.
 * Create Date: 2011-6-21
 * Create Author: liurong
 * File Name:  js执行方法
 * Last version:  1.0
 * Function:
 * Last Update Date:
 * Change Log:
**************************************************/	
 
public class ScriptUtil {
	public final static String ERROR="0";
	public final static String OK="1";
	public final static String EXCEPTION="error";
	public static String evel(String str){
		return evel(str, "JavaScript");
	}
	public static String evel(String str,String shortname){
		String s="";
		if(StringUtil.isBlank(str)){
			return s;
		}
		if(StringUtil.isBlank(shortname)){
			shortname="JavaScript";
		}
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName(shortname);
		try {
			str = str.replace("\r", "").replace("\n", "").replace("\r\n", "");
			Object obj=engine.eval(str);
			if(null!=obj){
				s=obj.toString();
			}
		} catch (Exception e) {
		    e.printStackTrace();
			s=EXCEPTION;
		}
		return s;
	}
	public static void main(String[] args){
		String s="var msg;if('\r头条摘要头条摘要头条摘要头条摘要头条'.replace('\\r','').replace('\\r','').length>40){msg='头条摘要头条摘要头条摘要头条摘要头条'.replace(/<[^>]+>/g,'').substring(0,40)+'...';}else{msg='头条摘要头条摘要头条摘要头条摘要头条'.replace(/<[^>]+>/g,'');}msg;";
		System.out.println(evel(s));
	}
}
