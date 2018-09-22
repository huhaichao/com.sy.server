package com.sy.server.app.service;

import java.util.List;

import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyItemInformation;

/**
 * server 层接口
 * @author huchao
 *
 */
public interface ItemService {

	public List<SyItemInformation> selectByItem(MapUtil maputil);
	public List<SyItemInformation> selectByDetailItem(MapUtil maputil);
	
}
