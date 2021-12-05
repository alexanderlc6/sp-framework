package com.sp.framework.redis.manager;

import com.sp.framework.utilities.SerializableUtil;
import com.sp.framework.redis.command.CacheExpiredCommand;
import com.sp.framework.redis.exception.CacheException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import java.io.Serializable;
import java.util.*;

public abstract class BaseCacheManagerImpl implements CacheManager {
	/**
	 * 默认情况下返回的filedName
	 */
	protected static final String DEFAULT_FIELD="default-field";

	protected CacheConfigManager cacheConfigManager;

	public CacheConfigManager getCacheConfigManager() {
		return cacheConfigManager;
	}

	public void setCacheConfigManager(CacheConfigManager cacheConfigManager) {
		this.cacheConfigManager = cacheConfigManager;
	}

	/**
	 * 配置不同环境不同项目的key前辍
	 */
	protected static final String REDIS_KEY_START="redis.keystart";

	/**
	 * 获取当前Jedis cluster实例
	 * @return
	 */
	public abstract JedisCluster getJedisCluster();

	/**
	 * 无论是key为null或是配置的key前辍为null,都返回key的值
	 * @param key
	 * @return
	 */
	protected String processKey(String key){
		String confKeyStart=null;
		if(cacheConfigManager!=null) {
			confKeyStart = cacheConfigManager.getKeyStart();
		}

		if(StringUtils.isBlank(key)||StringUtils.isBlank(confKeyStart)) {
			return key;
		}
		return confKeyStart+"_"+key;
	}

	/**
	 * 返回true表示会使用缓存
	 * @return
	 */
	protected boolean useCache(){
		boolean hasCache=true;
		if(cacheConfigManager!=null) {
			hasCache = cacheConfigManager.hasCache();
		}

		if(hasCache){
			return true;
		}

		return false;
	}

//	public List<String> Keys(String keyPattern) {
//
//		List<String> list=new ArrayList<String>();
//		if(!useCache()) return list;
//
//		/**
//		 * 如果用户传递的表达式为*,则直接返回空串
//		 */
//		if(StringUtils.isNotBlank(keyPattern)&&keyPattern.equals("*"))
//				return list;
//
//		keyPattern=processKey(keyPattern);
//		JedisCluster jredis = null;
//
//		try{
//			jredis=getJedisCluster();
//			Set<String> keySet=jredis.keys(keyPattern);
//
//			Iterator<String> itera=keySet.iterator();
//
//			while(itera.hasNext()){
//				list.add(itera.next();
//			}
//
//		}
//		catch(Exception e){
//			
//			throw new CacheException(e);
//		}
//		finally{
//			returnResource(jredis);
//		}
//
//		return list;
//	}

	@Override
	public boolean removeKeys(String keyPattern) {
		if(!useCache()) return false;

		/**
		 * 如果用户传递的表达式为*,则直接返回空串
		 */
		if(StringUtils.isNotBlank(keyPattern)&&keyPattern.equals("*"))
				return false;

		keyPattern=processKey(keyPattern);
		JedisCluster jredis = null;

		try{
			jredis=getJedisCluster();
			Set<String> keySet=jredis.keys(keyPattern);

			Iterator<String> itera=keySet.iterator();

			while(itera.hasNext()){
				jredis.del(itera.next());
			}
			return true;

		}
		catch(Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public void setValue(String key,String value,Integer expireSeconds) {
		if(!useCache()) return;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			jredis.setex(key, expireSeconds, value);

		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	@Override
	public void setValue(String key,String value){
		if(!useCache()) return;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			jredis.set(key, value);
		}
		catch(Exception e){
			throw new CacheException(e);
		}
	}

	@Override
	public Long remove(String key){
		if(!useCache()) return null;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.del(key);
		}
		catch(Exception e){
			throw new CacheException(e);
		}
	}

	@Override
	public String getValue(String key){
		if(!useCache()) return null;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.get(key);
		}
		catch(Exception e){
			throw new CacheException(e);
		}
	}

	@Override
	public Map<String,String> getAllMap(String key){
		if(!useCache()) return null;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.hgetAll(key);
		}
		catch(Exception e){
			throw new CacheException(e);
		}
	}

	@Override
	public void pushToListHead(String key,String[] values){
		if(!useCache()) return ;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			jredis.lpush(key, values);
		}
		catch(Exception e){
			throw new CacheException(e);
		}
	}

	@Override
	public void pushToListHead(String key,String value){
		if(!useCache()) return ;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			jredis.lpush(key, value);
		}catch (Exception e){
			throw new CacheException(e);
		}
	}

	@Override
	public void pushToListFooter(String key,String[] values){
		if(!useCache()) return ;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			jredis.rpush(key, values);
		}
		catch(Exception e){
			throw new CacheException(e);
		}
	}

	@Override
	public void pushToListFooter(String key,String value){
		if(!useCache()) return ;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			jredis.rpush(key, value);
		}
		catch(Exception e){
			throw new CacheException(e);
		}
	}

	@Override
	public String popListHead(String key){
		if(!useCache()) return null;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.lpop(key);
		}
		catch(Exception e){
			throw new CacheException(e);
		}
	}

	@Override
	public String popListFooter(String key){
		if(!useCache()) return null;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.rpop(key);
		}
		catch(Exception e){
			throw new CacheException(e);
		}
	}

	@Override
	public String findListItem(String key,long index){
		if(!useCache()) return null;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.lindex(key, index);
		}
		catch(Exception e){
			throw new CacheException(e);
		}
	}

	public List<String> findLists(String key,long start,long end){
		if(!useCache()) return null;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.lrange(key, start, end);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}

	}

	public long listLen(String key){
		if(!useCache()) return 0;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.llen(key);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	public void addSet(String key,String[] values){
		if(!useCache()) return ;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			jredis.sadd(key, values);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	public String popSet(String key){
		if(!useCache()) return null;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.spop(key);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	public boolean existsInSet(String key,String member){
		if(!useCache()) return false;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.sismember(key, member);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	public Set<String> findSetAll(String key){
		if(!useCache()) return null;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.smembers(key);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	public long findSetCount(String key){
		if(!useCache()) return 0;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.scard(key);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}

	}

	@Override
	public <T> void setObject(String key, T t) {
		if(!useCache()) return ;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			String value=SerializableUtil.convert2String((Serializable)t);
			//setValue(key, value);
			jredis.set(key, value);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	@Override
	public <T> void setObject(String key, T t, Integer expireSeconds) {
		if(!useCache()) return ;

		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			String value= SerializableUtil.convert2String((Serializable)t);

			//setValue(key, value,expireSeconds);
			jredis.setex(key, expireSeconds, value);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getObject(String key) {
		if(!useCache()) return null;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis = getJedisCluster();
			String value=jredis.get(key);
			if(StringUtils.isBlank(value)){
				return null;
			}
			return (T)SerializableUtil.convert2Object(value);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	public void addSortSet(String key,String value,long sortNo){
		if(!useCache()) return ;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			jredis.zadd(key, sortNo, value);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}

	}

	public Set<String> findSortSets(String key,long start,long end){
		if(!useCache()) return null;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.zrange(key, start, end);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	public long findSortSetCount(String key){
		if(!useCache()) return 0;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.zcard(key);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	public long findSortSetCount(String key,long min,long max){
		if(!useCache()) return 0;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.zcount(key, min, max);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	@Override
	public void removeMapValue(String key, String field) {
		if(!useCache()) return ;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			jredis.hdel(key, field);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	public void setMapValue(String key,String field,String value ,int seconds){
		if(!useCache()) return ;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();

			CacheExpiredCommand<String> cec=new CacheExpiredCommand<String>();

			cec.setObject(value);
			cec.setExpiredTime(System.currentTimeMillis()+seconds*1000l);

			jredis.hset(key, field, SerializableUtil.convert2String((Serializable)cec));
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public String getMapValue(String key,String field){
		if(!useCache()) return null;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			String value=jredis.hget(key, field);

			if(StringUtils.isBlank(value)){
				return null;
			}
			CacheExpiredCommand<String> cec=(CacheExpiredCommand<String>)SerializableUtil.convert2Object(value);

			if(System.currentTimeMillis()<cec.getExpiredTime())
				return cec.getObject();
			else
				return null;
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	@Override
	public <T> void setMapObject(String key, String field,T t,int seconds) {
		if(!useCache()) return ;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();

			CacheExpiredCommand<T> cec=new CacheExpiredCommand<T>();

			cec.setObject(t);
			cec.setExpiredTime(System.currentTimeMillis()+seconds*1000l);
			String value=SerializableUtil.convert2String((Serializable)cec);
			jredis.hset(key, field, value);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getMapObject(String key, String field) {
		
		if(!useCache()) return null;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			String value=jredis.hget(key, field);
			if(StringUtils.isBlank(value)){
				return null;
			}
			CacheExpiredCommand<T> cec=(CacheExpiredCommand<T>)SerializableUtil.convert2Object(value);
			if(System.currentTimeMillis()<cec.getExpiredTime())
				return (T)cec.getObject();
			else
				return null;
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	@Override
	public String generateMapFieldByDefault(Object... objArray) {
		
		StringBuffer sb=new StringBuffer();

		for(Object obj:objArray){
			sb.append(obj.toString()+"-");
		}
		if(sb.length()>0){
			sb=sb.delete(sb.length()-1, sb.length());
		}
		else{
			sb.append(DEFAULT_FIELD);

		}
		return sb.toString();
	}


	@Override
	public long decrBy(String key, Integer count) {
		
		if(!useCache()) throw new CacheException("cache off");
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.decrBy(key, count);

		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}


	@Override
	public long decr(String key) {
		
		if(!useCache()) throw new CacheException("cache off");
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.decr(key);

		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}


	@Override
	public long incrBy(String key, Integer count) {
		
		if(!useCache()) throw new CacheException("cache off");
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.incrBy(key, count);

		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}


	@Override
	public long incr(String key) {
		
		if(!useCache()) throw new CacheException("cache off");
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.incr(key);

		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public String convertMapValue(String mapValueStr) {
		CacheExpiredCommand<String> cec=(CacheExpiredCommand<String>)SerializableUtil.convert2Object(mapValueStr);

		if(System.currentTimeMillis()<cec.getExpiredTime())
			return cec.getObject();
		else
			return null;


	}


	public void expire(String key,Integer expireSeconds) {
		if(!useCache()) return;
		key=processKey(key);
		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			jredis.expire(key, expireSeconds);

		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}


	@Override
	public void setValueByFullKey(String key, String value) {
		
		if(!useCache()) return;

		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			jredis.set(key, value);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}


	@Override
	public void setValueByFullKey(String key, String value, Integer expireSeconds) {
		
		if(!useCache()) return;

		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			jredis.setex(key, expireSeconds, value);

		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}


	@Override
	public String getValueByFullKey(String key) {
		
		if(!useCache()) return null;

		JedisCluster jredis = null;
		try{
			jredis=getJedisCluster();
			return jredis.get(key);
		}
		catch(Exception e){
			
			throw new CacheException(e);
		}
	}

	@Override
	public boolean tryLock(String key, int expire) {
		return tryLock(key, expire, 0L);
	}

	@Override
	public boolean tryLock(String key, int expire, long timeout) {
		final long begin = System.currentTimeMillis();
		if (timeout > expire * 1000L) {
			timeout = expire * 1000L;
		}
		final long end = begin + timeout;
		boolean result;
		while (!(result = lock(key, expire))) {
			if (System.currentTimeMillis() >= end) {
				break;
			}
			try {
				// 不要过于霸道长时间占用CPU
				Thread.sleep(5L);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		return result;
	}

	/**
	 *
	 * @param key
	 * @param expire
	 * @return
	 */
	protected boolean lock(String key, int expire) {
		if (key == null || !useCache()) {
			return false;
		}
		key = processKey(key);
		JedisCluster jedis = null;
		try {
			jedis = getJedisCluster();
			String script = "if redis.call('setnx', KEYS[1], KEYS[2]) == 1 then\n"
					+ "return redis.call('expire', KEYS[1], KEYS[3]);\n" + "end\n" + "return nil;";
            // String result = jedis.set(key, ".", "NX", "PX", expire);
            // 执行LUA脚本保证原子性
            Object result = jedis.eval(script, 3, key, ".", String.valueOf(expire));
			return result != null && result.equals(1L);
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public boolean releaseLock(String key) {
		long released;
		if (!useCache()) {
			return false;
		}
		key = processKey(key);
		JedisCluster jedis = null;
		try {
			jedis = getJedisCluster();
			released = jedis.del(key);
		} catch (Exception e) {
			throw new CacheException(e);
		}
		
		return released >= 1L;
	}

	public Long remSet(String key, String... members) {
		if (!useCache()) {
			return null;
		}
		key = processKey(key);
		JedisCluster jedis = null;
		try {
			jedis = getJedisCluster();
			return jedis.srem(key, members);
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public Long getExpire(String key) {
		if (!useCache()) {
			return null;
		}
		key = processKey(key);
		JedisCluster jedis = null;
		try {
			jedis = getJedisCluster();
			return jedis.pttl(key);
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public void setValue(String key, String value, Long mills) {
		if (!useCache()) {
			return;
		}
		key = processKey(key);
		JedisCluster jedis = null;
		try {
			jedis = getJedisCluster();
			jedis.psetex(key, mills.intValue(), value);

		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public List<String> getValues(String... keys) {
		List<String> list = new ArrayList<>();
		if (keys == null || keys.length == 0) {
			return list;
		}
		if (!useCache()) {
			return list;
		}
		int batch = 1000;
		int length = keys.length;
		String keyStart;
		if (cacheConfigManager != null) {
			keyStart = cacheConfigManager.getKeyStart() + "_";
		} else {
			keyStart = "";
		}
		JedisCluster jedis = null;
		try {
			jedis = getJedisCluster();
			int loop = length % batch == 0 ? length / batch : length / batch + 1;
			String[] array;
			for (int i = 0; i < loop; i++) {
				if (i < loop - 1) {
					array = new String[batch];
				} else {
					array = new String[length - (loop - 1) * batch];
				}
				for (int j = 0; j < array.length; j++) {
					array[j] = keyStart + keys[i * batch + j];
				}
				list.addAll(jedis.mget(array));
			}
			return list;
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public boolean setAndLock(String key, String value) {
		if (!useCache()) {
			return false;
		}
		key = processKey(key);
		JedisCluster jedis = null;
		try {
			jedis = getJedisCluster();
			long affected = jedis.setnx(key, value);
			if (affected == 1) {
				return true;
			}
		} catch (Exception e) {
			throw new CacheException(e);
		}
		return false;
	}
}
