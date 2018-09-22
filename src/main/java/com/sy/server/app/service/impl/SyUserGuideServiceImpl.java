package com.sy.server.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyUserGuide;
import com.sy.server.app.service.SyUserGuideService;
import com.sy.server.mapper.SyUserGuideMapper;
/**
 * server 层实现类
 * @author wangliang
 *
 */
@Service
public class SyUserGuideServiceImpl implements SyUserGuideService {
	
	@Autowired
	private SyUserGuideMapper syUserGuideMapper;

	public SyUserGuide query(Integer id) {
		// TODO Auto-generated method stub
		return syUserGuideMapper.selectByPrimaryKey(id);
	}

	public int insert(SyUserGuide u) {
		// TODO Auto-generated method stub
		return syUserGuideMapper.insert(u);
	}

	public List<SyUserGuide> selectByItem(MapUtil maputil) {
		// TODO Auto-generated method stub
		return syUserGuideMapper.selectByItem(maputil);
	}
	
	

}
