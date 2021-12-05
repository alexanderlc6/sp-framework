package com.sp.framework.common.utils;
/**  
* @Title: ListUtil.java
* @Package com.baozun.common.utils
* @Description: TODO(用一句话描述该文件做什么)
* @author alexlu
* @date 2017年4月6日
* @version V1.0  
*/

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.*;

/**
    * @ClassName: ListUtil
    * @Description: TODO(这里用一句话描述这个类的作用)
    * @author alexlu
    * @date 2017年4月6日
    *
*/

public class ListUtil {
    
    /**
     * 
        * @Title: groupByProperty
        * @Description: list根据属性分组
        * @param @param collection
        * @param @param propertyName
        * @param @param clazz
        * @param @return    参数
        * @return Map<K,List<T>>    返回类型
        * @throws
     */
    @SuppressWarnings("unchecked")
    public static <T, K> Map<K, List<T>> groupByProperty(Collection<T> collection, String propertyName, Class<K> clazz) {
	Map<K, List<T>> map = new HashMap<K, List<T>>();
	for (T item : collection) {
	    BeanWrapper beanWrapper = new BeanWrapperImpl(item);
	    K key = (K) beanWrapper.getPropertyValue(propertyName);
	    List<T> list = map.get(key);
	    if (null == list) {
		list = new ArrayList<T>();
		map.put(key, list);
	    }
	     list.add(item);
	}
	return map;
    }
    
    /**
     * 
        * @Title: isEmpty
        * @Description: 验证集合非空
        * @param @param collection
        * @param @return    参数
        * @return boolean    返回类型
        * @throws
     */
    public static <T> boolean isEmpty(Collection<T> collection){
	boolean isEmpty = false;
	if(collection==null || collection.size()==0){
	    isEmpty = true;
	}
	return isEmpty;
    }
    
    

}
