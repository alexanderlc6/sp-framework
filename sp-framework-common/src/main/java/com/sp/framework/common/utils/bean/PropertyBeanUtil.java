package com.sp.framework.common.utils.bean;

/**
 * Created by Alexa on 2017/8/7.
 */

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;

public class PropertyBeanUtil {
    public PropertyBeanUtil() {
    }

    public static List<Map> listToKey_ValueList(List data, Class orgClass) {
        ArrayList retList = null;

        try {
            if(data != null && data.size() > 0) {
                retList = new ArrayList();

                for(int i = 0; i < data.size(); ++i) {
                    Object entity = data.get(i);
                    BeanInfo beanInfo = Introspector.getBeanInfo(orgClass);
                    PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
                    Map codeMap = new LinkedHashMap();
                    PropertyDescriptor[] var9 = pds;
                    int var10 = pds.length;

                    for(int var11 = 0; var11 < var10; ++var11) {
                        PropertyDescriptor pd = var9[var11];
                        codeMap.put(pd.getName(), pd.getReadMethod().invoke(entity, new Object[0]));
                    }

                    retList.add(codeMap);
                }
            }
        } catch (Exception var13) {
            var13.printStackTrace();
        }

        return retList;
    }

    public static List<Map> listToKey_ValueList(List datas, Class orgClass, String key, String value) {
        ArrayList retList = null;

        try {
            if(datas != null && datas.size() > 0) {
                retList = new ArrayList();
                int i = 0;

                for(int j = datas.size(); i < j; ++i) {
                    Object data = datas.get(i);
                    BeanInfo beanInfo = Introspector.getBeanInfo(orgClass);
                    PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
                    int size = 0;
                    Map codeMap = new HashMap();
                    PropertyDescriptor[] var12 = pds;
                    int var13 = pds.length;

                    for(int var14 = 0; var14 < var13; ++var14) {
                        PropertyDescriptor pd = var12[var14];
                        Object obj;
                        if(pd.getName().equals(key)) {
                            ++size;
                            obj = pd.getReadMethod().invoke(data, new Object[0]);
                            codeMap.put("id", obj == null?"":obj.toString());
                        }

                        if(pd.getName().equals(value)) {
                            ++size;
                            obj = pd.getReadMethod().invoke(data, new Object[0]);
                            codeMap.put("text", obj == null?"":obj.toString());
                        }

                        if(size >= 2) {
                            break;
                        }
                    }

                    retList.add(codeMap);
                }
            }
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        return retList;
    }
}
