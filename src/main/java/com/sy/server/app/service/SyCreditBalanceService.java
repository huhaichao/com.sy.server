package com.sy.server.app.service;


import java.util.List;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyCreditBalance;


/**
 * server 层接口
 * @author wangliang
 *
 */
public interface SyCreditBalanceService {
    /**
     * 按id查询实体
     * @param id
     * @return
     */
	public SyCreditBalance query(Integer id);
	
	/**
	 * 保存实体
	 * @param d
	 * @return
	 */
	public int  insert(SyCreditBalance  u);
	
	/**
	 * 依据条件查询集合
	 * @param maputil
	 * @return
	 */
	public List<SyCreditBalance> selectByItem(MapUtil maputil);
	
	/**
	 * 依据日期查询对象
	 * @param maputil
	 * @return
	 */
	public List<SyCreditBalance> selectByDate(MapUtil maputil);


	
}
