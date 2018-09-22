package com.sy.server.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyCreditBalance;
import com.sy.common.busibean.SyIntegral;
import com.sy.server.app.service.SyCreditBalanceService;
import com.sy.server.mapper.SyCreditBalanceMapper;
import com.sy.server.mapper.SyIntegralMapper;
/**
 * server 层实现类
 * @author wangliang
 *
 */
@Service
public class SyCreditBalanceServiceImpl implements SyCreditBalanceService {
	
	
	@Autowired
	private SyCreditBalanceMapper syCreditBalanceMapper;

	public SyCreditBalance query(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	public int insert(SyCreditBalance u) {
		// TODO Auto-generated method stub
		return syCreditBalanceMapper.insert(u);
	}

	public List<SyCreditBalance> selectByItem(MapUtil maputil) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SyCreditBalance> selectByDate(MapUtil maputil) {
		// TODO Auto-generated method stub
		return null;
	}

}
