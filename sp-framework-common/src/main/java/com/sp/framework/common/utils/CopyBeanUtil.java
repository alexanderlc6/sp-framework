package com.sp.framework.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sp.framework.common.base.BaseClone;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CopyBeanUtil {
    static String[] types = {"java.lang.Integer",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.Byte",
            "java.lang.Boolean",
            "java.lang.Character",
            "java.lang.String",
            "int","double","long","short","byte","boolean","char","float","java.util.Date","java.math.BigDecimal"};

    public static <T,K extends BaseClone> T copyPropertiesByJson(K orig, Class<T> clazz){
        K clone = null;
        try {
            clone = (K) orig.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Field[] fields = clone.getClass().getDeclaredFields();
        for(Field field:fields){
            if(!ArrayUtils.contains(types,field.getType().getName())){
                field.setAccessible(true);
                try {
                    field.set(clone,null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSONObject.parseObject(JSONObject.toJSONString(clone, SerializerFeature.WriteMapNullValue), clazz);
    }

    /**
     * 复制map对象
     * @explain 将paramsMap中的键值对全部拷贝到resultMap中
     * paramsMap中的内容不会影响到resultMap(深拷贝)
     * @param paramsMap
     *     被拷贝对象
     * @param resultMap
     *     拷贝后的对象
     */
    public static void mapCopy(Map paramsMap, Map resultMap) {
        if (resultMap == null) resultMap = new HashMap();
        if (paramsMap == null) return;

        Iterator it = paramsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            resultMap.put(key, paramsMap.get(key) != null ? paramsMap.get(key) : "");
        }
    }
}
