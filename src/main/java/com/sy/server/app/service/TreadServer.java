package com.sy.server.app.service;

import java.util.Map;
import com.sy.common.bean.SyAccountBalance;
import com.sy.common.bean.SyTread;

/**
 *  用户  之间 交易接口
 * @author huchao
 *
 */
public interface TreadServer {
	
	/**
	 * 同步交易接口
	 * @param syTread  //交易信息
	 * @param map      //失主账户金额信息
	 * @return
	 */
	public  Map<String,String> usersTreadSyn(SyTread syTread,Map<String,Double> map);

}
