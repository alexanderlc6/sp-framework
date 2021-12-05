package com.sp.framework.registry.zk.plus.utils;

import java.util.HashMap;
import java.util.Map;

public class ServerCache{

    //缓存Map  
    private static Map<String, String> map = new HashMap<String, String>();

    private ServerCache(){
    }

    //设置缓存值
    public static void setValue(String key,String obj){
        map.put(key, obj);
    }

    //根据key获取缓存值
    public static String getValue(String key){
        return map.get(key);
    }

}
