package com.sy.server.app.service;

import java.util.List;
import java.util.Map;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyItemInformation;


/**
 * server 层接口
 * @author yumeng
 *
 */
public interface SyItemService {

	public List<SyItemInformation> selectByItem(MapUtil maputil);
	public List<SyItemInformation> selectByDetailItem(MapUtil maputil);
	public int updateByPrimaryKey(SyItemInformation record);
	public int insert(SyItemInformation record);
	public SyItemInformation selectByPrimaryKey(Integer tSyItemInformationId);
	/**
	 * @author wangliang
	 * @date 2017-3-18
	 * @param maputil
	 * @return SyItemInformation
	 */
	public SyItemInformation selectByMessageId(MapUtil maputil);
	
	public int deleteByPrimaryKey(Integer tSyMessageId);
	/**
	 * @author wangliang
	 * @date 2017-05-29
	 * @param maputil
	 * @return
	 */
	public SyItemInformation selectByIdAndType(MapUtil maputil);
	public List<Map> selectByItemMap(MapUtil maputil);
	
	/**
	 * 根据条件删除相关信息
	 * @param maputil
	 * @return
	 */
	public int delSyItemInfomation(MapUtil maputil);


}
