package com.sy.server.app.service;

import java.util.List;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyPeopleInformation;


/**
 * server 层接口
 * @author huchao
 *
 */
public interface PeopleService {

	public List<SyPeopleInformation> selectByItem(MapUtil maputil);
	
	public List<SyPeopleInformation> selectByDetailItem(MapUtil maputil);
	
	public SyPeopleInformation selectByMessageId(MapUtil maputil);

	public  int insert(SyPeopleInformation record);
	public  int updateByPrimaryKey(SyPeopleInformation record);
	public  SyPeopleInformation selectByPrimaryKey(Integer tSyPeopleInformationId);
	public int deleteByPrimaryKey(Integer tSyPeopleInformationId);
}
