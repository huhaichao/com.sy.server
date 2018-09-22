package com.sy.server.app.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyPeopleInformation;
import com.sy.server.app.service.SyPeopleService;
import com.sy.server.mapper.SyPeopleInformationMapper;
/**
 * server 层实现类
 * @author yumeng
 *
 */
@Service
public class SyPeopleServiceImpl implements SyPeopleService {
	
	@Autowired
	private SyPeopleInformationMapper sypeopleinformationmapper;
	

	public List<SyPeopleInformation> selectByItem(MapUtil maputil){
		return sypeopleinformationmapper.selectByItem(maputil);
	}
	
	public List<SyPeopleInformation> selectByDetailItem(MapUtil maputil){
		return sypeopleinformationmapper.selectByDetailItem(maputil);
	}

	public int insert(SyPeopleInformation record){
		return sypeopleinformationmapper.insert(record);
	};
	
	public int updateByPrimaryKey(SyPeopleInformation record){
		return sypeopleinformationmapper.updateByPrimaryKey(record);
	}

	public SyPeopleInformation selectByPrimaryKey(Integer tSyPeopleInformationId) {
		// TODO Auto-generated method stub
		return sypeopleinformationmapper.selectByPrimaryKey(tSyPeopleInformationId);
	}

	public SyPeopleInformation selectByMessageId(MapUtil maputil) {
		// TODO Auto-generated method stub
		return sypeopleinformationmapper.selectByMessageId(maputil);
	}

	public int deleteByPrimaryKey(Integer tSyPeopleInformationId) {
		// TODO Auto-generated method stub
		return sypeopleinformationmapper.deleteByPrimaryKey(tSyPeopleInformationId);
	}
	/**
	 * wangliang  通过丢失人员信息主键与发布消息类型 获取 人员详细信息   
	 */
	public SyPeopleInformation selectByIdAndType(MapUtil maputil) {
		// TODO Auto-generated method stub
		return sypeopleinformationmapper.selectByIdAndType(maputil);
	}

	
	
	
}
