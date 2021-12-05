package com.sp.framework.common.utils;

/**
  * @Title: IntUtil.java
  * @Package com.baozun.common.utils
  * @Description: util for integer
  * @author alexlu
  * @date 2017年4月15日
  * @version V1.0  
  */

/**
  * @ClassName: IntUtil
  * @Description:util for integer
  * @author alexlu
  * @date 2017年4月15日
  *
  */

public class IntUtil {
    
    public static int parseString (String str){
	int result = 0;
	if(str==null || "".equals(str)){
	    return result;
	}
	result = Integer.parseInt(str);
	return result;
    }
    
    public static boolean isEmpty(Integer param){
	boolean result=false;
	if(param ==null || param <0){
	    result =true;
	}
	return result;
    }
    

}
