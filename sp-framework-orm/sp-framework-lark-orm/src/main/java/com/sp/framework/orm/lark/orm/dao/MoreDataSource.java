package com.sp.framework.orm.lark.orm.dao;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

/**
 * 多数据源
 * @author Alex Lu
 *
 */
public class MoreDataSource extends AbstractRoutingDataSource {
	private static final Logger logger = LogManager.getLogger(MoreDataSource.class);

	private Map<Object,Object> dataSourceMap;
	
	private String defaultDataSourceKey;
	
	@Override
	public void afterPropertiesSet() {				
		Assert.notNull(dataSourceMap, "DataSourceMap can not be null");
		Assert.notNull(dataSourceMap.get(defaultDataSourceKey), "dataSourceMap can not be null");

		this.setTargetDataSources(dataSourceMap);
		this.setDefaultTargetDataSource(dataSourceMap.get(defaultDataSourceKey));
		super.afterPropertiesSet();
	}

	@Override
	protected Object determineCurrentLookupKey() {
		logger.debug("Current LookupKey:" + MoreStatusHolder.getMoreDataSourceStatus());
		
		if(MoreStatusHolder.getMoreDataSourceStatus() == null) {
			//return dataSourceMap.get(defaultDataSourceKey);
			//返回key
			return defaultDataSourceKey;
		}

		return MoreStatusHolder.getMoreDataSourceStatus();
	}

	public Map<Object, Object> getDataSourceMap() {
		return dataSourceMap;
	}

	public void setDataSourceMap(Map<Object, Object> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	public String getDefaultDataSourceKey() {
		return defaultDataSourceKey;
	}

	public void setDefaultDataSourceKey(String defaultDataSourceKey) {
		this.defaultDataSourceKey = defaultDataSourceKey;
	}
}
