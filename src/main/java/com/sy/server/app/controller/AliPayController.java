package com.sy.server.app.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.sy.common.bean.RespondBean;
import com.sy.common.bean.SyCashAccount;
import com.sy.common.bean.SyPurse;
import com.sy.common.constant.Constant;
import com.sy.common.enums.MessageCodeConstant;
import com.sy.common.utils.IdGenerator;
import com.sy.common.utils.MD5;
import com.sy.server.app.service.SyZFBPayService;
import com.sy.server.cache.CacheOperation;

import net.sf.json.JSONObject;

/**
 * 钱包 -- 支付宝支付接口
 * @author huchao
 *
 */
@RestController
@RequestMapping("/alipay")
public class AliPayController {
	
	private Logger  log = Logger.getLogger(AliPayController.class);
	
	@Resource
	private  SyZFBPayService  SyZFBPayService  ;
	
	@Autowired
	private CacheOperation cacheOperation;
	
	/**
	 * 支付宝预下单操作
	 * @param userid //用户id
	 * @param amount //充值金额
	 * @param userAccount //用户账户
	 * @param vs
	 * @param sign  //签名
	 * @return
	 */
	//支付宝预下单接口	
    @PostMapping(value="/doPay",produces="application/json;charset=UTF-8")
	public RespondBean preAlipay(@RequestParam(value="userid",required = true) String userid,@RequestParam(value="amount",required = true) String amount,@RequestParam(value="userAccount",required = true) String userAccount,
			@RequestParam(value="vs",required = true)String vs,@RequestParam(value="sign",required = true)String sign){
    	log.info("支付宝预下单 接口调用参数:[amount:"+amount+"] [userAccount:"+userAccount+"] [vs:"+vs+"]");
    	JSONObject  response = new JSONObject();
		try {
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
			String respondBean = SyZFBPayService.ZFBpayAndWithdrawIint(syPurse, syCashAccount);
			
	    	Map<String,String>  map = new HashMap<String,String>();
	    	map.put("send", respondBean);
	    	String signRes =MD5.encryption4JHZF(map, signkey);
	    	
	    	//组装报文
	    	response.put("send", respondBean);
	    	response.put("sign",signRes);
		} catch (Exception e) {
			log.info("app充值操作失败："+e.getMessage());
			return new RespondBean(MessageCodeConstant.ALIPAY_PAY_DOPAYERROR) ;
		}
		
		return new RespondBean(response,MessageCodeConstant.ALIPAY_PAY_DOPAYSUCCESS) ;
	}
    
    
	/**
	 * 支付宝    异步通知服务下单结果接口
	 * @param request
	 * @param response
	 * @return
	 */
    @PostMapping(value="/payAsynNotify",produces="application/json;charset=UTF-8")
	public String  aliPayAsynNotify(HttpServletRequest request,HttpServletResponse response){
    	String result="";
		try {
			Map<String,String> params = new HashMap<String,String>();
			Map<String,String[]> requestParams = request.getParameterMap();
			log.info("支付宝下单回调接口参数:"+requestParams);
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			    String name = (String) iter.next();
			    String[] values = (String[]) requestParams.get(name);
			    String valueStr = "";
			    for (int i = 0; i < values.length; i++) {
			        valueStr = (i == values.length - 1) ? valueStr + values[i]
			                    : valueStr + values[i] + ",";
			   }
			  //乱码解决，这段代码在出现乱码时使用。
			  //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			  params.put(name, valueStr);
			 }
			result = SyZFBPayService.ZFBpayAndWithdrawAsynNotify(params);
			log.info("支付宝下单回调接口结果:"+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "error";
			log.info("支付宝下单回调接口异常:"+e.getMessage());
		}
    	return  result;
	}
    
    
    /**
     * app端返回支付宝同步 通知结果，验证签名
     * @param userAccount //用户账户
     * @param aliSynRespond //支付宝返回的报文
     * @param userid //用户id
     * @param vs
     * @return
     */
    @PostMapping(value="/paySynNotify",produces="application/json;charset=UTF-8")
	public RespondBean  aliPaySynNotify(@RequestParam(value="userAccount",required = true) String userAccount,@RequestParam(value="userid",required = true) String userid,@RequestParam(value="aliSynRespond") String aliSynRespond,
			@RequestParam(value="vs",required = true)String vs){
    	JSONObject  alipayRespond  = JSONObject.fromObject(aliSynRespond);
    	String   resultStatus =alipayRespond.getString("resultStatus");
    	
    	RespondBean  respondBean = null;
    	if ("9000".equals(resultStatus)){
    		JSONObject result =alipayRespond.getJSONObject("result");
        	JSONObject  alipay_trade_app_pay_response =result.getJSONObject("alipay_trade_app_pay_response");
        	String  sign  = result.getString("sign");
        	String  signType = result.getString("sign_type");
        	respondBean= SyZFBPayService.ZFBpayAndWithdrawSynNotify(alipay_trade_app_pay_response, sign, signType);
        	Object data = respondBean.getData();
        	String signkey =  (String)cacheOperation.getFromDaysCache(Constant.SIGNKEY+userid);
        	
        	Map<String,String>  map = new HashMap<String,String>();
	    	map.put("responce", data.toString());
	    	String signRes =MD5.encryption4JHZF(map, signkey);
	    	map.put("sign",signRes);
	    	respondBean.setData(map);
	    	
    	}else{
    		MessageCodeConstant messageCodeConstant = MessageCodeConstant.valueOf("ALIPAY_"+resultStatus);
    		if(messageCodeConstant==null){
    			messageCodeConstant =MessageCodeConstant.ALIPAY_PAY_PAYNORESULT;
    		}
    		respondBean = new RespondBean(messageCodeConstant);
    	}
    	
    	return respondBean;
	}
	
    /**
     * 充值结果 查询接口
     * @param orderNo  //订单号
     * @param userid   //用户id
     * @param treadNo  //微信 支付宝 返回订单号
     * @param sign     //签名
     * @param vs
     * @return
     */
    @PostMapping(value="/aliPaySearch",produces="application/json;charset=UTF-8")
    public RespondBean  aliPayOrderSearch(@RequestParam(value="orderNo",required = true) String orderNo, @RequestParam(value="userid",required = true) String userid,
    		@RequestParam(value="treadNo",required = true)String treadNo,@RequestParam(value="sign",required = true) String sign,
    		@RequestParam(value="vs",required = true) String vs){
    	String signkey =  (String)cacheOperation.getFromDaysCache(Constant.SIGNKEY+userid);
		Map<String,String> para = new HashMap<String,String>();
		para.put("userid", userid);
		para.put("vs", vs);
		para.put("treadNo", treadNo);
		para.put("orderNo", orderNo);
		
		if(!MD5.encryption4JHZF(para, signkey).equals(sign)){
		    log.info("支付宝订单查询 上送报文延签失败");	
			return  new RespondBean(MessageCodeConstant.PAY_SIGNERROR);
		}
		RespondBean  respondBean  = null;
    	Map<String, String> map;
		try {
			map = SyZFBPayService.ZFBpayAndWithdrawQuery(orderNo, treadNo);
			String signRes =MD5.encryption4JHZF(map, signkey);
	    	map.put("sign", signRes);
	    	respondBean =  new RespondBean(map,MessageCodeConstant.ALIPAY_PAY_PAYSUCCESS);
		} catch (AlipayApiException e) {
			log.info("支付宝订单查询 出错");
			respondBean =  new RespondBean(MessageCodeConstant.PAY_EXCEPTION);
		}
		return respondBean;
    	
    }
    
    
    /**
     * 提现方式查询接口 
     * @param userAcount //用户账户
     * @param userid     //用户id
     * @param withdrawAmount     //提现金额
     * @param sign     //签名
     * @param vs
     * @return
     */
    @PostMapping(value="/payWaySearch",produces="application/json;charset=UTF-8")
    public RespondBean  aliPayWaySearch(@RequestParam(value="userAcount",required = true) String userAcount, @RequestParam(value="userid",required = true) String userid,
    		@RequestParam(value="withdrawAmount",required = true)String amount,@RequestParam(value="sign",required = true) String sign,
    		@RequestParam(value="vs",required = true) String vs){
    	String signkey =  (String)cacheOperation.getFromDaysCache(Constant.SIGNKEY+userid);
		Map<String,String> para = new HashMap<String,String>();
		para.put("userid", userid);
		para.put("vs", vs);
		para.put("userAcount", userAcount);
		para.put("withdrawAmount", amount);
		
		if(!MD5.encryption4JHZF(para, signkey).equals(sign)){
		    log.info("提现方式查询查询 上送报文延签失败");	
			return  new RespondBean(MessageCodeConstant.PAY_SIGNERROR);
		}
		RespondBean  respondBean  = null;
    	Map<String, String> map;
		try {
			map = SyZFBPayService.payWithdraw(userAcount, amount);
			String signRes =MD5.encryption4JHZF(map, signkey);
	    	map.put("sign", signRes);
	    	respondBean =  new RespondBean(map,MessageCodeConstant.PAYWAY_SEARCHSUCCESS);
		} catch (Exception e) {
			log.info("提现方式查询  出错");
			respondBean =  new RespondBean(MessageCodeConstant.PAY_EXCEPTION);
		}
		return respondBean;
    	
    }
    
    /**
     * 提现接口   
     * @param userAccount  //用户账户
     * @param userid       //用户id
     * @param withdrawAmount //提现金额
     * @param sign          //签名
     * @param withdrawAcount  //收款方账户
     * @param withdrawWay     //提现方式  0 -- 微信退款   1  ---  支付宝退款   2 --- 企业付款
     * @param withdrawOrderNo //提现原订单号  --- 退款是充值的原订单号   --- 转账 是转账唯一编号
     * @param vs
     * @return
     */
    @PostMapping(value="/payWithdraw",produces="application/json;charset=UTF-8")
    public RespondBean  aliPayWithdraw(@RequestParam(value="userAccount",required = true) String userAccount, @RequestParam(value="userid",required = true) String userid,
    		@RequestParam(value="withdrawAmount",required = true)String withdrawAmount,@RequestParam(value="sign",required = true) String sign,
    		@RequestParam(value="withdrawAcount",required = true)String withdrawAcount,@RequestParam(value="withdrawWay",required = true)String withdrawWay,
    		@RequestParam(value="withdrawOrderNo",required = true)String withdrawOrderNo,
    		@RequestParam(value="vs",required = true) String vs){
    	String signkey =  (String)cacheOperation.getFromDaysCache(Constant.SIGNKEY+userid);
		Map<String,String> para = new HashMap<String,String>();
		para.put("userid", userid);
		para.put("vs", vs);
		para.put("userAcount", userAccount);
		para.put("withdrawAmount", withdrawAmount);
		para.put("withdrawAcount", withdrawAcount);
		para.put("withdrawWay", withdrawWay);
		para.put("withdrawOrderNo", withdrawOrderNo);
		
		if(!MD5.encryption4JHZF(para, signkey).equals(sign)){
		    log.info(userAccount+" 支付宝提现 上送报文延签失败");	
			return  new RespondBean(MessageCodeConstant.PAY_SIGNERROR);
		}
		
		String  orderNo  = IdGenerator.getInstance().generate();
		RespondBean  respondBean  = null;
    	Map<String, String> map;
		try {
			BigDecimal amount  = BigDecimal.valueOf(Double.parseDouble(withdrawAmount));
			Date  createDate  = new Date();
			SyPurse  syPurse = new SyPurse();
			syPurse.settSyAccount(userAccount);
			syPurse.settSyMoney(amount);
			syPurse.settSyPsRefundOrderno(withdrawOrderNo);
			syPurse.settSyPsRrefundWay(withdrawWay);
			syPurse.settSyBusinessType("1");
			syPurse.settSyPsStatus("init");
			syPurse.settSyPsOutaccount(withdrawAcount);
			syPurse.settSyPsDelete("0");
			syPurse.settSyPsCtratetime(createDate);
			syPurse.settSyPsOrderno(orderNo);
			syPurse.settSyMustsBalance(amount);
			
			SyCashAccount syCashAccount = new SyCashAccount();
			
			String  cashAccountNo  = IdGenerator.getInstance().generate();
			syCashAccount.settSyAccount(userAccount);
			syCashAccount.settSyCashAccountNo(cashAccountNo);//流水单号
			syCashAccount.settSyCashAmount(amount);//流水金额
			syCashAccount.settSyCashCreateDate(createDate);
			syCashAccount.settSyCashOutorinStatus("0");//资金流入
			syCashAccount.settSyPsOrderno(orderNo);//充值订单号
			syCashAccount.settSyCashType("1");//提现
			syCashAccount.settSyDeleteStatus("0");//未删除
			
			map = SyZFBPayService.ZFBpayWithdraw(syPurse, syCashAccount);
			String signRes =MD5.encryption4JHZF(map, signkey);
	    	map.put("sign", signRes);
	    	
	    	String respondCode = map.get("respondCode");
	    	respondBean =  new RespondBean(map,MessageCodeConstant.valueOf("ALIPAY_"+respondCode));
		} catch (Exception e) {
			log.info(userAccount+"支付宝提现  出错"+e.getMessage());
			respondBean =  new RespondBean(MessageCodeConstant.PAY_EXCEPTION);
		}
		return respondBean;
    	
    }
    
   /**
    * 提现结果查询 接口
    * @param userAccount      //用户账户
    * @param userid          //用户id
    * @param withdrawAmount  //提现金额
    * @param sign            //签名
    * @param withdrawWay     //提现方式
    * @param orderNo         //提现订单号 
    * @param withdrawOrderNo //提现原订单号  --- 退款是充值的原订单号   --- 转账 是转账唯一编号
    * @param vs
    * @return
    */
    @PostMapping(value="/payWithdrawSearch",produces="application/json;charset=UTF-8")
    public RespondBean  aliPayWithdrawSearch(@RequestParam(value="userAccount",required = true) String userAccount, @RequestParam(value="userid",required = true) String userid,
    		@RequestParam(value="withdrawAmount",required = true)String withdrawAmount,@RequestParam(value="sign",required = true) String sign,
    		@RequestParam(value="withdrawWay",required = true)String withdrawWay,@RequestParam(value="orderNo",required = true)String orderNo,
    		@RequestParam(value="withdrawOrderNo",required = true)String withdrawOrderNo,
    		@RequestParam(value="vs",required = true) String vs){
    	String signkey =  (String)cacheOperation.getFromDaysCache(Constant.SIGNKEY+userid);
		Map<String,String> para = new HashMap<String,String>();
		para.put("userid", userid);
		para.put("vs", vs);
		para.put("userAcount", userAccount);
		para.put("withdrawAmount", withdrawAmount);
		para.put("withdrawWay", withdrawWay);
		para.put("withdrawOrderNo", withdrawOrderNo);
		para.put("orderNo", orderNo);
		
		if(!MD5.encryption4JHZF(para, signkey).equals(sign)){
		    log.info(userAccount+" 支付宝提现 上送报文延签失败");	
			return  new RespondBean(MessageCodeConstant.PAY_SIGNERROR);
		}
		RespondBean  respondBean  = null;
    	Map<String, String> map;
		try {
			BigDecimal amount  = BigDecimal.valueOf(Double.parseDouble(withdrawAmount));
			SyPurse  syPurse = new SyPurse();
			syPurse.settSyPsOrderno(orderNo);
			syPurse.settSyAccount(userAccount);
			syPurse.settSyMoney(amount);
			syPurse.settSyPsRefundOrderno(withdrawOrderNo);
			syPurse.settSyPsRrefundWay(withdrawWay);
			
			map = SyZFBPayService.payWithdrawSearch(syPurse);
			String signRes =MD5.encryption4JHZF(map, signkey);
	    	map.put("sign", signRes);
	    	
	    	String respondCode = map.get("respondCode");
	    	respondBean =  new RespondBean(map,MessageCodeConstant.valueOf("ALIPAY_"+respondCode));
		} catch (Exception e) {
			log.info(userAccount+"支付宝提现接口查询  出错"+e.getMessage());
			respondBean =  new RespondBean(MessageCodeConstant.PAY_EXCEPTION);
		}
		return respondBean;
    	
    }
    
    

}
