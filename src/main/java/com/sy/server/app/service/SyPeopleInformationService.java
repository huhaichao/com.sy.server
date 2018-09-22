package com.sy.server.app.service;
import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyPeopleInformation;

import java.util.List;
import java.util.Map;
public interface SyPeopleInformationService {
	/**
	 * 获取丢失人物数据信息数据
	 * @param maputil
	 * @return
	 */
	public List<SyPeopleInformation> selectBySyPeopleInformations(MapUtil maputil);
	/**
	 * 根据消息id获取数据信息
	 */
	public SyPeopleInformation selectByPrimaryKey(Integer tSyPeopleInformationId) ;
	/**
	 * 根据参数返回一个map数据集合
	 * @param maputil
	 * @return
	 */
	
	public List<Map> selectBySyPeopleInformationsMap(MapUtil maputil);
	
}