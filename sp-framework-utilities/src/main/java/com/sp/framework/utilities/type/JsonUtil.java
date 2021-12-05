package com.sp.framework.utilities.type;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


/************************************************
 * Copyright (c)  by ysc
 * All right reserved.
 * Create Date: 2009-8-15
 * Create Author: liurong
 * File Name:  josn工具
 * Last version:  1.0
 * Function:这里写注释
 * Last Update Date:
 * Change Log:
**************************************************/ 
 
public class JsonUtil {
    private ObjectMapper mapper;
    public ObjectMapper getMapper() {
        return mapper;
    }
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    public JsonUtil(Include inclusion){
        mapper =new ObjectMapper();
        mapper.setSerializationInclusion(inclusion);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES , false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        setDateFormat(TimeUtil.theTimeFormat);
    }
    /**
     * 创建输出全部属性
     * @return
     */
    public static JsonUtil buildNormalBinder(){
        return new JsonUtil(Include.ALWAYS);
    }
    /**
     * 创建只输出非空属性的
     * @return
     */
    public static JsonUtil buildNonNullBinder(){
        return new JsonUtil(Include.NON_NULL);
    }
    /**
     * 创建只输出初始值被改变的属性
     * @return
     */
    public static JsonUtil buildNonDefaultBinder(){
        return new JsonUtil(Include.NON_DEFAULT);
    }
    
    /**
	 * 将对象转成json串
	 * @param obj
	 * @return
	 */
	public String getJsonToObject(Object obj) {
		if (obj == null) {
			return "";
		}
		try {
			return getMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
    /**
     * 把json字符串转成对象
     * @param json
     * @param clazz
     * @return
     */
    public <T> T getJsonToObject(String json,Class<T> clazz){
        T object=null;
        if(StringUtil.isNotBlank(json)) {
            try {
                object=getMapper().readValue(json, clazz);
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
        return object;
    }
    /**
     * 把JSON转成list
     * @param json
     * @param clazz
     * @return
     */
    public <T> List<T> getJsonToList(String json,Class<T> clazz){
    	List<T> list = null;
        if(StringUtil.isNotBlank(json)) {
           try {
        	   JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
        	   list = getMapper().readValue(json,javaType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    
    /**
     * 把JSON转成Map
     * @param json
     * @param keyclazz
     * @param valueclazz
     * @return
     */
    public <T, E> Map<T, E> getJsonToMap(String json,Class<T> keyclazz,Class<E> valueclazz){
    	Map<T,E> object=null;
        if(StringUtil.isNotBlank(json)) {
            try {
            	JavaType javaType = mapper.getTypeFactory().constructParametricType(Map.class, keyclazz,valueclazz);
                object=getMapper().readValue(json,javaType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }
    
    /**
     * 把map转成combo数据格式的json格式
     * @return String (json)
     */
    public String getMapToJson(Map<String,String> map) {
        List<String[]> list =new ArrayList<String[]>();
        if (null != map && !map.isEmpty()) {
            for (String key : map.keySet()) {
                String[] strS = new String[2];
                strS[0] = key;
                strS[1] = map.get(key);
                list.add(strS);
            }
        }
        return toJson(list);
    }

    /**
     * 把JSON转成Object
     * @param json
     * @param keyclazz
     * @param valueclazz
     * @return
     */
    public Object getJsonToObject(String json,Class<?> objclazz,Class<?> ...pclazz){
        Object object=null;
        if(StringUtil.isNotBlank(json)) {
            try {
            	JavaType javaType = mapper.getTypeFactory().constructParametricType(objclazz, pclazz);
                object=getMapper().readValue(json,javaType);
            } catch (Exception e) {
            }
        }
        return object;
    }
    
    
    /**
     * 把对象转成字符串
     * @param object
     * @return
     */
    public String toJson(Object object){
        String json=null;
        try {
            json=getMapper().writeValueAsString(object);
        }  catch (Exception e) {
        	e.printStackTrace();
        }
        return json;
    }
    /**
     * 设置日期格式
     * @param pattern
     */
    public void setDateFormat(String pattern){
        if(StringUtil.isNotBlank(pattern)){
            DateFormat df=new SimpleDateFormat(pattern);
            getMapper().setDateFormat(df);
        }
    }
}