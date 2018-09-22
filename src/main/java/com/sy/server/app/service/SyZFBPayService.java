package com.sy.server.app.service;

import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.sy.common.bean.RespondBean;
import com.sy.common.bean.SyCashAccount;
import com.sy.common.bean.SyPurse;

import net.sf.json.JSONObject;

/**
 * 支付宝处理类
 * 
 * @author huchao
 *
 */
public interface  SyZFBPayService {
	
	/**
	 * 支付宝预下单操作
	 * @param syPurse
	 * @param syBalance
	 * @param syCashAccount
	 * @return
	 * @throws Exception 
	 */
	public  String ZFBpayAndWithdrawIint(SyPurse syPurse, SyCashAccount syCashAccount) throws Exception;
	
	/**
	 * 支付宝下单 异步通知
	 * @param map
	 * @return
	 */
	public  String ZFBpayAndWithdrawAsynNotify(Map<String,String> map)throws Exception;
	
	/**
	 * 支付宝下单操作 同步通知 延签
	 * @param map
	 * @return
	 */
	public  RespondBean ZFBpayAndWithdrawSynNotify(JSONObject content,String sign,String signType);
	
	/**
	 * 支付宝 支付信息查询接口
	 * @param orderNo
	 * @param treadNo
	 * @return
	 */
	public Map<String,String> ZFBpayAndWithdrawQuery(String orderNo,String  treadNo)throws AlipayApiException;
	
	/**
	 * 支付宝交易 关闭接口
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> ZFBpayAndWithdrawClose()throws Exception;
	
	/**
	 * 支付宝 提现接口
	 * @param syPurse
	 * @param syCashAccount
	 * @throws AlipayApiException 
	 * @throws Exception 
	 */
	public  Map<String,String> ZFBpayWithdraw(SyPurse syPurse, SyCashAccount syCashAccount) throws AlipayApiException, Exception;
	
	/**
	 * 查询 用户的支付方式
	 * 
	 * @param userAcount
	 * @param amount
	 * @return
	 */
	public Map<String,String>  payWithdraw(String userAcount,String amount);
	
	
	/**
	 * 支付宝 提现查询接口
	 * @param syPurse
	 * @return
	 * @throws AlipayApiException 
	 * @throws Exception 
	 */
	public Map<String,String>  payWithdrawSearch(SyPurse syPurse) throws AlipayApiException, Exception;
}
