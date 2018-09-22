package com.sy.server.app.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyPeopleInformation;
import com.sy.server.app.service.SyPeopleInformationService;
import com.sy.server.mapper.SyPeopleInformationMapper;
@Service
public class SyPeopleInformationServiceImpl implements SyPeopleInformationService{
	@Autowired
	private SyPeopleInformationMapper syPeopleInformationMapper;

	public List<SyPeopleInformation> selectBySyPeopleInformations(MapUtil maputil) {
		// TODO Auto-generated method stub
		return syPeopleInformationMapper.selectByDetailItem(maputil);
	}
	
	public SyPeopleInformation selectByPrimaryKey(Integer tSyPeopleInformationId) {
		// TODO Auto-generated method stub
		return syPeopleInformationMapper.selectByPrimaryKey(tSyPeopleInformationId);
	}

	public List<Map> selectBySyPeopleInformationsMap(MapUtil maputil) {
		// TODO Auto-generated method stub
		return syPeopleInformationMapper.selectBySyPeopleInformationsMap(maputil);
	}


}
