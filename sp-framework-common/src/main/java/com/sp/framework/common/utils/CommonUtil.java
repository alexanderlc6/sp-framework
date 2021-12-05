package com.sp.framework.common.utils;

/**
 * @Title: CommonUtil.java
 * @Package com.baozun.common.utils
 * @Description: 通用util
 * @author alexlu
 * @date 2017年5月5日
 * @version V1.0
 */

import java.lang.reflect.Method;
import java.util.Random;

/**
 * @ClassName: CommonUtil
 * @Description: 通用util
 * @author alexlu
 * @date 2017年5月5日
 *
 */

public class CommonUtil {
    /**
     *
     * @Title: getFieldValueByName
     * @Description: 根据属性名获取属性值
     * @param @param fieldName
     * @param @param o
     * @param @return    参数
     * @return Object    返回类型
     * @throws
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成n位验证码
     * @param codeNumsCount
     * @return
     */
    public static String generateVerifyCode(Integer codeNumsCount){
        String valCode = "";

        if(codeNumsCount > 0){
            for (Integer i = 0; i < codeNumsCount; i++){
                char c = (char)(randomInt(0, 9) + '0');
                valCode += String.valueOf(c);
            }
        }

        return  valCode;
    }

    /**
     * 生成随机数
     * @param from
     * @param to
     * @return
     */
    public static int randomInt(int from, int to){
        Random random= new Random();
        return from + random.nextInt(to - from);
    }
//
//    /**
//     * 获取IP地址
//     * @param request
//     * @return
//     */
//    public static String getIpAddr(HttpServletRequest request) {
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("X-Real-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_CLIENT_IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        return ip;
//    }
}
