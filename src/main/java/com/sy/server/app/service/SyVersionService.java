package com.sy.server.app.service;


import java.util.List;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyVersion;


/**
 * server 层接口
 * @author wangliang
 *
 */
public interface SyVersionService {
    /**
     * 按id查询实体
     * @param id
     * @return
     */
	public SyVersion query(Integer id);
	
	/**
	 * 保存实体
	 * @param d
	 * @return
	 */
	public int  insert(SyVersion  u);
	
	/**
	 * 依据条件查询集合
	 * @param maputil
	 * @return
	 */
	public List<SyVersion> selectByItem(MapUtil maputil);

}
