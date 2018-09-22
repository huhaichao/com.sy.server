package com.sy.server.app.service;

import java.util.List;
import java.util.Map;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyUser;


/**
 * server 层接口
 * @author yumeng
 *
 */
public interface SyUserService {
    /**
     * 按id查询实体
     * @param id
     * @return
     */
	public SyUser query(Integer id);
	
	
	/**
	 * 按id查询实体
	 * @param lostid //拾主id
	 * @param pickupid //失主id
	 * @return
	 */
	public List<Map<String,Object>> queryLostAndPickup(Integer lostid,Integer pickupid);
	
	/**
	 * 保存实体
	 * @param d
	 * @return
	 */
	public int  insert(SyUser  u);
	
	/**
	 * 
	 * @param maputil
	 * @return
	 */
	public List<SyUser> selectByPhone(MapUtil maputil);
	
	/**
	 * 更新用户信息
	 * @param record
	 * @return
	 */
    int updateByPrimaryKey(SyUser record);
    
	
}
