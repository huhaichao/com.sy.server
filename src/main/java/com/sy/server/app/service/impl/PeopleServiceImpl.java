package com.sy.server.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyPeopleInformation;
import com.sy.server.app.service.PeopleService;
import com.sy.server.mapper.SyPeopleInformationMapper;
/**
 * server 层实现类
 * @author huchao
 *
 */
@Service
public class PeopleServiceImpl implements PeopleService {
	
	@Autowired
	private SyPeopleInformationMapper sypeopleinformationmapper;
	

	public List<SyPeopleInformation> selectByItem(MapUtil maputil){
		return sypeopleinformationmapper.selectByItem(maputil);
	}
	
	public List<SyPeopleInformation> selectByDetailItem(MapUtil maputil){
		return sypeopleinformationmapper.selectByDetailItem(maputil);
	}

	public SyPeopleInformation selectByMessageId(MapUtil maputil) {
		// TODO Auto-generated method stub
		return sypeopleinformationmapper.selectByMessageId(maputil);
	}

	public SyPeopleInformation selectByPrimaryKey(Integer tSyPeopleInformationId) {
		// TODO Auto-generated method stub
		return sypeopleinformationmapper.selectByPrimaryKey(tSyPeopleInformationId);
	}

	public int insert(SyPeopleInformation record) {
		// TODO Auto-generated method stub
		return sypeopleinformationmapper.insert(record);
	}

	public int updateByPrimaryKey(SyPeopleInformation record) {
		// TODO Auto-generated method stub
		return sypeopleinformationmapper.updateByPrimaryKey(record);
	}

	public int deleteByPrimaryKey(Integer tSyPeopleInformationId) {
		// TODO Auto-generated method stub
		return sypeopleinformationmapper.deleteByPrimaryKey(tSyPeopleInformationId);
	}

	

}
