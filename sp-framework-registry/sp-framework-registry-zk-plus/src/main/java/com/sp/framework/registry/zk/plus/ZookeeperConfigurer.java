package com.sp.framework.registry.zk.plus;


import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.sp.framework.registry.zk.plus.utils.AESGeneralUtil;
import com.sp.framework.registry.zk.plus.utils.ServerCache;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import com.sp.framework.utilities.http.HttpJsonClient;
import com.sp.framework.utilities.json.JsonUtil;
import com.sp.framework.registry.zk.plus.bean.ZkSerializerImpl;
import com.sp.framework.registry.zk.plus.bean.ZookeeperApp;
import org.springframework.util.CollectionUtils;

/**
 * 获取zookeeper中的配置信息
 * 1.通过zookeeper ip:port 以及 path list来汇总获取
 * 2.将ext.properties中的配置信息覆盖第一步的内容
 * 3.与spring的配置相结合
 * 4.保存到本地内存中 static Properties sysProp
 * 可提供给其它非spring管理的bean来进行获取配置
 * 
 * @author Alex Lu
 *
 */
public class ZookeeperConfigurer extends PropertyPlaceholderConfigurer implements Watcher{

    private Logger log = LoggerFactory.getLogger(ZookeeperConfigurer.class);

    //重写zookeeper中存储的配置
    private List<String> overrideLocaltions;
    
    //自定义配置文件
    private List<String> customLocations;

    private final static Properties sysProp = new Properties();

    /**
     * 获取所有系统属性
     * 
     * @return
     */
    public static Properties getProps(){
        return sysProp;
    }

    public List<String> getOverrideLocaltions(){
        return overrideLocaltions;
    }

    public void setOverrideLocaltions(List<String> overrideLocaltions){
        this.overrideLocaltions = overrideLocaltions;
    }
    
    public List<String> getCustomLocations() {
        return customLocations;
    }

    public void setCustomLocations(List<String> customLocations) {
        this.customLocations = customLocations;
    }

    /**
     * 将source中的属性覆盖到dest属性中
     * 
     * @param dest
     * @param source
     */
    private void copyProperties(Properties dest,Properties source,Boolean outLog){
        
        Enumeration<?> enums=source.propertyNames();
        
        while(enums.hasMoreElements()){
            String key=(String)enums.nextElement();
            dest.put(key, source.get(key));
            if(outLog) {
             log.info("ovride Zookeeper key:{}\t value:{}", key, source.get(key));
            }
        }
        
        
    }

    /**
     * 保存到本地内存中，可作为非spring bean代码中获取配置的方式
     * 
     * @param props
     */
    private void saveProperties(Properties props){

        Enumeration<?> enums = props.propertyNames();

        while (enums.hasMoreElements()){
            String key = (String) enums.nextElement();
            sysProp.put(key, props.get(key));
        }

    }

    //获取扩展配置的properties
    private Properties queryOverrideLocation(){
        
        Properties props=new Properties();
        
        for(String location: overrideLocaltions){
            
            PathMatchingResourcePatternResolver pmrpr=new PathMatchingResourcePatternResolver();
            try{
                Resource[] resource=pmrpr.getResources(location);
                
                Properties prop = PropertiesLoaderUtils.loadProperties(resource[0]);
                
                
                copyProperties(props,prop,false);
            }
            catch(Exception e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        
        return props;
        
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,Properties zkprops) throws BeansException{

        //发起http请求，获取与租户相关的zookeeper 参数
        String isUserZkproxy = zkprops.getProperty("is.user.zkproxy");
        //是否使用zk代理
        Boolean isZkproxy = (null == isUserZkproxy ? false : Boolean.parseBoolean(isUserZkproxy));
        String zkAppkey = zkprops.getProperty("zk.appkey");
        String zkServiceUrl = zkprops.getProperty("zk.service.url");
        String znodes = zkprops.getProperty("zk.cofing.root");
        String zkhost = zkprops.getProperty("zk.host");
        //获取appsystem
        ZookeeperApp zookeeperApp = null;
        //得到项目有关的zk.host
        //得到秘钥
        String secret = null;
        if (isZkproxy){
            if (StringUtils.isEmpty(znodes)){
                throw new ApplicationContextException("zk.cofing.root is null!");
            }
            if (StringUtils.isEmpty(zkAppkey)){
                throw new ApplicationContextException("zk.appkey is null!");
            }

            if (StringUtils.isEmpty(zkServiceUrl)){
                throw new ApplicationContextException("zk.service.url is null!");
            }
            try{
                zookeeperApp = this.getZookeeperApp(zkServiceUrl, zkAppkey);
            }catch (Exception e1){
                log.error("获取app异常：" + zkServiceUrl + ":" + zkAppkey, e1);
                throw new ApplicationContextException("获取app异常：" + zkServiceUrl + ":" + zkAppkey, e1);
            }
            if (null == zookeeperApp){
                throw new ApplicationContextException("没有此租户");
            }
            secret = zookeeperApp.getSecret();
            zkhost = zookeeperApp.getZkAddr();
        }

        System.out.println("-----------------------zhost:" + zkhost);
        ZkClient zk = null;
        try{
            zk = new ZkClient(zkhost, 30000, 30000, new ZkSerializerImpl());
        }catch (Exception e){
            log.error("Failed to connect to zk server" + zkhost, e);
            throw new ApplicationContextException("Failed to connect to zk server" + zkhost, e);
        }

        try{
            for (String znode : znodes.split(",")){
                List<String> children = zk.getChildren(znode.trim());
                for (String child : children){
                    try{
                        String value = zk.readData(znode + "/" + child);
                        if (isZkproxy){
                            try{
                                value = AESGeneralUtil.decrypt(value, secret);
                            }catch (Exception e){
                                log.info("未解密：Zookeeper key:{}\t value:{}", child, value);
                            }
                        }
                        log.info("Zookeeper key:{}\t value:{}", child, value);
                        zkprops.setProperty(child, value);
                    }catch (Exception e){
                        log.error("Read property(key:{}) error", child);
                        log.error("Exception:", e);
                    }
                }
            }
        }catch (Exception e){
            log.error("Failed to get property from zk server" + zkhost, e);
            throw new ApplicationContextException("Failed to get property from zk server" + zkhost, e);
        }finally{
            try{
                zk.close();
            }catch (Exception e){
                log.error("Error found when close zookeeper connection.", e);
            }
        }

        Properties overProps = queryOverrideLocation();

        //将扩展的配置信息覆盖至zookeeper获取的属性
        copyProperties(zkprops, overProps,true);
        //保存到静态属性中，供其它非spring托管理的类获取配置信息
        saveProperties(zkprops);
        //与spring进行结合
        super.processProperties(beanFactoryToProcess, zkprops);
    }

    private ZookeeperApp getZookeeperApp(String zkServiceUrl,String zkAppkey) throws Exception{
        String jsonData = HttpJsonClient.postJsonDataByJson(zkServiceUrl, zkAppkey, 1000);
        ZookeeperApp zookeeperApp = null;
        try{
            zookeeperApp = JsonUtil.readValue(jsonData, ZookeeperApp.class);
        }catch (Exception e){
            log.error(jsonData);
        }
        if (null != zookeeperApp){
            String key = zookeeperApp.getAppKey();
            String value = zookeeperApp.getSecret();
            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)){
                ServerCache.setValue(key, value);
            }
        }
        return zookeeperApp;
    }

    @Override
    public void process(WatchedEvent event){
    }
    
    @Override
    protected Properties mergeProperties() throws IOException {
        
        //处理location的profile路径
        ProfilePath.resetLocation(this);
        
        //处理ext的profile路径
        ProfilePath.resetOverrideLocation(this);
        
        Properties result = new Properties();

        if (this.localOverride) {
            // Load properties from file upfront, to let local properties override.
            loadProperties(result);
        }

        if (this.localProperties != null) {
            for (Properties localProp : this.localProperties) {
                CollectionUtils.mergePropertiesIntoMap(localProp, result);
            }
        }

        if (!this.localOverride) {
            // Load properties from file afterwards, to let those properties override.
            loadProperties(result);
        }

        return result;
    }

}
