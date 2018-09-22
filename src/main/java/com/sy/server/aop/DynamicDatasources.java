package com.sy.server.aop;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.sy.common.pools.DataSourceRegister;

/**
 * 动态切换数据源
 * @author huchao
 *
 */
public class DynamicDatasources extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceRegister.getDataSources();
	}

}
