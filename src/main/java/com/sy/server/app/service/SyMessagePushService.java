package com.sy.server.app.service;

import java.util.List;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyMessagePush;


/**
 * server 层接口
 * @author yumeng
 *
 */
public interface SyMessagePushService {
	
	public  SyMessagePush selectByPrimaryKey(Integer tSyMessageId);

	public List<SyMessagePush> selectByItem(MapUtil maputil);	

	public int deleteByItem(MapUtil maputil);
	/**
	 * wangliang 更新读写状态
	 * @param maputil
	 * @return
	 */
	public int updateByPrimaryKey(SyMessagePush smp);
}
