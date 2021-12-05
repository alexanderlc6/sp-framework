
package com.sp.framework.common.utils;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 * @ClassName: CookieTools 
 * @date 2015年5月9日 下午6:42:08
 * @version V2.0 
 */
public class CookieTools {
	
    /** 
     * @Fields EXPIRE_NEVER : Integer 最大值 
     */
    public final static int EXPIRE_NEVER = Integer.MAX_VALUE;
    /** 
     * @Fields EXPIRE_DAY : 天
     */
    public final static int EXPIRE_DAY = 60 * 60 * 24;
    /** 
     * @Fields EXPIRE_WEEK : 周
     */
    public final static int EXPIRE_WEEK = EXPIRE_DAY * 7;
    /** 
     * @Fields EXPIRE_MONTH : 月
     */
    public final static int EXPIRE_MONTH = EXPIRE_WEEK * 4;
    
    /** 
     * @Fields EXPIRE_SESSION : cookie 失效时间
     */
    public final static int EXPIRE_SESSION = 	 -1;
    
    /**
     * @Title getCookie
     * @Description 获取Cookie
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * @Title setCookie
     * @Description 设置Cookie
     * @param response
     * @param name
     * @param value
     * @param expiry
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int expiry) {
        setCookieWithPath(response, name, value, expiry, "/",null);
    }

    /**
     * @Title setCookieWithPath
     * @Description 设置带Path Cookie
     * @param response
     * @param name
     * @param value
     * @param expiry
     * @param path
     */
    public static void setCookieWithPath(HttpServletResponse response, String name, String value, int expiry, String path, String pattern) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(expiry);
        cookie.setPath(path);
        cookie.setSecure(false);
        if(pattern!=null&&!"".equals(pattern)){
        	cookie.setDomain(pattern);
        }
        response.addCookie(cookie);
    }


    /**
     * @Title getCookieIntValue
     * @Description 获取Int类型的Cookie值
     * @param request
     * @param name
     * @return
     */
    public static int getCookieIntValue(HttpServletRequest request, String name){
        int result = 0;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    try { result = Integer.parseInt(cookie.getValue()); }
                    catch (Exception e) { result = 0; }
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @Title getCookieValue
     * @Description 获取Cookie值
     * @param request
     * @param name
     * @return
     */
    public static Object getCookieValue(HttpServletRequest request, String name){
        Object result = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    result = cookie.getValue();
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @Title deleteCookie
     * @Description 删除Cookie
     * @param request
     * @param response
     * @param name
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        deleteCookieWithPath(request, response, name, "/");
    }

    /**
     * @Title deleteCookieWithPath
     * @Description 删除指定路径的Cookie
     * @param request
     * @param response
     * @param name
     * @param path
     */
    public static void deleteCookieWithPath(HttpServletRequest request, HttpServletResponse response, String name, String path) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setPath(path);
            response.addCookie(cookie);
        }
    }
    
    /**
	 * 根据cookieName删除带域的cookie
     * @Title: deleteCookieWithPath 
     * @date 2015年5月5日 下午3:41:07
     * @modifier
     * @modifydate 
     * @param request
     * @param response
     * @param name cookie名称
     * @param path 路径
     * @param domain 域名
     */
    public static void deleteCookieWithPath(HttpServletRequest request, HttpServletResponse response, String name, String path, String domain) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            cookie.setMaxAge(0);
            if(StringUtils.isNotBlank(domain)){
            	cookie.setDomain(domain);
            }
            cookie.setPath(path);
            response.addCookie(cookie);
        }
    }
}
