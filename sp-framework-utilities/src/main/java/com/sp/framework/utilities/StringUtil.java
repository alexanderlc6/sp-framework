/**
 * Copyright (c) 2012 Yonghui All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yonghui.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Yonghui.
 *
 * YONGHUI MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. YONGHUI SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.sp.framework.utilities;

import java.util.List;

public class StringUtil {
	
	public static final String DELIM = ",";
	
	private static final char[] Digit = { '0','1','2','3','4','5','6','7','8','9',
	        'A','B','C','D','E','F' };
	
	/**
	 * 使用默认分隔符","拼接字符串
	 * @param list
	 * @return
	 */
	public static String join(List<? extends Object> list){
		return join(list, DELIM);
	}
	
	/**
	 * 使用默认分隔符","拼接字符串
	 * @param arr
	 * @return
	 */
	public static String join(Object[] arr){
		return join(arr, DELIM);
	}
	
	/**
	 * 使用指定分隔符拼接字符串
	 * @param list
	 * @param seperator
	 * @return
	 */
	public static String join(List<? extends Object> list, String seperator){
    	if(list == null || list.size() == 0) return "";
    	Object[] t = new Object[0];
    	return join(list.toArray(t),seperator);
    }
    
	/**
	 * 使用指定分隔符拼接字符串
	 * @param arr
	 * @param seperator
	 * @return
	 */
    public static String join(Object[] arr, String seperator){
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < arr.length; i++) {
			sb.append(seperator + arr[i]);
		}
    	if (sb.length() > 0) sb.deleteCharAt(0);
    	return sb.toString();
    }

    /**
     * 使用默认分隔符","拆分字符串
     * @param str
     * @return
     */
    public static String[] split(String str){
    	//TODO
    	return (String[])null;
    }
    
    /**
     * 使用指定分隔符拆分字符串
     * @param str
     * @param seperator
     * @return
     */
    public static String[] split(String str, String seperator){
    	//TODO
    	return (String[])null;
    }
    
    /**
     * 从字符串左边开始，根据index的定义截取子字符串。
     * index>0 时，截取的是左边的index个字符的子字符串，类似String.substring(0,index），如原字符串长度不足返回全部
     * index<0 时，截取的是从左边第index字符开始的子字符串，类似String.substring(abs(index))，如index超出字符串长度返回空字符串
     * @param str
     * @param index
     * @return
     */
    public static String left(String str, int index){
    	//TODO
    	return null;
    }
    
    /**
     * 从字符串右边开始，根据index的定义截取子字符串。
     * index>0 时，截取的是最右边的index个字符，如果原字符串长度不足则返回全部
     * index<0 时，截取的是从右边第index个字符开始的子字符串，如index超出字符串长度返回空字符串
     * @param str
     * @param index
     * @return
     */
    public static String right(String str, int index){
    	//TODO
    	return null;
    }
    
    
    /**
     * Byte数组转换为十六进制字符串
     * @param bytes
     * @return
     */
    public static String bytes2String(byte[] bytes){
    	if(bytes == null || bytes.length ==0) return "";
    	StringBuffer sb = new StringBuffer();
    	for(byte b: bytes)
    		sb.append(byte2HEX(b));
    	return sb.toString();
    }
    
    /**
     * Byte转换为十六进制字符串
     * @param ib
     * @return
     */
    public static String byte2HEX(byte ib) {      
        char [] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }
}
