package com.sp.framework.registry.apollo.plus;


import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * 获取apollo中的配置信息
 * 1.通过apollo ip:port 以及 path list来汇总获取
 * 2.将ext.properties中的配置信息覆盖第一步的内容
 * 3.与spring的配置相结合
 * 4.保存到本地内存中 static Properties sysProp
 * 可提供给其它非spring管理的bean来进行获取配置
 *
 * @author Alex Lu
 *
 */
public class ApolloConfigurer extends PropertyPlaceholderConfigurer{

    private Logger log = LoggerFactory.getLogger(ApolloConfigurer.class);

    /**
     * 重写zookeeper中存储的配置
     */
    private List<String> overrideLocations;

    /**
     * 自定义配置文件
     */
    private List<String> customLocations;
    
    private List<String> apolloLocations;

    private final static Properties sysProp = new Properties();

    /**
     * 获取所有系统属性
     * 
     * @return
     */
    public static Properties getProps(){
        return sysProp;
    }

    public List<String> getOverrideLocations(){
        return overrideLocations;
    }

    public void setOverrideLocations(List<String> overrideLocations){
        this.overrideLocations = overrideLocations;
    }
    
    public List<String> getCustomLocations() {
        return customLocations;
    }

    public void setCustomLocations(List<String> customLocations) {
        this.customLocations = customLocations;
    }
    

    public List<String> getApolloLocations() {
		return apolloLocations;
	}

	public void setApolloLocations(List<String> apolloLocations) {
		this.apolloLocations = apolloLocations;
	}

	/**
     * 将source中的属性覆盖到dest属性中
     * 
     * @param dest
     * @param source
     */
    private void copyProperties(Properties dest,Properties source,Boolean outLog){
        Enumeration<?> enums = source.propertyNames();
        
        while(enums.hasMoreElements()){
            String key=(String)enums.nextElement();
            dest.put(key, source.get(key));
            if(outLog) {
                log.info("Override apollo key:{}\t value:{}", key, source.get(key));
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

    /**
     * 获取扩展配置的properties
     * @return
     */
    private Properties queryOverrideLocation(){
        Properties props=new Properties();
        
        for(String location: overrideLocations){
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
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,Properties apolloprops) throws BeansException{

    	try {
            //从apollo中获取所有配置信息
              Config config = ConfigService.getAppConfig(); //config instance is singleton for each namespace and is never null
              Set<String> fieldnames =  config.getPropertyNames();
              //遍历配置信息
              for(String fieldname : fieldnames){
                  String attributeName=fieldname;
                  String attributeValue = config.getProperty(fieldname,"");
                  apolloprops.put(attributeName,attributeValue);
              }
          } catch (Exception e) {
              e.printStackTrace();
              logger.info("获取apollo配置失败");
          }

        Properties overProps = queryOverrideLocation();

        //将扩展的配置信息覆盖至apollo获取的属性
        copyProperties(apolloprops, overProps,true);
        //保存到静态属性中，供其它非spring托管理的类获取配置信息
        saveProperties(apolloprops);
        //与spring进行结合
        super.processProperties(beanFactoryToProcess, apolloprops);
    }
    
    @Override
    protected Properties mergeProperties() throws IOException {
        
        //处理location的profile路径
        ApolloProfilePath.resetLocation(this);
        
        //处理ext的profile路径
        ApolloProfilePath.resetOverrideLocation(this);
        
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
