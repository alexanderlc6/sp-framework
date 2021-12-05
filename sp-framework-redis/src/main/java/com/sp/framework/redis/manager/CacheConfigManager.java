package com.sp.framework.redis.manager;

public interface CacheConfigManager {

	/**
	 * 获取key的前辍，前辍用于解决不同的项目使用同一个redis环境的重名问题
	 * @return
	 */
	String getKeyStart();
	
	/**
	 * 缓存开关
	 * @return
	 */
	boolean hasCache();
}
