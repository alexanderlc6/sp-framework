package com.sp.framework.orm.lark.tool.util;

/**
 * 36进制的转换
 * @author alexlu
 *
 */
public class ThirtySixSystemUtil {

	
	/**
	 * 将10进制的字符串、数字，转成36进制的字符串
	 * @param obj
	 * @return
	 */
	public static String toThirtySixSystem(Object obj){
	
		if(obj instanceof String){
			Long result=Long.parseLong((String)obj);
			return Long.toString(result, 36);
		}
		else if(obj instanceof Integer){
			
			return Integer.toString((Integer)obj, 36);
		}
		
		else if(obj instanceof Long){
			
			return Long.toString((Long)obj, 36);
		}
		
		return null;
		
	}
	
	/**
	 * 将36进制转成10进制数字
	 * @param str
	 * @return
	 */
	public static Long fromThirtySixSystem(String str){
		
		return Long.parseLong(str, 36);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String str="10";
		Long l1=1333l;
		long l2=23335l;
		int i1=2435235;
		Integer i2=633;
		
		System.out.println("str:"+toThirtySixSystem(str));
		System.out.println("l1:"+toThirtySixSystem(l1));
		System.out.println("l2:"+toThirtySixSystem(l2));
		System.out.println("i1:"+toThirtySixSystem(i1));
		System.out.println("i2:"+toThirtySixSystem(i2));
		
		
	}

}
