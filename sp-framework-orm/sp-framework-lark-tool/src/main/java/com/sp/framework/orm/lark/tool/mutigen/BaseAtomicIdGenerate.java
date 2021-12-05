package com.sp.framework.orm.lark.tool.mutigen;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单个原子的ID生成
 * 因为考虑到性能,所以使用24个桶做并发处理，每个桶一次只能处理一个，大大增强了并发处理瓶颈
 * @author alexlu
 *
 */
public abstract class BaseAtomicIdGenerate {

	protected static Map<String,Integer> lowIdMap=new ConcurrentHashMap<String,Integer>();
	
	/**
	 * 高位的map
	 */
	protected static Map<String,Integer> HighIdMap=new ConcurrentHashMap<String,Integer>();
	
	/**
	 * 获取高位的manager,这里的功能一位由项目自己本身来实现
	 */
	protected MutiIdHighNumGenerateManager mutiIdHighNumGenerateManager;
	
	/**
	 * 初始低位序列值 
	 */
	protected Integer initLowNum=1000;
	
	/**
	 * 最大低位充列值
	 */
	protected Integer maxLowNum=9999;
	
	
	public BaseAtomicIdGenerate(MutiIdHighNumGenerateManager mhnm,Integer iln,Integer mln){
		this.mutiIdHighNumGenerateManager=mhnm;
		this.initLowNum=iln;
		this.maxLowNum=mln;
	}
	
	/**
	 * 重新加载高位
	 * @param clazz
	 * @param mid
	 */
	protected void reloadHighNum(String classPath,String mid){
		String key=classPath+"-"+mid;
		int high=mutiIdHighNumGenerateManager.queryHighNum(classPath, mid);
		HighIdMap.put(key, high);
	}
	
	/**
	 * 获取高位数值
	 * @param key
	 * @return
	 */
	protected Integer queryHighNum(String classPath,String mid){
		String key=classPath+"-"+mid;
		
		if(!HighIdMap.containsKey(key)){
			reloadHighNum(classPath, mid);
		}
		
		return HighIdMap.get(key);
	}
	

	
	/**
	 * 获取低位数值
	 * @param key
	 * @return
	 */
	protected Integer count(String classPath,String mid){
		
		String key=classPath+"-"+mid;
		int count=initLowNum;
		//如果已经存在，则count+1,因为保存的是上一次的数据，所以这次要+1
		if(lowIdMap.containsKey(key)){
			
			count=lowIdMap.get(key);
			count++;
		}
		
		//如果超过最大值，则更新高位num，并且重置低位count
		if(count>maxLowNum){
			reloadHighNum(classPath, mid);
			count=initLowNum;
		}
		
		lowIdMap.put(key, count);
		
		return count;
		
		
		
	}
	
	
	
}
