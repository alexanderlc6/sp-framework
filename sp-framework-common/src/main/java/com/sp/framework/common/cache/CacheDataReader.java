package com.sp.framework.common.cache;

import java.util.List;
import java.util.Map;

/**
 * 缓存数据读取接口
 *
 * @author Alex Lu
 * @date 2020/4/17
 */
public interface CacheDataReader {
    /**
     * 根据缓存Key读取缓存数据对象
     * @return
     */
    DataObject getCacheObjectByKey(String cacheKey);

    /**
     * 根据缓存Value读取对应的缓存Key
     * @return
     */
    String getCacheKeyByValue(Object cacheValue);

    /**
     * 根据缓存Key集合读取缓存数据对象
     * @return
     */
    Map<String, DataObject> getCacheObjectByKeyList(List<String> cacheKeyList);

    /**
     * 获取缓存数据Map
     * @return
     */
    Map<String, Object> getCacheDataMap();

    /**
     * 缓存单条数据
     * @param key
     * @param value
     */
    void put(String key, DataObject value);

    /**
     * 缓存集合数据
     * @param cacheDataMap
     */
    void putWithMap(Map<String, DataObject> cacheDataMap);

    /**
     * 检查(手动/同步加载的)缓存是否为空
     * @return
     */
    boolean checkCacheEmpty();

    /**
     * 设置缓存Key过期
     * @param key
     */
    void removeKey(String key);
}
