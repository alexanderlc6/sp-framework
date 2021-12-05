package com.sp.framework.orm.lark.tool.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;

public class ProfileConfigUtil {
	

	/**
	 * 当前模式(dev,test,production)
	 */
	private static String mode="null";
	
	/**
	 * getProfilePath方法中会对此路径进行处理，加上profile的路径
	 * 如 
	 * 原路径为：config/abc.properties
	 * 返回：config/dev/abc.properties
	 */
	private static final String[] filterPath={"config/"};
	
	/**
	 * 用于缓存查询过的配置文件
	 */
	private static Map<String,Properties> proMap=new HashMap<String,Properties>();
	

	public static void setMode(String m){
		
		mode=m;
	}
	
	public static String getMode(){
		return mode;
	}
	
	private static void checkModel(){
		if(StringUtils.isBlank(mode)||mode.equalsIgnoreCase("null")){
			throw new RuntimeException("profile获取在初始化之前，这是不被允许的！请联系NEBULA的相关人员！");
		}
	}
	
	/**
	 * 获取profile对应的路径
	 * @param source
	 * @return
	 */
	public static String getProfilePath(String source){
		checkModel();
		//source与mode都不为空
		if(StringUtils.isNotBlank(source) &&StringUtils.isNotBlank(mode)){
			for(String path:filterPath){
				//必须以此字符为前辍
				if(source.startsWith(path)){
					return source.replaceFirst(path, path+mode+"/");
				}
			}
		}
		
		return source;
	}
	
	/**
	 * 通过路径返回properties文件,会进行profile处理
	 * @param source
	 * @return
	 */
	public static Properties findPro(String source){
		checkModel();
		String path=getProfilePath(source);

		Properties pro=proMap.get(path);
		if(pro==null){
		
			InputStream is = ResourceUtil.getResourceAsStream(
					path, ProfileConfigUtil.class);
			
			 pro=new Properties();
			try {
				pro.load(is);
				proMap.put(path, pro);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return pro;
	}
	
	/**
	 * 通过路径返回properties文件,不会进行profile路径处理
	 * @param source
	 * @return
	 */
	public static Properties findCommonPro(String source){
		
		String path=source;
		
		Properties pro=proMap.get(path);
		if(pro==null){
		
			InputStream is = ResourceUtil.getResourceAsStream(
					path, ProfileConfigUtil.class);
			
			 pro=new Properties();
			try {
				pro.load(is);
				proMap.put(path, pro);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return pro;
	}
	
	/**
	 * 通过spring的resource获取属性
	 * @param resource
	 * @return
	 */
	public static Properties findResourcePro(Resource resource){
	
			
		Properties pro=new Properties();
			try {
				pro.load(resource.getInputStream());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		
		return pro;
	}
	
	public static void main(String[] args){
		mode="dev";
		String str="config/asdfas/config/test";
		
		System.out.println(getProfilePath(str));
		
		findPro(str);
		
		findCommonPro(str);
	}
}
