package com.sy.server.app.service;


import java.util.List;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyUserGuide;



/**
 * server 层接口
 * @author wangliang
 *
 */
public interface SyUserGuideService {
    /**
     * 按id查询实体
     * @param id
     * @return
     */
	public SyUserGuide query(Integer id);
	
	/**
	 * 保存实体
	 * @param d
	 * @return
	 */
	public int  insert(SyUserGuide  u);
	
	/**
	 * 依据条件查询集合
	 * @param maputil
	 * @return
	 */
	public List<SyUserGuide> selectByItem(MapUtil maputil);

}
