package com.sy.server.app.service;

import java.util.List;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyPeopleInformation;

/**
 * server 层接口
 * @author yumeng
 *
 */
public interface SyPeopleService {

	public List<SyPeopleInformation> selectByItem(MapUtil maputil);
	
	public List<SyPeopleInformation> selectByDetailItem(MapUtil maputil);
	
	public int insert(SyPeopleInformation record);
	
	public int updateByPrimaryKey(SyPeopleInformation record);
	
	public SyPeopleInformation selectByPrimaryKey(Integer tSyPeopleInformationId);
	
	public SyPeopleInformation selectByMessageId(MapUtil maputil);
	
	public int deleteByPrimaryKey(Integer tSyMessageId);
	/**
	 * 功能:通过丢失人员信息主键与发布消息类型 获取 人员详细信息   
	 * 疑问  主键就可以定位到相关信息，不需要发布类型确认？
	 * wangliang 
	 * @param maputil
	 * @return
	 */
	public SyPeopleInformation  selectByIdAndType(MapUtil maputil);

}
