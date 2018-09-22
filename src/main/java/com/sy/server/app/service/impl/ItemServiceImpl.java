package com.sy.server.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyItemInformation;
import com.sy.server.app.service.ItemService;
import com.sy.server.mapper.SyItemInformationMapper;
/**
 * server 层实现类
 * @author huchao
 *
 */
@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private SyItemInformationMapper syiteminformationmapper;
	

	public List<SyItemInformation> selectByItem(MapUtil maputil){
		return syiteminformationmapper.selectByItem(maputil);
	}
	
	public List<SyItemInformation> selectByDetailItem(MapUtil maputil){
		return syiteminformationmapper.selectByDetailItem(maputil);
	}


}
