package com.sp.framework.orm.lark.tool.propertyutil;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.sp.framework.orm.lark.tool.zk.ZooKeeperOperator;
import com.sp.framework.orm.lark.tool.system.ProfileConfigUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesFactoryBean extends PropertiesLoaderSupport implements FactoryBean<Properties>, InitializingBean {

	private static Log log = LogFactory.getLog(PropertiesFactoryBean.class.getName());  
	
	private boolean singleton = true;

	private Properties singletonInstance;
	
	private Resource[] locations;
	
	private String node;
	
	private String overrideLocation;
	
	private Properties queryOverrideLocation(){
		
			if(overrideLocation==null||overrideLocation.length()==0){
				return null;
			}
			
			PathMatchingResourcePatternResolver pmrpr=new PathMatchingResourcePatternResolver();
			try{
				Resource[] resource=pmrpr.getResources(overrideLocation);
				
				Properties prop = PropertiesLoaderUtils.loadProperties(resource[0]);
				
				
				return prop;
			}
			catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
		
		
		
	}
	
	/**
	 * 将source中的属性覆盖到dest属性中
	 * @param dest
	 * @param source
	 */
	private void copyProperties(Properties dest,Properties source){
		
		Enumeration<?> enums=source.propertyNames();
		
		while(enums.hasMoreElements()){
			String key=(String)enums.nextElement();
			dest.put(key, source.get(key));
		}
		
		
	}
	
	
	public void setLocation(Resource location) {
		this.locations = new Resource[] {location};
	}

	/**
	 * Set locations of properties files to be loaded.
	 * <p>Can point to classic properties files or to XML files
	 * that follow JDK 1.5's properties XML format.
	 * <p>Note: Properties defined in later files will override
	 * properties defined earlier files, in case of overlapping keys.
	 * Hence, make sure that the most specific files are the last
	 * ones in the given list of locations.
	 */
	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}
	


	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}



	/**
	 * Set whether a shared 'singleton' Properties instance should be created,
	 * or rather a new Properties instance on each request.
	 * <p>
	 * Default is "true" (a shared singleton).
	 */
	public final void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public final boolean isSingleton() {
		return this.singleton;
	}

	public final void afterPropertiesSet() throws IOException {
		if (this.singleton) {
			this.singletonInstance = createProperties();
		}
	}

	public final Properties getObject() throws IOException {
		if (this.singleton) {
			return this.singletonInstance;
		} else {
			return createProperties();
		}
	}

	public Class<Properties> getObjectType() {
		return Properties.class;
	}

	/**
	 * Template method that subclasses may override to construct the object
	 * returned by this factory. The default implementation returns the plain
	 * merged Properties instance.
	 * <p>
	 * Invoked on initialization of this FactoryBean in case of a shared
	 * singleton; else, on each {@link #getObject()} call.
	 * 
	 * @return the object returned by this factory
	 * @throws IOException
	 *             if an exception occured during properties loading
	 * @see #mergeProperties()
	 */
	protected Properties createProperties() throws IOException {
		Properties pros=new Properties();
//		 Environment environment = new StandardEnvironment();  
//		String profile =environment.getProperty("spring.profiles.active");
//		System.out.println(profile);
//		
		Properties sysPro=ProfileConfigUtil.findResourcePro(locations[0]);
		String zkHost=sysPro.getProperty("zk.host");
		String zkRoot=sysPro.getProperty("zk.cofing.root");
		
		ZooKeeperOperator zko=ZooKeeperOperator.getInstance(zkHost);
		
		String curNode=zkRoot+"/"+node;
		try {
			
				List<String> children = zko.getZk().getChildren(curNode, true);
				for (String child : children) {
					try {
						String value = new String(zko.getZk().getData(curNode + "/" + child, null, null));
						
						pros.setProperty(child, value);
                    } catch (Exception e) {
                    	log.error("Read property(key:{"+child+"}) error");
                    	log.error("Exception:",e);
                    }
				}
			
		} catch (KeeperException e) {
			log.error("Failed to get property from zk server" + zkHost, e);
			throw new RuntimeException(
					"Failed to get property from zk server" + zkHost, e);
		} catch (InterruptedException e) {
			log.error("Failed to get property from zk server" + zkHost, e);
			throw new RuntimeException(
					"Failed to get property from zk server" + zkHost, e);
		} finally {
			
		}
		
		Properties overProps=queryOverrideLocation();
		
		if(overProps!=null){
			//将扩展的properties信息覆盖zookeeper获取的属性
			copyProperties(pros,overProps);
		}
		
		return pros;
	}

	/**
	 * Template method that subclasses may override to construct the object
	 * returned by this factory. The default implementation returns the plain
	 * merged Properties instance.
	 * <p>
	 * Invoked on initialization of this FactoryBean in case of a shared
	 * singleton; else, on each {@link #getObject()} call.
	 * 
	 * @return the object returned by this factory
	 * @throws IOException
	 *             if an exception occured during properties loading
	 * @deprecated as of Spring 3.0, in favor of {@link #createProperties()}
	 */
	@Deprecated
	protected Object createInstance() throws IOException {
		return mergeProperties();
	}

	public String getOverrideLocation() {
		return overrideLocation;
	}

	public void setOverrideLocation(String overrideLocation) {
		this.overrideLocation = overrideLocation;
	}
	
	

}
