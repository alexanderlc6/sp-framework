package com.sp.framework.orm.lark.orm.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sp.framework.orm.lark.common.dao.ShardingSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;
/**
 * 水平拆分数据源
 * 包括一个公共数据源与多个拆分数据源
 * 拆分数据源的表结构一模一样
 * @author Alex Lu
 *
 */
public class ShardingDataSource extends AbstractRoutingDataSource {
	private static final Logger logger = LogManager.getLogger(ShardingDataSource.class);

	/**
	 * 数据源集
	 */
	private Map<Object,Object> dataSourceMap;

	
	/**
	 * 公共库数据源
	 */
	private Object commonDataSource;
	
	/**
	 * 拆分数据源的key列表
	 */
	private List<Object> keyList=new ArrayList<Object>();
	

	
	@Override
	public void afterPropertiesSet() {				
		Assert.notNull(dataSourceMap, "dataSourceMap can not be null");
		Assert.notNull(commonDataSource, "commonDataSource can not be null");

		Iterator<Object> iterator=dataSourceMap.keySet().iterator();
		
		while(iterator.hasNext()){
			keyList.add(iterator.next());
		}
		
		Collections.sort(keyList, new Comparator<Object>(){

			public int compare(Object o1, Object o2) {
				String s1=o1.toString();
				String s2=o2.toString();
				
				return s1.compareTo(s2);
			}
			
		});
		
		//拆分库、默认库的数据源加入进来
		Map<Object,Object> allDataSourceMap=new HashMap<Object,Object>();
		allDataSourceMap.put(ShardingSupport.COMMON,commonDataSource);
		allDataSourceMap.putAll(dataSourceMap);
		
		this.setTargetDataSources(allDataSourceMap);
		this.setDefaultTargetDataSource(commonDataSource);
		super.afterPropertiesSet();
	}

	/**
	 * 将数据源的key返回，用于数据源的切换
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		logger.debug("Current LookupKey:" + ShardingStatusHolder.getShardingDataSourceStatus());
		
		if(ShardingStatusHolder.getShardingDataSourceStatus() == null)
			//return dataSourceMap.get(defaultDataSourceKey);
			return ShardingSupport.COMMON;					//应该是返回key
		
		return ShardingStatusHolder.getShardingDataSourceStatus();
	}

	public Map<Object, Object> getDataSourceMap() {
		return dataSourceMap;
	}

	public void setDataSourceMap(Map<Object, Object> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	public Object getCommonDataSource() {
		return commonDataSource;
	}

	public void setCommonDataSource(Object commonDataSource) {
		this.commonDataSource = commonDataSource;
	}

	public List<Object> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<Object> keyList) {
		this.keyList = keyList;
	}
	


	

	

	
}
