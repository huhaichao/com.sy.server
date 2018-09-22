package com.sy.server.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyVersion;
import com.sy.server.app.service.SyVersionService;
import com.sy.server.mapper.SyVersionMapper;
/**
 * server 层实现类
 * @author wangliang
 *
 */
@Service
public class SyVersionServiceImpl implements SyVersionService {
	
	@Autowired
	private SyVersionMapper versionMapper;
	
	/**
	 * 不加 @TargetDataSource 采用默认数据源
	 */
	public SyVersion query(Integer id) {
		return versionMapper.selectByPrimaryKey(id);
	}
    
	/**
	 * 不加 @TargetDataSource 采用默认数据源
	 *  @Transactional  添加事物，单表操作一般不需要添加事务
	 */
	public int insert(SyVersion u) {
		return versionMapper.insert(u);
	}

	public List<SyVersion> selectByItem(MapUtil maputil) {
		// TODO Auto-generated method stub
		return versionMapper.selectByItem(maputil);
	}
	
	

}
