package com.sy.server.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyBrowse;
import com.sy.common.busibean.SyMessage;


/**
 * server 层接口
 * @author yumeng
 *
 */
public interface SyMessageService {
	public  int insert(SyMessage record);
	public  int updateByPrimaryKey(SyMessage record);
	public  SyMessage selectByPrimaryKey(Integer tSyMessageId);
	public int deleteByPrimaryKey(Integer tSyMessageId);
	public List<SyMessage> selectByItem(MapUtil maputil);
	public SyMessage selectByMesId(MapUtil maputil);
	
	public int insertByCollect(SyBrowse syBrowse);
	public int deleteByCollect(SyBrowse syBrowse);
	public List<HashMap<String, Object>> selectByItems(MapUtil maputil);
	/**
	 * 更新消息表标识
	 * @param mapUtil
	 * @return
	 */
    public int delMessage(MapUtil mapUtil);
    
    public List<Map> selectByItemMap(MapUtil maputil);
    public List<Map> selectBySyPeopleInformationsMap(MapUtil maputil);
    List<Map> selectByMessages(MapUtil maputil);
    List<Map> selectByBrowse(MapUtil maputil);
    
    List<Map> selectByItemMx(@Param("record")MapUtil maputil);
    

    List<Map> selectByPeopleMx(@Param("record")MapUtil maputil);
    
    
    Map<String,Object> selectByMessageIdFromView(Integer messageId);
    /**
     * 查询所有的消息信息 wl
     * @param maputil
     * @return
     */
	public List<SyMessage> getListMessages(MapUtil maputil);

    

}
