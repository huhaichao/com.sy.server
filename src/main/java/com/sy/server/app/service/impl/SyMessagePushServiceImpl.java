package com.sy.server.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyMessagePush;
import com.sy.server.app.service.SyMessagePushService;
import com.sy.server.mapper.SyMessagePushMapper;

@Service
public class SyMessagePushServiceImpl implements SyMessagePushService{

	@Autowired
	public SyMessagePushMapper symessagepushmapper;
	
	public  SyMessagePush selectByPrimaryKey(Integer tSyMessagePushId){
		return symessagepushmapper.selectByPrimaryKey(tSyMessagePushId);
	};
	public List<SyMessagePush> selectByItem(MapUtil maputil){
		return symessagepushmapper.selectByItem(maputil);
	};
	
	public int deleteByItem(MapUtil maputil){
		return symessagepushmapper.deleteByItem(maputil);
	}
	public int updateByPrimaryKey(SyMessagePush smp) {
		// TODO Auto-generated method stub
		return symessagepushmapper.updateByPrimaryKey(smp);
	}
	
}
