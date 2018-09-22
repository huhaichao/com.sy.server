package com.sy.server.app.service;

import java.io.Closeable;

import com.sy.common.Exception.AppPayWechatServiceException;
import com.sy.common.bean.SyCashAccount;
import com.sy.common.bean.SyPurse;
import com.sy.common.bean.AppPayWechatBean.AppPayConfig;
import com.sy.common.bean.AppPayWechatBean.HttpConfig;
import com.sy.common.bean.AppPayWechatBean.UnifiedOrder;
import com.sy.common.bean.AppPayWechatBean.UnifiedOrderResponse;


/**
 * 微信APP支付服务入口
 * @author lizhenzhong 2017.5.28
 *
 */
public interface SyAppPayWechatService {
	/**
	 * 提供配置以初始化服务
	 * @param conf 微信应用配置
	 * @param httpConf Http配置信息
	 */
	public void init(AppPayConfig conf, HttpConfig httpConf,SyPurse syPurse,SyCashAccount syCashAccount);
	/**
	 * 调用统一下单接口，因为异常情况无签名返回，由应用负责在收到正确响应时去验证签名
	 * @param order
	 * @return
	 * @throws WechatAppPayServiceException
	 */
	public UnifiedOrderResponse unifiedOrder(UnifiedOrder order) throws AppPayWechatServiceException;
	/**
	 * 将收到的异步通知xml转成Java对象以供业务处理
	 * @param xml
	 * @return
	 * @throws MalformedPduException
	 */
	//public PaymentNotification parsePaymentNotificationXml(String xml) throws MalformedPduException;
	
	/**
	 * 订单查询，因为异常情况无签名返回，由应用负责在收到正确响应时去验证签名
	 * @param orderQuery
	 * @return
	 * @throws WechatAppPayServiceException
	 */
	//public OrderQueryResponse queryOrder(OrderQuery orderQuery) throws AppPayWechatServiceException;
	
	/**
	 * 生成客户端支付请求，需要提供调用统一下单接口后收到的prepay_id
	 * @param prepayId 调用统一下单接口后收到的prepay_id
	 * @return
	 * @throws WechatAppPayServiceException
	 */
	//public WechatAppPayRequest createPayRequestForClient(String prepayId);
}