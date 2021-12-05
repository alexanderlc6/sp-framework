package com.sp.framework.utilities;

/**
 * 各种路径的处理
 * @author alexlu
 *
 */
public class PathUtil {

	
	/**
	 * 确保当前的source是一个目录，在前后加上/
	 * @param source
	 * @return
	 */
	public static String processFolder(String source){
		
		if(!source.startsWith("/")){
			source="/"+source;
		}
		
		
		if(!source.endsWith("/")){
			source=source+"/";
					
		}
		
		return source;
		
	}
	
	/**
	 * 获取最后一级目录
	 * 如 /ass/ddd/sss/
	 * 则当前值为 sss
	 * @param folder
	 * @return
	 */
	public static String findLastLevelFolder(String folder){
		
		if(folder.endsWith("/")){
			folder=folder.substring(0,folder.length()-1);
		}
		
		int lastIndex=folder.lastIndexOf("/");
		
		if(lastIndex!=-1&&lastIndex!=folder.length()-1){
			
			folder=folder.substring(lastIndex+1);
			
		}
		
		return folder;
		
	}
	
	public static void main(String[] args){
		
		System.out.println(PathUtil.findLastLevelFolder("/111/2222/"));
		
		System.out.println(PathUtil.findLastLevelFolder("/111//"));
		
		System.out.println(PathUtil.findLastLevelFolder("/111/2222"));
	}
}
