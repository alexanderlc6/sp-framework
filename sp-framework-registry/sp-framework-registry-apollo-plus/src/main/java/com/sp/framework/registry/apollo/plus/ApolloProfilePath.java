package com.sp.framework.registry.apollo.plus;


import com.sp.framework.registry.apollo.plus.utils.ReflectionUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ApolloProfilePath {
	public static final String DEFAULT_PROPERTIES="dev";
	
	/**
	 * 获取java -Dprofiles=xx 的参数值
	 */
	public static final String SYSTEM_PROPERTIES="profiles";

	public static void resetLocation(ApolloConfigurer zc) {
		Properties prop = System.getProperties();
		
		String profiles=(String)prop.get(SYSTEM_PROPERTIES);
		
		if(profiles==null) {
			profiles=DEFAULT_PROPERTIES;
		}
		
		Resource[] locations=new Resource[zc.getCustomLocations().size()];
		int i=0;
		
		PathMatchingResourcePatternResolver pmrpr=new PathMatchingResourcePatternResolver();
		for(String source:zc.getCustomLocations()) {
			
			source=source.replaceAll("\\[profile\\]", profiles);
			
			Resource dest=null;
			try {
				dest = pmrpr.getResources(source)[0];
			} catch (IOException e) {
				e.printStackTrace();
			};

			locations[i++]=dest;
		}

		ReflectionUtils.setFieldValue(zc, "locations",locations );
	}
	
	/**
	 * 重置overrideLocation的classpath路径
	 * @param zc
	 */
	public static void resetOverrideLocation(ApolloConfigurer zc) {
		Properties prop = System.getProperties();
		String profiles=(String)prop.get(SYSTEM_PROPERTIES);
		
		if(profiles==null) {
			profiles=DEFAULT_PROPERTIES;
		}
		List<String> list=new ArrayList<String>();
		
		for(String source:zc.getOverrideLocations()) {
			source=source.replaceAll("\\[profile\\]", profiles);
			list.add(source);
		}
		
		zc.setOverrideLocations(list);
	}
}
