package com.sy.server.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyIntegral;
import com.sy.server.app.service.SyCreditService;
import com.sy.server.mapper.SyCreditBalanceMapper;
import com.sy.server.mapper.SyIntegralMapper;
/**
 * server 层实现类
 * @author wangliang
 *
 */
@Service
public class SyCreditServiceImpl implements SyCreditService {
	
	@Autowired
	private SyIntegralMapper syIntegralMapper;
	@Autowired
	private SyCreditBalanceMapper syCreditBalanceMapper;
	/**
	 * 不加 @TargetDataSource 采用默认数据源
	 */
	public SyIntegral query(Integer id) {
		return syIntegralMapper.selectByPrimaryKey(id);
	}
    
	/**
	 * 不加 @TargetDataSource 采用默认数据源
	 *  @Transactional  添加事物，单表操作一般不需要添加事务
	 */
	public int insert(SyIntegral u) {
		return syIntegralMapper.insert(u);
	}
	/**
	 * 查找积分明细
	 */
	public List<SyIntegral> selectByItem(MapUtil maputil) {
		// TODO Auto-generated method stub
		return syIntegralMapper.selectByItem(maputil);
	}
	/**
	 * 查找用户的积分最新数据
	 */
	public  List<SyIntegral> selectByDate(MapUtil maputil) {
		// TODO Auto-generated method stub
		return syIntegralMapper.selectByDate(maputil);
	}

}
