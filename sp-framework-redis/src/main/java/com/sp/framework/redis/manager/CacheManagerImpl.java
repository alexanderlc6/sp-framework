package com.sp.framework.redis.manager;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

public class CacheManagerImpl extends BaseCacheManagerImpl implements CacheManager {
	
	/**
	 * 默认情况下返回的filedName
	 */
	protected static final String DEFAULT_FIELD="default-field";

	@Override
	public CacheConfigManager getCacheConfigManager() {
		return cacheConfigManager;
	}

	@Override
	public void setCacheConfigManager(CacheConfigManager cacheConfigManager) {
		this.cacheConfigManager = cacheConfigManager;
	}

	/**
	 * Jedis集群对象
	 */
	@Autowired
	JedisCluster jedisCluster;

	/**
	 * 获取当前Jedis cluster实例
	 * @return
	 */
	@Override
	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}

	/**
	 * 配置不同环境不同项目的key前辍
	 */
	protected static final String REDIS_KEY_START="redis.keystart";
}
