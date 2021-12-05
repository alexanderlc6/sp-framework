/**
* Copyright (C) abba, 2010
*/
package com.sp.framework.orm.lark.orm.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

/**
* 
* 
* @since 1.0
*/
public class PageBeanUtilsBean extends BeanUtilsBean {
	@Override
	public Map<String, Object> describe(Object bean) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		if (bean == null) {
			//            return (Collections.EMPTY_MAP);
			return (new HashMap<String, Object>());
		}
		Map<String, Object> description = new HashMap<String, Object>();
		if (bean instanceof DynaBean) {
			DynaProperty[] descriptors = ((DynaBean) bean).getDynaClass().getDynaProperties();
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				description.put(name, getProperty(bean, name));
			}
		} else {
			PropertyDescriptor[] descriptors = getPropertyUtils().getPropertyDescriptors(bean);
			Class clazz = bean.getClass();
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				
				if (getPropertyUtils().getReadMethod( descriptors[i]) != null) {
					//调试出错误，临时解决方案
					if (getPropertyUtils().getNestedProperty(bean, name) != null)
						description.put(name, getPropertyUtils().getNestedProperty(bean, name));
				}
			}
		}
		return (description);

	}
}
