package com.sy.server.app.service;


import java.util.List;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyIntegral;


/**
 * server 层接口
 * @author wangliang
 *
 */
public interface SyCreditService {
    /**
     * 按id查询实体
     * @param id
     * @return
     */
	public SyIntegral query(Integer id);
	
	/**
	 * 保存实体
	 * @param d
	 * @return
	 */
	public int  insert(SyIntegral  u);
	
	/**
	 * 依据条件查询集合
	 * @param maputil
	 * @return
	 */
	public List<SyIntegral> selectByItem(MapUtil maputil);
	
	/**
	 * 依据日期查询对象
	 * @param maputil
	 * @return
	 */
	public List<SyIntegral> selectByDate(MapUtil maputil);


	
}
