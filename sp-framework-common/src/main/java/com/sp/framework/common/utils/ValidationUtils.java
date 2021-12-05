package com.sp.framework.common.utils;

import com.sp.framework.common.enums.SystemErrorCodeEnum;
import com.sp.framework.common.exception.BusinessException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * 校验工具类
 * @author Ivan luo
 *
 */
public class ValidationUtils {

	private final static String PIX = "get";



	/**
	 * 断言对象中的字段值为非空
	 * @param obj 待检测对象
	 * @param ignoreFieldName 不做检测的忽略字段的名称
	 * @throws BusinessException 如果非忽略字段的值为空，抛出此异常
	 * @author Ivan luo
	 */
	public static void checkNotNull(Object obj,String... ignoreFieldName)throws BusinessException {
		if(obj==null){
			throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "Param is NULL");
		}
		Class<?> clazz=obj.getClass();
		Field[] fields=clazz.getDeclaredFields();
		String fieldName;
		String methodName;
		Method method;
		for(Field field : fields){
			fieldName=field.getName();
			if(containsElement(fieldName, ignoreFieldName)){
				continue;
			}
			methodName = PIX + fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
			try {
				method=clazz.getDeclaredMethod(methodName);
			} catch (Exception e) {
				continue;
			}
			Object result=null;
			try {
				result=method.invoke(obj);
			} catch (Exception e) {
				continue;
			}
			if(result==null||"".equals(result)){
				throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "Necessary Field ["+fieldName+"] is null");
			}
		}
	}
	
	/**
	 * 只校验数组中传入的字段
	 * 断言对象中的字段值为非空
	 * @param obj 待检测对象
	 * @throws BusinessException 如果非忽略字段的值为空，抛出此异常
	 * @author alexlu
	 */
	public static void checkNotNullByNecessary(Object obj,String... necessaryFieldName)throws BusinessException {
		if(obj==null){
			throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "Param is NULL");
		}
		Class<?> clazz=obj.getClass();
		Field[] fields=clazz.getDeclaredFields();
		String fieldName;
		String methodName;
		Method method;
		for(Field field : fields){
			fieldName=field.getName();
			if(!containsElement(fieldName, necessaryFieldName)){
				continue;
			}
			methodName=PIX + fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
			try {
				method=clazz.getDeclaredMethod(methodName);
			} catch (Exception e) {
				continue;
			}
			Object result=null;
			try {
				result=method.invoke(obj);
			} catch (Exception e) {
				continue;
			}
			if(result==null||"".equals(result)){
				throw new BusinessException(SystemErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "Necessary Field ["+fieldName+"] is null");
			}
		}
	}


	
	/**
	 * 检测container数组是否包含element元素
	 * @param element
	 * @param container
	 * @return boolean
	 * @author Ivan luo
	 */
	public static boolean containsElement(String element,String... container){
		for(String str : container){
			if(element.equals(str)){
				return true;
			}
		}
		return false;
	}




}
