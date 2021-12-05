package com.sp.framework.common.cache;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sp.framework.common.enums.SystemErrorCodeEnum;
import com.sp.framework.common.exception.SystemException;
import com.sp.framework.common.utils.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存数据读取接口实现类
 *
 * @author Alex Lu
 * @date 2020/4/17
 */

@Slf4j
public class DefaultLocalCacheDataReader implements CacheDataReader {
    private static DefaultLocalCacheDataReader singletonLocalCacheReader = null;

    /**
     * (手动/同步)缓存对象实例
     */
    private static Cache<String, DataObject> normalCacheInstance;

    /**
     * (异步)缓存对象实例
     */
    private AsyncLoadingCache<String, DataObject> asyncCacheInstance;

    /**
     * Caffeine缓存加载类型
     */
    private CaffeineCacheLoadType loadType = CaffeineCacheLoadType.MANUAL;

    /**
     * 最大缓存大小
     */
    private final static int MAX_CACHE_SIZE = 10000;

    /**
     * 写入缓存后超时时间
     */
    private final static int EXPIRE_TIME_AFTER_WRITE = 1;

    /**
     * 获取单例对象
     * @return
     */
    public static DefaultLocalCacheDataReader getInstance(){
        if(singletonLocalCacheReader != null){
            return singletonLocalCacheReader;
        }

        synchronized (DefaultLocalCacheDataReader.class){
            if(singletonLocalCacheReader == null){
                singletonLocalCacheReader = new DefaultLocalCacheDataReader(CaffeineCacheLoadType.MANUAL);
            }
        }

        return singletonLocalCacheReader;
    }

    public DefaultLocalCacheDataReader(CaffeineCacheLoadType loadType) {
        if(normalCacheInstance != null || asyncCacheInstance != null){
            return;
        }

        this.loadType = loadType;
        initCacheInstance();
    }

    public Cache<String, DataObject> getNormalCacheInstance() {
        return normalCacheInstance;
    }

    /**
     * 初始化本地缓存(Caffeine对象)
     */
    private void initCacheInstance() {
        switch (loadType) {
            case MANUAL:
                normalCacheInstance = Caffeine.newBuilder()
                        .maximumSize(MAX_CACHE_SIZE)
                        .expireAfterWrite(EXPIRE_TIME_AFTER_WRITE, TimeUnit.DAYS)
                        .build();
                break;
            case SYNC:
                //同步加载
                normalCacheInstance = Caffeine.newBuilder()
                        .maximumSize(MAX_CACHE_SIZE)
                        .expireAfterWrite(EXPIRE_TIME_AFTER_WRITE, TimeUnit.DAYS)
                            .build(key -> DataObject.get("Data for " + key));
                break;
            case ASYNC:
                //异步加载
                this.asyncCacheInstance = Caffeine.newBuilder()
                        .maximumSize(MAX_CACHE_SIZE)
                        .expireAfterWrite(EXPIRE_TIME_AFTER_WRITE, TimeUnit.DAYS)
                        .buildAsync(key -> DataObject.get("Data for " + key));
                break;
        }
    }

    /**
     * 缓存单条数据
     * @param key
     * @param value
     */
    @Override
    public void put(String key, DataObject value){
//        if(this.loadType.equals(CaffeineCacheLoadType.ASYNC)){
//            return asyncCacheInstance.put(key, new CompletableFuture(value));
//        }

        normalCacheInstance.put(key, value);
    }

    /**
     * 缓存集合数据
     * @param cacheDataMap
     */
    @Override
    public void putWithMap(Map<String, DataObject> cacheDataMap){
        if(CollectionUtil.isEmpty(cacheDataMap)){
            return;
        }

        normalCacheInstance.putAll(cacheDataMap);
    }

    /**
     * 根据缓存Key读取对应的缓存数据对象
     * @param cacheKey
     * @return
     */
    @Override
    public DataObject getCacheObjectByKey(String cacheKey){
//        if(this.loadType.equals(CaffeineCacheLoadType.ASYNC)){
//            return asyncCacheInstance.getIfPresent(cacheKey).thenAccept(t -> t.getData());
//        }

        return normalCacheInstance.getIfPresent(cacheKey);
    }

    /**
     * 根据缓存Value读取对应的缓存Key
     * @param cacheValue
     * @return
     */
    @Override
    public String getCacheKeyByValue(Object cacheValue){
        Map<String, DataObject> resultMap = normalCacheInstance.asMap();
        if(CollectionUtil.isEmpty(resultMap)){
            return null;
        }

        //遍历查找
        Optional<Map.Entry<String, DataObject>> keyOptional = resultMap.entrySet().stream()
                .filter(t -> String.valueOf(cacheValue).equals(t.getValue().getData()))
                .findAny();
        if(!keyOptional.isPresent()){
            return null;
        }

        return keyOptional.get().getKey();
    }

    /**
     * 获取缓存数据Map
     * @return
     */
    @Override
    public Map<String, Object> getCacheDataMap() {
        Map<String, DataObject> resultMap = normalCacheInstance.asMap();
        if(CollectionUtil.isEmpty(resultMap)){
            return null;
        }

        Map<String, Object> dataMap = new HashMap(16);
        resultMap.entrySet().stream().forEach(t -> {
            dataMap.put(t.getKey(), t.getValue().getData());
        });
        return dataMap;
    }

    /**
     * 根据缓存Key集合读取缓存数据对象集合
     * @return
     */
    @Override
    public Map<String, DataObject> getCacheObjectByKeyList(List<String> cacheKeyList){
        if(this.loadType.equals(CaffeineCacheLoadType.ASYNC)){
            CompletableFuture<Map<String, DataObject>> graphsResult = asyncCacheInstance.getAll(cacheKeyList);
            if(graphsResult != null){
                try {
                    return graphsResult.get();
                } catch (InterruptedException e) {
                    log.error("Read data from cache error while be interrupted:" + e.getStackTrace());
                    throw new SystemException(SystemErrorCodeEnum.SYSTEM_ERROR.getCode(),
                            SystemErrorCodeEnum.SYSTEM_ERROR.getMsg());
                } catch (ExecutionException e) {
                    log.error("Read data from cache error when execution:" + e.getStackTrace());
                    throw new SystemException(SystemErrorCodeEnum.IO_ERROR.getCode(),
                            SystemErrorCodeEnum.IO_ERROR.getMsg());
                }
            }
        }

        return normalCacheInstance.getAllPresent(cacheKeyList);
    }

    /**
     * 检查(手动/同步加载的)缓存是否为空
     * @return
     */
    @Override
    public boolean checkCacheEmpty(){
        if(normalCacheInstance == null || normalCacheInstance.estimatedSize() == 0){
            return true;
        }

        return false;
    }

    /**
     * 设置缓存Key过期
     * @param key
     */
    @Override
    public void removeKey(String key){
        normalCacheInstance.invalidate(key);
    }
}
