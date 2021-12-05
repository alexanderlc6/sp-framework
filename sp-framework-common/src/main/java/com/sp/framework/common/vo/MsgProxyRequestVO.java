package com.sp.framework.common.vo;

import java.util.Properties;

/**
 * Created by dell on 2018/2/22.
 */
public class MsgProxyRequestVO {
    /**
     * 消息标签
     */
    private String tag;

    /**
     * 消息主题
     */
    private String topic;

    /**
     * 用户自定义属性集合
     */
    private Properties userProperties;

    /**
     * 系统自定义属性集合
     */
    private Properties systemProperties;

    /**
     * 服务URL地址
     */
    private String serviceUrl;

    /**
     * 服务请求数据
     */
    private Object requestData;

    public MsgProxyRequestVO() {
    }

    public MsgProxyRequestVO(String tag, String topic, Properties userProperties, Properties systemProperties, String serviceUrl, Object requestData) {
        this.tag = tag;
        this.topic = topic;
        this.userProperties = userProperties;
        this.systemProperties = systemProperties;
        this.serviceUrl = serviceUrl;
        this.requestData = requestData;
    }

    void putSystemProperties(String key, String value){
        if(null == this.systemProperties){
            this.systemProperties = new Properties();
        }

        if(key != null && value != null){
            this.systemProperties.put(key,value);
        }
    }

    public String getSystemProperties(String key){
        return null != this.systemProperties ? this.systemProperties.getProperty(key) : null;
    }

    void putUserProperties(String key, String value){
        if(null == this.userProperties){
            this.userProperties = new Properties();
        }

        if(key != null && value != null){
            this.userProperties.put(key,value);
        }
    }

    public String getUserProperties(String key){
        return null != this.userProperties ? (String)this.userProperties.get(key) : null;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Properties getUserProperties() {
        return userProperties;
    }

    public void setUserProperties(Properties userProperties) {
        this.userProperties = userProperties;
    }

    public Properties getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(Properties systemProperties) {
        this.systemProperties = systemProperties;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public Object getRequestData() {
        return requestData;
    }

    public void setRequestData(Object requestData) {
        this.requestData = requestData;
    }
}
