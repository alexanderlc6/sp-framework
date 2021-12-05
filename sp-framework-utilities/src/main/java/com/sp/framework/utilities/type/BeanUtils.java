package com.sp.framework.utilities.type;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.http.client.ClientProtocolException;
import org.dozer.DozerBeanMapper;

/**
 * 反射的Utils函数集合.扩展自Apache Commons BeanUtils
 */
@SuppressWarnings({"unchecked","rawtypes","unused"})
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {
	private static final DozerBeanMapper mapper = new DozerBeanMapper();

	
	private BeanUtils() {
	}

	public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException {
		return getDeclaredField(object.getClass(), propertyName);
	}

	
	public static Field getDeclaredField(Class clazz, String propertyName) throws NoSuchFieldException {
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
				
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
	}
	/**
	 * 直接获取属性值
	 * @param object
	 * @param propertyName
	 * @return
	 */
	public static Object getNameProperty(Object object, String propertyName) {
		Field field=null;
		try {
			field = getDeclaredField(object, propertyName);
		} catch (NoSuchFieldException e1) {
		}
		Object result = null;
		if(null!=field){
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			try {
				result = field.get(object);
			} catch (Exception e) {
			}
			field.setAccessible(accessible);
		}
		return result;
	}
	/**
	 * 支持a.b.c
	 * @param object
	 * @param propertyName
	 * @return
	 */
	public static Object newForceGetProperty(Object object, String propertyName) {
		if(null==object)return null;
		if(StringUtil.isBlank(propertyName))return null;
		String[] s=propertyName.split("\\.");
		if(null==s)return null;
		for (int i = 0; i < s.length; i++) {
			object=forceGetProperty(object, s[i]);
		}
		return object;
	}
	public static Object forceGetProperty(Object object, String propertyName) {
		Object result=null;
		try {
		    if(object instanceof Map) {
		        result = ((Map)object).get(propertyName);
		    } else {
		        result = getObjValue(object, propertyName, null);
		    }
		} catch (Exception e) {
		}
		return result;
	}
	/**
	 * 直接赋属性值
	 * @param object
	 * @param propertyName
	 * @return
	 */
	public static void setNameProperty(Object object, String propertyName, Object newValue)
		throws NoSuchFieldException {
		
		Field field = getDeclaredField(object, propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, newValue);
		} catch (Exception e) {
		}
		field.setAccessible(accessible);
	}
	
	/**
	 * 通过set方法赋值
	 * @param object
	 * @param propertyName
	 * @param newValue
	 */
	public static void forceSetProperty(Object object, String propertyName, Object newValue) {
		if(null==object||StringUtil.isBlank(propertyName))return;
		String[] s=propertyName.split("\\.");
		if(null==s)return;
		for (int i = 0; i < s.length-1; i++) {
			object=forceGetProperty(object, s[i]);
		}
		try {
		    if(object instanceof Map) {
		        ((Map)object).put(propertyName, newValue);
		    } else {
		        setObjValue(object,propertyName,newValue);
		    }
		} catch (Exception e) {
		}
	}

	
	public static Object invokePrivateMethod(Object object, String methodName, Object... params)
			throws NoSuchMethodException {
		Class[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}

		Class clazz = object.getClass();
		Method method = null;
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				method = superClass.getDeclaredMethod(methodName, types);
				break;
			} catch (NoSuchMethodException e) {
				
			}
		}

		if (method == null)
			throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + methodName);

		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object result = null;
		try {
			result = method.invoke(object, params);
		} catch (Exception e) {
		}
		method.setAccessible(accessible);
		return result;
	}
	/**
	 * 通过反射动态调用方法
	 * @param classpath 类
	 * @param methodname 方法名称
	 * @param types[] 方法参数数组
	 * @return
	 */
	public static Method transferMethoder(String classpath,String methodname,Class types[]) {
		try {
			Class clazz = Class.forName(classpath);
			for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
				return superClass.getMethod(methodname,types);
			}
		} catch (Exception e) {
			return null;
		}
		return null; 
	}
	/**
	 * 通过反射动态调用方法
	 * @param classpath 类
	 * @param methodname 方法名称
	 * @param types[] 方法参数数组
	 * @return
	 */
	public static Method transferMethoder(Object obj,String methodname,Class types[]) {
		try {
			Class clazz = obj.getClass();
			for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
				return superClass.getMethod(methodname,types);
			}
		} catch (Exception e) {
			return null;
		}
		return null; 
	}
	/**
	 * 获取对象的属性(不包括继承的)
	 * @param obj
	 * @return Field[]
	 */
	public static Field[] getObjProperty(Object obj){
		Class c = obj.getClass();
		Field[] field=c.getDeclaredFields();
		return field;
	}
	
	/**
     * 拷贝对象属性，不抛出异常。深层次拷贝
     * @param dest 目标对象
     * @param src 元对象
     */
    public static void copyPropertiesSafe(Object dest, Object src) {
        if ((null == src) || (null == dest)) {
            return;
        }
        try {
        	mapper.map(src, dest);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * 只拷贝超类里的数据
	 * @param arg0
	 * @param arg1
	 * @throws Exception
	 */
	public static void copySupperPropertys(Object arg0,Object arg1) throws Exception{
		if(null!=arg0&&null!=arg1){
			Object value=null;
			if(arg1 instanceof Map) {
				for (String key : ((Map<String,Object>)arg1).keySet()) {
					value=BeanUtils.forceGetProperty(arg1, key);
					BeanUtils.forceSetProperty(arg0, key, value);
				}
			}else{
				Field[] field= getObjSupperProperty(arg1);
				if(null!=field){
					for (int i = 0; i < field.length; i++) {
						value=BeanUtils.forceGetProperty(arg1, field[i].getName());
						BeanUtils.forceSetProperty(arg0, field[i].getName(), value);
					}
				}
			}
		}else{
			throw new Exception("参数为空");
		}
	}
	/**
	 * 只拷贝超类里的数据
	 * @param arg0
	 * @param arg1
	 * @throws Exception
	 */
	public static void copyAllPropertys(Object arg0,Object arg1) throws Exception{
		if(null!=arg0&&null!=arg1){
			Object value=null;
			if(arg1 instanceof Map) {
				for (String key : ((Map<String,Object>)arg1).keySet()) {
					value=BeanUtils.forceGetProperty(arg1, key);
					BeanUtils.forceSetProperty(arg0, key, value);
				}
			}else{
				Field[] field= getObjAllProperty(arg1);
				if(null!=field){
					for (int i = 0; i < field.length; i++) {
						if("goodsSudate".equals(field[i].getName())){
							System.out.println(field[i].getName());
							System.out.println("==");
						}
						value=BeanUtils.forceGetProperty(arg1, field[i].getName());
						BeanUtils.forceSetProperty(arg0, field[i].getName(), value);
					}
				}
			}
		}else{
			throw new Exception("参数为空");
		}
	}
	/**
	 * 拷贝的数据(不包括继承的)
	 * @param arg0
	 * @param arg1
	 * @throws Exception
	 */
	public static void copyImplPropertys(Object arg0,Object arg1) throws Exception{
		if(null!=arg0&&null!=arg1){
			Object value=null;
			if(arg1 instanceof Map) {
				for (String key : ((Map<String,Object>)arg1).keySet()) {
					value=BeanUtils.forceGetProperty(arg1, key);
					BeanUtils.forceSetProperty(arg0, key, value);
				}
			}else{
				Field[] field= getObjProperty(arg1);
				if(null!=field){
					for (int i = 0; i < field.length; i++) {
						value=BeanUtils.forceGetProperty(arg1, field[i].getName());
						BeanUtils.forceSetProperty(arg0, field[i].getName(), value);
					}
				}
			}
		}else{
			throw new Exception("参数为空");
		}
	}
	/**
	 * 获取对象祖先的属性
	 * @param obj
	 * @return Field[]
	 */
	public static Field[] getObjSupperProperty(Object obj){
		Class c = obj.getClass();
		Class supper=c.getSuperclass();
		List<Field> list = new ArrayList<Field>();
		if(null!=supper){
			for (Class superClass = supper; superClass != Object.class; superClass = superClass.getSuperclass()) {
				Field[] fieldchild=superClass.getDeclaredFields();
				if(null!=fieldchild){
					for (Field field2 : fieldchild) {
						list.add(field2);
					}
				}
			}
		}
		Field[] field=new Field[list.size()];
		field=list.toArray(field);
		return field;
	}
	/**
	 * 获取对象祖先的属性,不包括supperbean
	 * @param obj
	 * @return Field[]
	 */
	public static Field[] getObjOpSupperProperty(Object obj){
		Class c = obj.getClass();
		Class supper=c.getSuperclass();
		List<Field> list = new ArrayList<Field>();
		if(null!=supper){
			for (Class superClass = supper; superClass != Object.class; superClass = superClass.getSuperclass()) {
				Field[] fieldchild=superClass.getDeclaredFields();
				if(null!=fieldchild){
					for (Field field2 : fieldchild) {
						list.add(field2);
					}
				}
			}
		}
		Field[] field=new Field[list.size()];
		field=list.toArray(field);
		return field;
	}
	/**
	 * 获取对象所有的属性(包括继承的)
	 * @param obj
	 * @return Field[]
	 */
	public static Field[] getObjAllProperty(Object obj){
		List<Field> list = new ArrayList<Field>();
		for (Class superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			Field[] fieldchild=superClass.getDeclaredFields();
			if(null!=fieldchild){
				for (Field field2 : fieldchild) {
					list.add(field2);
				}
			}
		}
		Field[] field=new Field[list.size()];
		field=list.toArray(field);
		return field;
	}
	/**
	 * 获取对象所有的属性(包括继承的),不包括supperbean
	 * @param obj
	 * @return Field[]
	 */
	public static Field[] getObjAllOpProperty(Object obj){
		List<Field> list = new ArrayList<Field>();
		for (Class superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			Field[] fieldchild=superClass.getDeclaredFields();
			if(null!=fieldchild){
				for (Field field2 : fieldchild) {
					list.add(field2);
				}
			}
		}
		Field[] field=new Field[list.size()];
		field=list.toArray(field);
		return field;
	}
	/**
	 * 获取对应的名称的get方法
	 * @param proName
	 * @return
	 */
	public static String getProNameMethod(String proName){
		String methodName="";
		if(StringUtil.isNotBlank(proName)){
			methodName="get"+StringUtil.upperFirst(proName);
		}
		return methodName;
	}
	/**
	 * 获取对应的名称的set方法
	 * @param proName
	 * @return
	 */
	public static String getProSetNameMethod(String proName){
		String methodName="";
		if(StringUtil.isNotBlank(proName)){
			methodName="set"+StringUtil.upperFirst(proName);
		}
		return methodName;
	}
	/**
	 * 获取对象里对应的属性值(通过get方法)
	 * @param obj
	 * @param name
	 * @param defObj 默认值
	 * @return
	 */
	public static Object getObjValue(Object obj,String name,Object defObj){
		Object valueObj=null;
		String methodName=getProNameMethod(name);
		Method method=transferMethoder(obj, methodName, new Class[0]);
		if(null!=method){
			try {
				valueObj=method.invoke(obj);
				if(null==valueObj){
					valueObj=defObj;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return valueObj;
	}
	/**
	 * 赋值对象里对应的属性值(通过set方法)
	 * @param obj
	 * @param name
	 * @param defObj 值
	 * @return
	 */
	public static void setObjValue(Object obj,String name,Object defObj){
		String methodName=getProSetNameMethod(name);
		try {
			Field field = getDeclaredField(obj, name);
			Class fclass=field.getType();
			Object valueobj=getValueByType(fclass.getName(), defObj);
			Class[] types={fclass};
			Method method=transferMethoder(obj, methodName, types);
			if(null!=method){
				method.invoke(obj,valueobj);
			}
		} catch (Exception e) {
		} 
	}
	/**
	 * @param className
	 * @param defObj
	 * @return
	 */
	public static Object getValueByType(String className,Object defObj){
		Object obj=null;
		if(className.indexOf("String")>=0){
			if(null==defObj){
				obj=null;
			}else{
				obj=defObj+"";
			}
		}else if(className.indexOf("int")>=0){
			if(StringUtil.isBlank(String.valueOf(defObj))){
				defObj="0";
			}
			obj=Long.valueOf(String.valueOf(defObj)).intValue();
		}else if(className.indexOf("Long")>=0){
			if(StringUtil.isBlank(String.valueOf(defObj))){
				defObj="0";
			}
			obj=Long.valueOf(String.valueOf(defObj));
		}else if(className.indexOf("Double")>=0){
			if(StringUtil.isBlank(String.valueOf(defObj))){
				defObj="0";
			}
			obj=Double.valueOf(String.valueOf(defObj));
		}else if(className.indexOf("double")>=0){
			if(StringUtil.isBlank(String.valueOf(defObj))){
				defObj="0";
			}
			obj=Double.valueOf(String.valueOf(defObj));
		}else if(className.indexOf("Date")>=0){
			if(null!=defObj&&StringUtil.isNotBlank(String.valueOf(defObj))){
				obj = TimeUtil.toCalendar(String.valueOf(defObj)).getTime();
				if(obj == null){
					obj = defObj;
				}
			}
		}else if(className.indexOf("Integer")>=0){
			if(StringUtil.isBlank(String.valueOf(defObj))){
				defObj="0";
			}
			obj=Integer.valueOf(String.valueOf(defObj));
		}else if(className.indexOf("boolean")>=0){
			if(StringUtil.isBlank(String.valueOf(defObj))){
				defObj="false";
			}
			if("true".equals(String.valueOf(defObj))){
				obj=true;
			}else{
				obj=false;
			}
		}else if(className.indexOf("Boolean")>=0){
			if(StringUtil.isBlank(String.valueOf(defObj))){
				defObj="false";
			}
			if("true".equals(String.valueOf(defObj))){
				obj=true;
			}else{
				obj=false;
			}
		}
		return obj;
	}
	/**
	 * 赋值对象里对应的属性值(通过set方法) defObj固定式string,要转换
	 * @param obj
	 * @param name
	 * @param defObj 值
	 * @return
	 */
	public static void setObjValue(Object obj,String name,String defObj){
		String methodName=getProSetNameMethod(name);
		try {
			Field field = getDeclaredField(obj, name);
			Class fclass=field.getType();
			Class[] types={fclass};
			Method method=transferMethoder(obj, methodName, types);
			if(null!=method){
				method.invoke(obj,getStringToType(fclass,defObj));
			}
		} catch (Exception e) {
		} 
	}
	public static Object getObject(Object obj,String name,String defObj){
		String methodName=getProSetNameMethod(name);
		try {
			Field field = getDeclaredField(obj, name);
			Class fclass=field.getType();
			Class[] types={fclass};
			return getStringToType(fclass,defObj);
		} catch (Exception e) {
		} 
		return null;
	}
	public static String getObjectHql(Object obj,String name,List<Object> paramlist,Object value){
		String methodName=getProSetNameMethod(name);
		try {
			Field field = getDeclaredField(obj, name);
			Class fclass=field.getType();
			Class[] types={fclass};
			return getStringToHql(fclass,name,paramlist,value);
		} catch (Exception e) {
		} 
		return null;
	}
	/**
	 * 把string 转化成对应的类型
	 * @param typeClass
	 * @param value
	 * @return
	 */
	public static Object getStringToType(Class typeClass,String value){
		Object obj=null;
		if(typeClass.equals(String.class)){
			if(null==value||StringUtil.isBlank(value)){
				obj="";
			}else{
				obj=String.valueOf(value);
			}
		}else if(typeClass.equals(Double.class)){
			if(null==value||StringUtil.isBlank(value)){
				obj=0D;
			}else{
				obj=Double.valueOf(value);
			}
		}else if(typeClass.equals(Integer.class)){
			if(null==value||StringUtil.isBlank(value)){
				obj=0;
			}else{
				obj=Integer.valueOf(value);
			}
		}else if(typeClass.equals(Date.class)){
			if(null==value||StringUtil.isBlank(value)){
				obj=null;
			}else{
				obj = TimeUtil.toCalendar(value).getTime();
			}
		}else if(typeClass.equals(Long.class)){
			if(null==value||StringUtil.isBlank(value)){
				obj=0L;
			}else{
				obj=Long.valueOf(value);
			}
		}else{
			obj=0;
		}
		return obj;
	}
	@SuppressWarnings("deprecation")
	public static String getStringToHql(Class typeClass,String name,List<Object> paramlist,Object value){
		String obj=null;
		if(typeClass.equals(String.class)){
			obj= "'--'";
			paramlist.add(null==value||"".equals(value)?"--":value);
		}else if(typeClass.equals(Double.class)){
			obj="0";
			paramlist.add(null==value||"".equals(value)?0D:value);
		}else if(typeClass.equals(Integer.class)){
			obj="0";
			paramlist.add(null==value||"".equals(value)?0:value);
		}else if(typeClass.equals(Date.class)){
			obj="to_date('1991.01.01','yyyy.mm.dd')";
			paramlist.add(null==value||"".equals(value)?new Date("1991.01.01"):value);
		}else if(typeClass.equals(Long.class)){
			obj="0";
			paramlist.add(null==value||"".equals(value)?0L:value);
		}else{
			obj="0";
			paramlist.add(null==value||"".equals(value)?0:value);
		}
		return obj;
	}
	
	public static void copyPropertiesNotNull(Object dest, Object orig)
        throws IllegalAccessException, InvocationTargetException {
		PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        // Copy the properties, converting as necessary
        if (orig instanceof DynaBean) {
            DynaProperty origDescriptors[] =
                ((DynaBean) orig).getDynaClass().getDynaProperties();
            for (int i = 0; i < origDescriptors.length; i++) {
                String name = origDescriptors[i].getName();
                if (propertyUtilsBean.isWriteable(dest, name)) {
                    Object value = ((DynaBean) orig).get(name);
                    if (value != null) {
                    	copyProperty(dest, name, value);
                    }
                }
            }
        } else if (orig instanceof Map) {
            Iterator names = ((Map) orig).keySet().iterator();
            while (names.hasNext()) {
                String name = (String) names.next();
                if (propertyUtilsBean.isWriteable(dest, name)) {
                    Object value = ((Map) orig).get(name);
                    if (value != null) {
                    	copyProperty(dest, name, value);
                    }
                }
            }
        } else {
            PropertyDescriptor origDescriptors[] =
                propertyUtilsBean.getPropertyDescriptors(orig);
            for (int i = 0; i < origDescriptors.length; i++) {
                String name = origDescriptors[i].getName();
                if ("class".equals(name)) {
                    continue; // No point in trying to set an object's class
                }
                if (propertyUtilsBean.isReadable(orig, name) &&
                    propertyUtilsBean.isWriteable(dest, name)) {
                    try {
                        Object value =
                            propertyUtilsBean.getSimpleProperty(orig, name);
                        if (value != null) {
                        	copyProperty(dest, name, value);
                        }
                    } catch (NoSuchMethodException e) {
                        ; // Should not happen
                    }
                }
            }
        }
    }
	/**
	 * 将Map的值复制到java对象的同名属性
	 * 
	 * @param targetBean
	 * @param dataMap
	 * @param ignoreEmptyString
	 * @throws Exception
	 */
	public static void copyMap2Pojo(Object targetBean, Map dataMap,
			boolean ignoreEmptyString) throws Exception {
		try {
			PropertyDescriptor origDescriptors[] = PropertyUtils
					.getPropertyDescriptors(targetBean);

			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if (name.equals("class")) {
					continue;
				}

				if (PropertyUtils.isWriteable(targetBean, name)) {
					Object obj = dataMap.get(name);
					if (obj == null) {
						continue;
					}

					if ((obj.toString().trim()).length() == 0
							&& ignoreEmptyString) {
						continue;
					}
					obj = convertValue(origDescriptors[i], obj);
					BeanUtils.copyProperty(targetBean, name, obj);
				}
			}// for end
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 将java对象的值复制到Map的同名属性
	 */
	public static Map copyPojo2Map(Object dataBean, boolean ignoreEmptyString) throws Exception {
		Map targetMap = new HashMap();
		try {
			PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(dataBean);

 			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if ("class".equals(name))
					continue;

				Object obj = PropertyUtils.getProperty(dataBean, name);
				if (obj == null) {
					continue;
				}

				if ((obj.toString().trim()).length() == 0 && ignoreEmptyString) {
					continue;
				}
				
				if (obj instanceof Date){
					//obj = CommonMethod.dateFormat((Date)obj);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					obj = sdf.format((Date)obj);
					
				}
				//obj = convertValue(origDescriptors[i], obj);
				targetMap.put(name, obj);
				// BeanUtils.copyProperty(targetMap, name, obj);

			}// for end
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return targetMap;
	}
	
	/**
	 * 将一个对象的属性值取出来放置到Map中。Map的Key为对象属性名称
	 * 
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static Map getProperties(Object bean) throws Exception {
		if (bean == null) {
			return null;
		}

		Map dataMap = new HashMap();
		try {
			PropertyDescriptor origDescriptors[] = PropertyUtils
					.getPropertyDescriptors(bean);

			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if (name.equals("class")) {
					continue;
				}

				if (PropertyUtils.isReadable(bean, name)) {
					Object obj = PropertyUtils.getProperty(bean, name);
					if (obj == null) {
						continue;
					}
					obj = convertValue(origDescriptors[i], obj);
					dataMap.put(name, obj);
				}
			}// for end
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return dataMap;
	}

	private static Object convertValue(PropertyDescriptor origDescriptor,
			Object obj) throws Exception {
		if (obj == null) {
			return null;
		}

		if (obj.toString().trim().length() == 0) {
			return null;
		}
		if (origDescriptor.getPropertyType() == Date.class) {
			//同一个时间，第一次从界面层传过来时，obj为String类型;转化后为Date类型
			 if (obj instanceof Date) {
				 return obj;
			}else{
				obj = TimeUtil.toCalendar(obj.toString()).getTime();
			}
		}
		return obj;
	}
}

