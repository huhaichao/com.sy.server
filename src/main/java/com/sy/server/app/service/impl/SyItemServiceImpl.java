package com.sy.server.app.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyItemInformation;
import com.sy.server.app.service.SyItemService;
import com.sy.server.mapper.SyItemInformationMapper;
/**
 * server 层实现类
 * @author yumeng
 *
 */
@Service
public class SyItemServiceImpl implements SyItemService {
	
	@Autowired
	private SyItemInformationMapper syiteminformationmapper;
	

	public List<SyItemInformation> selectByItem(MapUtil maputil){
		return syiteminformationmapper.selectByItem(maputil);
	}
	
	public List<SyItemInformation> selectByDetailItem(MapUtil maputil){
		return syiteminformationmapper.selectByDetailItem(maputil);
	}

	public int updateByPrimaryKey(SyItemInformation record){
		return syiteminformationmapper.updateByPrimaryKey(record);
	};
	
	public int insert(SyItemInformation record){
		return syiteminformationmapper.insert(record);
	};
	
	public SyItemInformation selectByPrimaryKey(Integer tSyItemInformationId){
		return syiteminformationmapper.selectByPrimaryKey( tSyItemInformationId);
	}

	public SyItemInformation selectByMessageId(MapUtil maputil) {
		// TODO Auto-generated method stub
		 List<SyItemInformation> list = syiteminformationmapper.selectByItem( maputil);
		 return list ==null ? null :list.get(0);
	}

	public int deleteByPrimaryKey(Integer tSyItemInformationId) {
		// TODO Auto-generated method stub
		return syiteminformationmapper.deleteByPrimaryKey(tSyItemInformationId);
	}

	public SyItemInformation selectByIdAndType(MapUtil maputil) {
		// TODO Auto-generated method stub
		return syiteminformationmapper.selectByIdAndType(maputil);
	}

	public List<Map> selectByItemMap(MapUtil maputil) {
		// TODO Auto-generated method stub
		return syiteminformationmapper.selectByItemMap(maputil);
	}

	public int delSyItemInfomation(MapUtil maputil) {
		// TODO Auto-generated method stub
		return syiteminformationmapper.delSyItemInfomation(maputil);
	}

	
	
	
}
