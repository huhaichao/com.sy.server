package com.sy.server.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyUser;
import com.sy.server.app.service.SyUserService;
import com.sy.server.mapper.SyUserMapper;
/**
 * server 层实现类
 * @author YUMENG
 *
 */
@Service
public class SyUserServiceImpl implements SyUserService {
	
	@Autowired
	private SyUserMapper userMapper;
	
	/**
	 * 不加 @TargetDataSource 采用默认数据源
	 */
	public SyUser query(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}
    
	/**
	 * 不加 @TargetDataSource 采用默认数据源
	 *  @Transactional  添加事物，单表操作一般不需要添加事务
	 */
	public int insert(SyUser u) {
		return userMapper.insert(u);
	}
	
	/**
	 * 根据手机号查询用户
	 */
	public List<SyUser> selectByPhone(MapUtil maputil){
		return userMapper.selectByPhone(maputil);
	}

	/**
	 * 更新用户信息
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKey(SyUser record){
		return userMapper.updateByPrimaryKey(record);
    }
	
	
    /**
     * 根据 id查询用户信息
     */
	public List<Map<String,Object>> queryLostAndPickup(Integer lostid, Integer pickupid) {
		// TODO Auto-generated method stub
		Map<String,Integer>  map = new HashMap<String,Integer>();
		
		map.put("lostUserId", lostid);
		map.put("pickupUserid", pickupid);
		
		return userMapper.selectLostAndPickById(map);
	};
    
}
