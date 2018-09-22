package com.sy.server.app.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sy.common.Exception.AppPayWechatServiceException;
import com.sy.common.bean.RespondBean;
import com.sy.common.bean.SyCashAccount;
import com.sy.common.bean.SyPurse;
import com.sy.common.bean.AppPayWechatBean.AppPayConfig;
import com.sy.common.bean.AppPayWechatBean.HttpConfig;
import com.sy.common.bean.AppPayWechatBean.UnifiedOrder;
import com.sy.common.bean.AppPayWechatBean.UnifiedOrderResponse;
import com.sy.common.constant.Constant;
import com.sy.common.enums.MessageCodeConstant;
import com.sy.common.utils.IdGenerator;
import com.sy.common.utils.MD5;
import com.sy.common.utils.UtilForWechatPay;
import com.sy.server.app.service.SyAppPayWechatService;
import com.sy.server.app.service.impl.SyAppPayWechatServiceImpl;
import com.sy.server.cache.CacheOperation;

import net.sf.json.JSONObject;


/**
 * APP微信支付
 * @author lizhenzhong 2017.5.28
 *
 */
@RestController
@RequestMapping("/AppPayWechat")
public class AppPayWechatController {
	
	private  Logger   log  = LoggerFactory.getLogger(AppRestController.class);
	
	@Autowired
	private CacheOperation cacheOperation;
	//private String key = "63d8ddd6ff234233edefb2633c218f7c";
	private String device_info = "013467007045764";
	private String body = "Ipad mini 16G 白色";
	private String detail = "Ipad mini 16G 白色 二手";
	private String attach = "说明";
	private String out_trade_no = "1217752501201407033233368018";
	private int total_fee = 888;
	private String spbill_create_ip = "8.8.8.8";
	private String time_start = "20091225091010";
	private String time_expire = "20091227091010";
	private String goods_tag = "WXG";
	private String notify_url = "http://yoursite.com/AppPayWechat/appPayWechatNotify";
	private String product_id = "12235413214070356458058";
	private String nounce_str = "nounce";
	//private String appid = "wx333386e333333c44";
	//private String mch_id = "1611111110";
	
	/**
	 * APP微信支付预处理
	 * 
	 * @param
	 * @throws AppPayWechatServiceException 
	 * 
	 */
	@PostMapping(value = "/doPay", produces="application/json;charset=UTF-8")
	public  RespondBean Pretreatment(
			@RequestParam(value="userid",required = true) String userid,
			@RequestParam(value="amount",required = true) String amount,
			@RequestParam(value="userAccount",required = true) String userAccount,
			@RequestParam(value="vs",required = true)String vs,
			@RequestParam(value="sign",required = true)String sign
			) throws AppPayWechatServiceException {
		
    	log.info("微信预下单 接口调用参数:[amount:"+amount+"] [userAccount:"+userAccount+"] [vs:"+vs+"]");
    	JSONObject  response = new JSONObject();
    	RespondBean  respondBean = null;
    	try{
		//验证签名
		String signkey =  (String)cacheOperation.getFromDaysCache(Constant.SIGNKEY+userid);
		Map<String,String> para = new HashMap<String,String>();
		para.put("userid", userid);
		para.put("amount", amount);
		para.put("userAccount", userAccount);
		para.put("vs", vs);
		if(!MD5.encryption4JHZF(para, signkey).equals(sign)){
		    log.info("支付宝微信 上送报文延签失败");	
			return  new RespondBean(MessageCodeConstant.PAY_SIGNERROR);
		}
		String  orderNo  = IdGenerator.getInstance().generate();
		Double  amountD =Double.parseDouble(amount);
		BigDecimal amountB = BigDecimal.valueOf(amountD);
		//String createDate = Constant.YYYY_MM_DD_HH_MM_SS.format(new Date());
		Date  createDate  = new Date();
		SyPurse  syPurse = new SyPurse();
		syPurse.settSyAccount(userAccount);
		syPurse.settSyBusinessType("0");//充值
		syPurse.settSyMoney(amountB);
		syPurse.settSyPsOrderno(orderNo);
		syPurse.settSyMustsBalance(amountB);
		syPurse.settSyPsCtratetime(createDate);
		syPurse.settSyFinanced("1");//微信
		syPurse.settSyPsDelete("0");
		syPurse.settSyPsStatus("init");
		syPurse.settSyPsRefundStatus("2");
		log.info("组装钱包结束......");
		String  cashAccountNo  = IdGenerator.getInstance().generate();
		SyCashAccount syCashAccount = new SyCashAccount();
		syCashAccount.settSyAccount(userAccount);
		syCashAccount.settSyCashAccountNo(cashAccountNo);//流水单号
		syCashAccount.settSyCashAmount(amountB);//流水金额
		syCashAccount.settSyCashCreateDate(createDate);
		syCashAccount.settSyCashOutorinStatus("0");//资金流入
		syCashAccount.settSyPsOrderno(orderNo);//充值订单号
		syCashAccount.settSyCashType("0");//充值
		syCashAccount.settSyDeleteStatus("0");//未删除
		log.info("组装流水......");
    	
		AppPayConfig conf = new AppPayConfig();
		HttpConfig httpConf = new HttpConfig();
		
		SyAppPayWechatService appPayWechatService = new SyAppPayWechatServiceImpl();
		appPayWechatService.init(conf, httpConf, syPurse, syCashAccount);
		
		UnifiedOrder order = new UnifiedOrder();
	    order.setAppid(conf.getAppId()).setMch_id(conf.getMchId()).setAttach(attach).setBody(body).setDetail(detail)
	        .setDevice_info(device_info).setGoods_tag(goods_tag).setLimit_pay("limit_pay")
	        .setNonce_str(nounce_str);
	    order.setNotify_url(notify_url);
	    order.setOut_trade_no(out_trade_no);
	    order.setProduct_id(product_id);
	    order.setSpbill_create_ip(spbill_create_ip);
	    order.setTime_startByDate(UtilForWechatPay.fromWeixinDateFormat(time_start));
	    order.setTime_expireByDate(UtilForWechatPay.fromWeixinDateFormat(time_expire));
	    order.setTotal_fee(total_fee);
	    // 设置商户Key
	    order.setSign(UtilForWechatPay.validateFieldsAndGenerateWxSignature(order, conf.getKey()));
	    UnifiedOrderResponse unifiedOrderResponse =appPayWechatService.unifiedOrder(order);
	    String signRes = "Appid="+unifiedOrderResponse.getAppid()+
	    		          "&Mch_id="+unifiedOrderResponse.getMch_id()+
	    		          "&Nonce_str="+unifiedOrderResponse.getNonce_str();//作为APP端和后台校验标识（内部签名）
	 	   //组装报文
	 	   response.put("send", unifiedOrderResponse);
	 	   response.put("sign",UtilForWechatPay.bytesToHexString(UtilForWechatPay.md5(signRes.getBytes("UTF-8"))).toUpperCase());
		log.info("微信支付预处理结束...");
	} catch (Exception e) {
		log.info("app充值操作失败："+e.getMessage());
		return new RespondBean(MessageCodeConstant.WECHAT_PAY_ERROR) ;
	}
	
	return new RespondBean(response,MessageCodeConstant.WECHAT_PAY_SUCCESS) ;
	}
    /**
     * 支付结果通知
     * @param userAccount
     * @param aliSynRespond
     * @param vs
     * @return
     */
    @PostMapping(value="/appPayWechatNotify",produces="application/json;charset=UTF-8")
	public void  appPayWechatNotify(HttpServletRequest request,HttpServletResponse response){
    	Map<String,String[]> requestParams = request.getParameterMap();//获取微信统一下单返回参数
    	log.info("微信统一下单回调接口参数:"+requestParams);
    	               //解析参数并向微信返回提示

    	}
	/**
	 * 提现
	 * @param amount
	 * @param userAccount
	 * @param vs
	 * @return
	 * @throws AppPayWechatServiceException
	 */
	@PostMapping(value = "/withDraw", produces="application/json;charset=UTF-8")
	public  RespondBean searchOrderWechat(
			@RequestParam(value="amount",required = true) String amount,
			@RequestParam(value="userAccount",required = true) String userAccount,
			@RequestParam(value="vs",required = true)String vs) throws AppPayWechatServiceException {
		
    	log.info("微信提现 接口调用参数:[amount:"+amount+"] [userAccount:"+userAccount+"] [vs:"+vs+"]");
    	String  orderNo  = IdGenerator.getInstance().generate();
    	Double  amountD =Double.parseDouble(amount);
    	BigDecimal amountB = BigDecimal.valueOf(amountD);
    	//String createDate = Constant.YYYY_MM_DD_HH_MM_SS.format(new Date());
		Date  createDate  = new Date();
		SyPurse  syPurse = new SyPurse();
		syPurse.settSyAccount(userAccount);
		syPurse.settSyBusinessType("0");//充值
		syPurse.settSyMoney(amountB);
		syPurse.settSyPsOrderno(orderNo);
		syPurse.settSyMustsBalance(amountB);
		syPurse.settSyPsCtratetime(createDate);
		syPurse.settSyFinanced("2");//支付宝
		syPurse.settSyPsDelete("0");
		syPurse.settSyPsStatus("init");
		syPurse.settSyPsRefundStatus("2");
		log.info("组装钱包结束......");
		String  cashAccountNo  = IdGenerator.getInstance().generate();
		SyCashAccount syCashAccount = new SyCashAccount();
		syCashAccount.settSyAccount(userAccount);
		syCashAccount.settSyCashAccountNo(cashAccountNo);//流水单号
		syCashAccount.settSyCashAmount(amountB);//流水金额
		syCashAccount.settSyCashCreateDate(createDate);
		syCashAccount.settSyCashOutorinStatus("0");//资金流入
		syCashAccount.settSyPsOrderno(orderNo);//充值订单号
		syCashAccount.settSyCashType("0");//充值
		syCashAccount.settSyDeleteStatus("0");//未删除
		log.info("组装流水......");
    	
		AppPayConfig conf = new AppPayConfig();
		HttpConfig httpConf = new HttpConfig();
		
		SyAppPayWechatService appPayWechatService = new SyAppPayWechatServiceImpl();
		appPayWechatService.init(conf, httpConf, syPurse, syCashAccount);
		
		UnifiedOrder order = new UnifiedOrder();
	    order.setAppid(conf.getAppId()).setMch_id(conf.getMchId()).setAttach(attach).setBody(body).setDetail(detail)
        .setDevice_info(device_info).setGoods_tag(goods_tag).setLimit_pay("limit_pay")
        .setNonce_str(nounce_str);
	    order.setNotify_url(notify_url);
	    order.setOut_trade_no(out_trade_no);
	    order.setProduct_id(product_id);
	    order.setSpbill_create_ip(spbill_create_ip);
	    order.setTime_startByDate(UtilForWechatPay.fromWeixinDateFormat(time_start));
	    order.setTime_expireByDate(UtilForWechatPay.fromWeixinDateFormat(time_expire));
	    order.setTotal_fee(total_fee);
	    System.out.println(order.toString());
	    // 设置商户Key
	    order.setSign(UtilForWechatPay.validateFieldsAndGenerateWxSignature(order, conf.getKey()));
		//RespondBean  respondBean =  appPayWechatService.unifiedOrder(order);
		
		log.info("微信支付预处理结束...");
		return null;
	}
}