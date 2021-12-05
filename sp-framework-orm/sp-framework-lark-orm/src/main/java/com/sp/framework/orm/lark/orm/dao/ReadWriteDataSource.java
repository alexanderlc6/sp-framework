package com.sp.framework.orm.lark.orm.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.sp.framework.orm.lark.common.dao.ReadWriteSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

public class ReadWriteDataSource extends AbstractRoutingDataSource {
	private static final Logger logger = LogManager.getLogger(ShardingDataSource.class);

	private DataSource readDataSource;
	private DataSource writeDataSource;

	@Override
	public void afterPropertiesSet() {				
		Assert.notNull(readDataSource, "readDataSource can not be null");
		Assert.notNull(writeDataSource, "writeDataSource can not be null");
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		targetDataSources.put(ReadWriteSupport.READ, readDataSource);
		targetDataSources.put(ReadWriteSupport.WRITE, writeDataSource);
		this.setTargetDataSources(targetDataSources);
		this.setDefaultTargetDataSource(writeDataSource);
		super.afterPropertiesSet();
	}

	@Override
	protected Object determineCurrentLookupKey() {
		logger.debug("Current LookupKey:" + ReadWriteStatusHolder.getReadWriteStatus());
		if(ReadWriteStatusHolder.getReadWriteStatus() == null)
			return ReadWriteSupport.WRITE;
		return ReadWriteStatusHolder.getReadWriteStatus();
	}

	public DataSource getReadDataSource() {
		return readDataSource;
	}

	public void setReadDataSource(DataSource readDataSource) {
		this.readDataSource = readDataSource;
	}

	public DataSource getWriteDataSource() {
		return writeDataSource;
	}

	public void setWriteDataSource(DataSource writeDataSource) {
		this.writeDataSource = writeDataSource;
	}
}
