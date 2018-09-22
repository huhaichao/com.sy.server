package com.sy.server.app.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sy.common.Exception.AppPayWechatServiceException;
import com.sy.common.bean.RespondBean;
import com.sy.common.bean.SyCashAccount;
import com.sy.common.bean.SyPurse;
import com.sy.common.bean.AppPayWechatBean.AppPayConfig;
import com.sy.common.bean.AppPayWechatBean.HttpConfig;
import com.sy.common.bean.AppPayWechatBean.UnifiedOrder;
import com.sy.common.bean.AppPayWechatBean.UnifiedOrderResponse;
import com.sy.common.enums.MessageCodeConstant;
import com.sy.common.utils.FasterXmlJacksonUtil;
import com.sy.common.utils.HttpClientUtil;
import com.sy.common.utils.UtilForWechatPay;
import com.sy.server.app.service.SyAppPayWechatService;
import com.sy.server.app.service.SyPayAbstractService;

import net.sf.json.JSONObject;
/**
 * 微信支付业务类
 * @author lizhenzhong 
 * @Date   2017.06.03
 */
public class SyAppPayWechatServiceImpl  extends SyPayAbstractService implements SyAppPayWechatService{
  private AppPayConfig config;
  private HttpConfig httpConf;
  private SyPurse syPurse;
  private SyCashAccount syCashAccount;
  private static Logger logger = LoggerFactory.getLogger(SyAppPayWechatServiceImpl.class);
  private static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
  private static String ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";


  public void init(AppPayConfig conf, HttpConfig httpConf,SyPurse syPurse,SyCashAccount syCashAccount) {
	validateConf();
    this.config = conf;
    this.httpConf = httpConf;
    //super.initHttp(this.httpConf);
    this.syPurse = syPurse;
    this.syCashAccount = syCashAccount;
  }

  private void validateConf() {
    if (config == null || !config.isReady()) {
      throw new IllegalStateException("Config must be not null or missing settings in config:"
          + config);
    }
  }

  public UnifiedOrderResponse unifiedOrder(UnifiedOrder order) throws AppPayWechatServiceException {
    validateConf();
    String responseText = null;
    HttpClientUtil httpClient = new HttpClientUtil();
    try {
      // 序列化成xml
      String xml = FasterXmlJacksonUtil.marshalToXml(order, config.getKey());
      logger.debug("Sending to wechat for unified order: " + xml);
      // 发到服务器并收回XML响应
      responseText = httpClient.sendHttpPostAndReturnString(UNIFIED_ORDER_URL, xml,httpConf);
      if (responseText == null) {
        throw new AppPayWechatServiceException(
            "Unified order API to Wechat failed! Received empty response. Order Info: "
                + order.toString());
      }
      // 反序列化为Java对象并返回
      UnifiedOrderResponse unifiedOrderResponse = FasterXmlJacksonUtil.unmarshalFromXml(
          responseText, UnifiedOrderResponse.class);
      logger.debug("Response from WX is {}", unifiedOrderResponse);
      
      //将下单记录存入数据库
      payAndWithdrawInit(syPurse,syCashAccount);

      return unifiedOrderResponse;

    } catch (IOException e) {
      logger.warn("Failed to call Weixin for order {} ", order, e);
      throw new AppPayWechatServiceException("Unified order API to Wechat failed! Order Info: "
          + order.toString(), e);
    }
  }

/*  public PaymentNotification parsePaymentNotificationXml(String xml) throws MalformedPduException {
    validateConf();
    return WechatAppPayProtocolHandler.unmarshalFromXmlAndValidateSignature(xml,
        PaymentNotification.class, config.key);
  }

  public WechatAppPayRequest createPayRequestForClient(String prepayId) {
    WechatAppPayRequest request = new WechatAppPayRequest();
    request.setAppid(config.appId);
    request.setAppPackage("Sign=WXPay");
    request.setPartnerid(config.mchId);
    request.setPrepayid(prepayId);
    request.setTimestamp(Long.toString(System.currentTimeMillis() / 1000));
    request.setNoncestr(Util.generateString(32));
    request.setSign(Util.validateFieldsAndGenerateWxSignature(request, config.key));
    return request;
  }*/

/*  public OrderQueryResponse queryOrder(OrderQuery orderQuery) throws AppPayWechatServiceException {
    validateConf();
    String responseText = null;
    try {
      // 序列化成xml
      String xml = WechatAppPayProtocolHandler.marshalToXml(orderQuery, config.key);
      logger.debug("Sending to wechat for order query: " + xml);
      // 发到服务器并收回XML响应
      responseText = sendHttpPostAndReturnString(ORDER_QUERY_URL, xml);
      if (responseText == null) {
        throw new WechatAppPayServiceException(
            "Order Query API to Weixin failed! Received empty response. OrderQuery Info: "
                + orderQuery.toString());
      }
      // 反序列化为Java对象并返回
      OrderQueryResponse orderQueryResponse = WechatAppPayProtocolHandler.unmarshalFromXml(
          responseText, OrderQueryResponse.class);
      logger.debug("Response from WX for order query is {}", orderQueryResponse);
      return orderQueryResponse;
    } catch (IOException e) {
      logger.warn("Failed to call Weixin for order {} ", orderQuery, e);
      throw new WechatAppPayServiceException("Order Query API to Weixin failed! Query Info: "
          + orderQuery.toString(), e);
    } catch (MalformedPduException e) {
      logger.warn("Failed to unmarshall the order query response {} ", responseText, e);
      throw new WechatAppPayServiceException("Failed to unmarshall the order query response: "
          + responseText + " for querying the order: " + orderQuery.toString(), e);
    }
  }*/
}