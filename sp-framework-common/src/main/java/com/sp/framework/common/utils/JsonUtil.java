package com.sp.framework.common.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: JsonUtil
 * @Description:JsonUtil
 * @author alexlu
 * @date 2017年4月28日
 *
 */

public class JsonUtil {


    /**
     * hashMap 转化成表单字符串
     * @param map
     * @return
     */
    public static String map2Form(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        if (map == null) {
            return stringBuilder.toString();
        } else {
            for (Map.Entry<String, String> entry :
                    map.entrySet()) {
                stringBuilder.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        }
    }

    /**
     * hashMap 转化成表单字符串
     * @param map
     * @return
     */
    public static String mapToForm(Map<String, Object> map) {
        StringBuilder stringBuilder = new StringBuilder();
        if (map == null) {
            return stringBuilder.toString();
        } else {
            for (Map.Entry<String, Object> entry :
                    map.entrySet()) {
                stringBuilder.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        }
    }

    public static <T> List<T> formatList(String jaosnStr, Class<T> clazz) {
        List<T> list = null;
        try {
            if (StringUtil.isEmpty(jaosnStr)) {
                return null;
            }
            List<Object> lst = JsonFormatter.toObject(jaosnStr, List.class);
            if (lst.size() > 0) {
                list = lst.stream().map(j -> formatObject(j, clazz)).collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public static <T> T formatObject(Object obj, Class<T> clazz) {
        T t = null;
        try {
            t = JsonFormatter.toObject(JsonFormatter.toJsonString(obj), clazz);
        } catch (Exception e) {
            return null;
        }
        return t;
    }
}
