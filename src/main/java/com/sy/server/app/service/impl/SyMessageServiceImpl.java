package com.sy.server.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyBrowse;
import com.sy.common.busibean.SyMessage;
import com.sy.server.app.service.SyMessageService;
import com.sy.server.mapper.SyBrowseMapper;
import com.sy.server.mapper.SyMessageMapper;

@Service
public class SyMessageServiceImpl implements SyMessageService{
	
	@Autowired
	private SyMessageMapper symessagemapper;
	
	@Autowired
	private SyBrowseMapper syBrowseMapper;
	
	public  int insert(SyMessage record){
		return symessagemapper.insert(record);
	};
	public  int updateByPrimaryKey(SyMessage record){
		return symessagemapper.updateByPrimaryKey(record);
	};
	public  SyMessage selectByPrimaryKey(Integer tSyMessageId){
		return symessagemapper.selectByPrimaryKey(tSyMessageId);
	};
	
	public int deleteByPrimaryKey(Integer tSyMessageId){
		return symessagemapper.deleteByPrimaryKey(tSyMessageId);
	};
	
	public List<SyMessage> selectByItem(MapUtil maputil){
		return symessagemapper.selectByItem(maputil);
	}
	public SyMessage selectByMesId(MapUtil maputil) {
		// TODO Auto-generated method stub
		return symessagemapper.selectByMesId(maputil);
	}
	public List<HashMap<String, Object>> selectByItems(MapUtil maputil) {
		// TODO Auto-generated method stub
		return symessagemapper.selectByItems(maputil);
	}

	
	public int insertByCollect(SyBrowse syBrowse){
		return syBrowseMapper.insert(syBrowse);
	}
	
	public int deleteByCollect(SyBrowse syBrowse){
		return syBrowseMapper.deleteByCollect(syBrowse);
	}
	public int delMessage(MapUtil mapUtil) {
		// TODO Auto-generated method stub
		return symessagemapper.delMessage(mapUtil);
	}
	public List<Map> selectByItemMap(MapUtil maputil) {
		// TODO Auto-generated method stub
		return symessagemapper.selectByItemMap(maputil);
	}
	public List<Map> selectBySyPeopleInformationsMap(MapUtil maputil) {
		// TODO Auto-generated method stub
		return symessagemapper.selectBySyPeopleInformationsMap(maputil);
	}
	public List<Map> selectByMessages(MapUtil maputil) {
		// TODO Auto-generated method stub
		return symessagemapper.selectByMessages(maputil);
	}
	public List<Map> selectByBrowse(MapUtil maputil) {
		// TODO Auto-generated method stub
		return symessagemapper.selectByBrowse(maputil);
	}
	public List<Map> selectByItemMx(MapUtil maputil) {
		// TODO Auto-generated method stub
		return symessagemapper.selectByItemMx(maputil);
	}
	public List<Map> selectByPeopleMx(MapUtil maputil) {
		// TODO Auto-generated method stub
		return symessagemapper.selectByPeopleMx(maputil);
	}
	public Map<String, Object> selectByMessageIdFromView(Integer messageId) {
		// TODO Auto-generated method stub
		return symessagemapper.selectByMessageIdFromView(messageId);
	}
	public List<SyMessage> getListMessages(MapUtil maputil) {
		// TODO Auto-generated method stub
		return symessagemapper.getListMessages(maputil);
	}
	
}
