package com.sy.server.app.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.sy.common.bean.RespondBean;
import com.sy.common.bean.SyAccountBalance;
import com.sy.common.bean.SyCashAccount;
import com.sy.common.bean.SyPurse;
import com.sy.common.constant.Constant;
import com.sy.common.enums.MessageCodeConstant;
import com.sy.common.enums.SyTreadCode;
import com.sy.server.app.service.SyPayAbstractService;
import com.sy.server.app.service.SyZFBPayService;

import net.sf.json.JSONObject;

/**
 * 支付宝 支付类
 * @author huchao
 *
 */
@Service
public class SyZFBPayServiceImpl extends SyPayAbstractService implements SyZFBPayService {
	
	 private Logger  log = Logger.getLogger(SyZFBPayServiceImpl.class);
	 
	 /**
	  * 支付宝支付客户端
	  */
	 private  final AlipayClient alipayClient = 
				new DefaultAlipayClient(Constant.ALIPAY_URL, Constant.ALIPAY_APP_ID, 
						Constant.ALIPAY_APP_PRIVATE_KEY, "json", "utf-8", Constant.ALIPAY_PUBLIC_KEY, "RSA2");	
     /**
      * 支付宝 预下单
      * @param syPurse
      * @param syBalance
      * @param syCashAccount
      * @return
     * @throws AlipayApiException 
      */
	 @Transactional(rollbackFor={RuntimeException.class},propagation=Propagation.REQUIRED)
	 public String ZFBpayAndWithdrawIint(SyPurse syPurse, SyCashAccount syCashAccount) throws RuntimeException {
	    		try {
					//下单操作
					payAndWithdrawInit(syPurse,syCashAccount);
					//ali下单操作
					//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
					AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
					//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
					AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
					model.setBody("充值操作");
					model.setSubject("充值操作");
					model.setTotalAmount(String.valueOf(syPurse.gettSyMoney()));
					model.setOutTradeNo(syPurse.gettSyPsOrderno());
					model.setProductCode(Constant.ALIPAY_PRODUCTCODE);
					model.setSellerId(Constant.ALIPAY_SELLERID);
					request.setNotifyUrl(Constant.ALIPAY_NOTIFYURL);
					model.setDisablePayChannels(Constant.ALIPAY_CREDITCHANNELS);//支付宝禁用 信用卡通道
					request.setBizModel(model);
					
					AlipayTradeAppPayResponse response  = alipayClient.sdkExecute(request);
					log.info("app充值加密后参数"+response.getBody());
					return response.getBody();
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("支付宝 预下单异常");
				}
	  }

    
    /**
     * 支付宝异步通知 接口
     * @throws Exception 
     */
	@Transactional(rollbackFor={Exception.class},isolation=Isolation.REPEATABLE_READ)
	public String ZFBpayAndWithdrawAsynNotify(Map<String,String> map) throws Exception {
		/*try {*/
			boolean flag = AlipaySignature.rsaCheckV1(map, Constant.ALIPAY_PUBLIC_KEY, "utf-8", "RSA2");
			SyPurse   syPurse   = new SyPurse();
			SyPurse   syPurseOrg =null;
			if(flag){
			//延签通过
				log.info("ZFB异步通知 延签通过");
				String  orderNo = map.get("out_trade_no");
						
				List<SyPurse>  list = selectByPsOrderno(orderNo);
				
				if (list !=null && list.size()==1){
					syPurseOrg = list.get(0); 
				}else{
					log.info("ZFB异步通知  外部商户号有问题");
					return "error";
				}
				
				String seller_id = map.get("seller_id");
				String total_amount = map.get("total_amount");
				String app_id = map.get("app_id");
				
				if (checkRespondInfo(seller_id ,syPurseOrg.gettSyMoney(),total_amount , app_id )){
					return "error";
				}
				
				String treadStatus = map.get("trade_status");
				String tSyPsStatus = "";
				if("TRADE_SUCCESS".equals(treadStatus)){
					tSyPsStatus = SyTreadCode.SUCCESS.getSyTreadCode();
				}else if ("TRADE_CLOSED".equals(treadStatus)){
					tSyPsStatus = SyTreadCode.CLOSE.getSyTreadCode();
				}else if ("TRADE_FINISHED".equals(treadStatus)){
					//退款状态 提示，交易已完成，不能退款
					tSyPsStatus = SyTreadCode.FINISHED.getSyTreadCode();
				}
				
				String syAccount = syPurseOrg.gettSyAccount();
				syPurse.settSyAccount(syPurseOrg.gettSyAccount());
				syPurse.settSyPsOrderno(orderNo);
				syPurse.settSyPsTreadno(map.get("trade_no"));
				syPurse.settSyFinancedAccount(map.get("buyer_logon_id"));
				syPurse.settSyPsStatus(tSyPsStatus);
				syPurse.settSyPsCompletetime(Constant.YYYY_MM_DD_HH_MM_SS.parse(map.get("notify_time")));
				//更新钱包表
				payAndWithdrawUpdate(syPurse);
				
				if ("success".equals(tSyPsStatus)){
					
					//支付成功 更新余额操作
					SyAccountBalance syAccountBalance = new SyAccountBalance();
					syAccountBalance.settSyAccount(syAccount);
					syAccountBalance.settSyMoney(syPurseOrg.gettSyMoney());
					syAccountBalance.settSyUpdateDate(new Date());
					int count = balanceUpdate(syAccountBalance);
					if(count !=1){
						log.info(syAccount+"---修改余额条数["+count+"]");
						throw new Exception(syAccount+"：修改余额异常");
					}
				}
				
				
			}else{
			//延签不通过
			   log.info("ZFB异步通知 延签不通过");
			   return "error";
			}
		/*	
		} catch (Exception e) {
			log.info("ZFB异步通知 报文异常信息："+e.getMessage());
			return "error";
		} */
		return "success";
	}

   /**
    * 同步通知验证 签名
    */
	public RespondBean ZFBpayAndWithdrawSynNotify(JSONObject content,String sign,String signType) {
		log.info("支付宝支付 同步结果延签");
		//JSONObject  jsonObject
		try {
			boolean  alipaybool = AlipaySignature.rsaCheck(content.toString(), sign, Constant.ALIPAY_PUBLIC_KEY, "UTF-8",signType);
			SyPurse syPurseOrg = null;
			if (alipaybool){
				
				String  orderNo = content.getString("out_trade_no");
				
				List<SyPurse>  list = selectByPsOrderno(orderNo);
				
				if (list !=null && list.size()==1){
					syPurseOrg = list.get(0); 
				}else{
					log.info("ZFB异步通知  外部商户号有问题");
					return new RespondBean(MessageCodeConstant.ALIPAY_PAY_CHECKSIGNERROR) ;
				}
				
				String seller_id = content.getString("seller_id");
				String total_amount = content.getString("total_amount");
				String app_id = content.getString("app_id");
				
				
				if (checkRespondInfo(seller_id ,syPurseOrg.gettSyMoney(),total_amount , app_id )){
					return new RespondBean(MessageCodeConstant.ALIPAY_PAY_CHECKSIGNERROR) ;
				}
			}else {
				return new RespondBean(MessageCodeConstant.ALIPAY_PAY_CHECKSIGNERROR) ;
			}
			
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			log.info("同步通知验证 签名失败 签名报文:["+content+"] 签名:["+sign+"] 签名方式:["+signType+"]");
			return new RespondBean(MessageCodeConstant.ALIPAY_PAY_CHECKSIGNERROR) ;
		}
		
		return new RespondBean(content,MessageCodeConstant.ALIPAY_PAY_PAYSUCCESS);
	}
	
	/**
	 * 支付宝  校验数据
	 * @param orderNo   //订单号
	 * @param seller_id //商家编号
	 * @param total_amount //订单金额
	 * @param app_id    //aapid
	 * @return
	 */
	private boolean  checkRespondInfo(String seller_id ,BigDecimal  syMoney ,String total_amount ,String app_id ){
		
		//验证金额  、验证app_id、验证seller_id
		if(!Constant.ALIPAY_APP_ID.equals(app_id)){
			log.info("ZFB异步通知  app_id号有问题");
			return true;
		}
		
		if(!Constant.ALIPAY_SELLERID.equals(seller_id)){
			log.info("ZFB异步通知  seller_id号有问题");
			return true;
		}
		
		if(syMoney!=BigDecimal.valueOf(Double.parseDouble(total_amount==null?"0":total_amount))){
			log.info("ZFB异步通知  total_amount有问题");
			return true;
		}
		
		return  false;
	}

    /**
     * 查询订单接口
     * orderNo 商户订单号 
     * treadNo 交易订单号
     */
	public Map<String,String> ZFBpayAndWithdrawQuery(String orderNo, String treadNo) throws AlipayApiException {
		
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();//创建API对应的request类
    	request.setBizContent("{" +
    	"   \"out_trade_no\":\""+orderNo+"\"," +
    	"   \"trade_no\":\""+treadNo+"\"" +
    	"  }");//设置业务参数
    	AlipayTradeQueryResponse response = alipayClient.execute(request);//通过alipayClient调用API，获得对应的response类
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("orderNo", orderNo);
    	map.put("treadNo", treadNo);
    	//获取交易状态
    	if (response!=null){
    		String tradeStatus =response.getTradeStatus();
    		map.put("tradeStatus", SyTreadCode.valueOf(tradeStatus).getSyTreadCode());
    	}else{
    		map.put("tradeStatus", "none");//结果不确定
    	}
    	
		return map;
	}
	
	/**
	 * 提现接口   --- 规则 ：1.判断提现的金额 是否大于，充值的金额  2.假如整好等于或小于 充值金额，走退款接口 3.假如大于充值金额或者 支付时间 超过一年的，走企业付款通道
	 * @param syPurse
	 * @param syCashAccount
	 * @return
	 * @throws AlipayApiException
	 * @throws Exception 
	 */
	@Transactional(rollbackFor={Exception.class,AlipayApiException.class},isolation=Isolation.REPEATABLE_READ)
	public  Map<String,String>   ZFBpayWithdraw(SyPurse syPurse, SyCashAccount syCashAccount) throws AlipayApiException, Exception{
		
		Map<String,String>  map = new HashMap<String,String>();
		String orderNo = syPurse.gettSyPsOrderno() ;//订单号
		String trade_no = syPurse.gettSyPsTreadno();//支付宝交易号
		BigDecimal withdrawMoney = syPurse.gettSyMoney();//提现金额
		String withdrawOrderNo = syPurse.gettSyPsRefundOrderno();//退款 原交易订单号
		String  userAcount = syPurse.gettSyAccount(); //用户账户
		String withdrawAcount =  syPurse.gettSyPsOutaccount();//收款方账户
		//下单操作
		payAndWithdrawInit(syPurse,syCashAccount);
		String respondCode = "";
		if("1".equals(syPurse.gettSyPsRrefundWay())){ //支付宝退款
			
			AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();//创建API对应的request类
			request.setBizContent("{" +
			"    \"out_trade_no\":\""+withdrawOrderNo+"\"," +
			"    \"trade_no\":\""+trade_no+"\"," +
			"    \"out_request_no\":\""+orderNo+"\"," +
			"    \"refund_amount\":\""+withdrawMoney+"\"," +
			"    \"refund_reason\":\"正常退款\""+
			"  }");//设置业务参数
			AlipayTradeRefundResponse response = alipayClient.execute(request);//通过alipayClient调用API，获得对应的response类
			//String responBody =response.getBody();
			respondCode = response.getCode();
			
			map.put("withdrawOrderNo", withdrawOrderNo);//提现原交易订单号
			map.put("withdrawMoney", response.getRefundFee());//退款金额
			map.put("orderNo", response.getOutTradeNo());//提现交易订单号
			map.put("respondCode", respondCode); //返回码
			map.put("userAccount", userAcount);  //用户账户
			//map.put("orderId", "");  //支付宝 转账唯一单号 ，退款无
			map.put("withdrawAcount", withdrawAcount);//收款方账户
			
			log.info(userAcount+" 支付宝提现接口 退款 修改原充值订单接口.....");
			//修改原充值订单信息
			SyPurse syPurseOrg = new SyPurse();
			syPurseOrg.settSyAccount(userAcount);
			syPurseOrg.settSyPsOrderno(withdrawOrderNo);
			syPurseOrg.settSyPsRefundMoney(withdrawMoney);
			syPurseOrg.settSyPsRefundDate(response.getGmtRefundPay());
			syPurseOrg.settSyPsRrefundWay("1");
			syPurseOrg.settSyPsRefundStatus(SyTreadCode.valueOf("ALIPAY_"+respondCode).getSyTreadCode());
			syPurseOrg.settSyPsRefundOrderno(orderNo);
			
			//更新钱包表 原充值订单
			payAndWithdrawUpdate(syPurseOrg);
			
			log.info(userAcount+" 支付宝提现接口 退款 修改提现订单接口.....");
			//修改提现  订单信息
			SyPurse syPurseWithdraw = new SyPurse();
			syPurseWithdraw.settSyAccount(userAcount);
			syPurseWithdraw.settSyPsOrderno(orderNo);
			syPurseWithdraw.settSyPsStatus(SyTreadCode.valueOf("ALIPAY_"+respondCode).getSyTreadCode());
			syPurseWithdraw.settSyPsRefundMoney(withdrawMoney);
			syPurseWithdraw.settSyPsRefundDate(response.getGmtRefundPay());
			syPurseWithdraw.settSyPsRrefundWay("1");
			syPurseWithdraw.settSyPsRefundStatus(SyTreadCode.valueOf("ALIPAY_"+respondCode).getSyTreadCode());
			syPurseWithdraw.settSyPsRefundOrderno(withdrawOrderNo);
			//更新钱包表 提现订单
			payAndWithdrawUpdate(syPurseWithdraw);
			
			
			if("10000".equals(respondCode)){//退款成功
				SyAccountBalance syAccountBalance = new SyAccountBalance();
				syAccountBalance.settSyAccount(userAcount);
				syAccountBalance.settSyMoney(withdrawMoney.multiply(new BigDecimal(-1)));
				syAccountBalance.settSyUpdateDate(syPurse.gettSyPsCtratetime());
				int count = balanceUpdate(syAccountBalance);//修改余额表
				if(count !=1){
					log.info(userAcount+"---修改余额条数["+count+"]");
					throw new Exception(userAcount+"：修改余额异常");
				}
			}
			
			log.info("支付宝退款接口：账户:"+userAcount+" 退款订单号:"+orderNo+" 订单原订单号:"+withdrawOrderNo+" 原支付宝交易号:"+trade_no+" 退款金额:"+withdrawMoney+" 退款说明:"+response.getSubMsg());
		}else { //支付宝企业 付款接口
			//支付宝转账接口
			//请求支付宝 -- 转账
			AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
			request.setBizContent("{" +
			"    \"out_biz_no\":\""+orderNo+"\"," +
			"    \"payee_type\":\"ALIPAY_LOGONID\"," +
			"    \"payee_account\":\""+withdrawAcount+"\"," +
			"    \"amount\":\""+withdrawMoney+"\"," +
			"    \"payer_show_name\":\"拾缘科技有限公司\"," +
			"    \"remark\":\"提现\"" +
			"  }");
			
			AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
			//String responBody = response.getBody();
			respondCode = response.getCode();
			map.put("withdrawOrderNo", response.getOrderId());//支付宝 转账唯一单号 ，退款为 充值的订单号
			map.put("withdrawMoney",withdrawMoney+"");//退款金额
			map.put("orderNo", orderNo);//交易订单号
			map.put("respondCode", respondCode);
			map.put("userAccount", userAcount);
			map.put("withdrawAcount", withdrawAcount);//收款方账户
			//map.put("orderId",response.getOrderId());  
			
			log.info(userAcount+" 支付宝提现接口 转账 修改提现订单接口.....");
			//修改转账订单信息
			SyPurse syPurseWithdraw = new SyPurse();
			
			syPurseWithdraw.settSyAccount(userAcount);
			syPurseWithdraw.settSyPsOrderno(orderNo);
			syPurseWithdraw.settSyPsStatus(SyTreadCode.valueOf("ALIPAY_"+respondCode).getSyTreadCode());
			syPurseWithdraw.settSyPsRefundMoney(withdrawMoney);
			syPurseWithdraw.settSyPsRefundDate(response.getPayDate()==null||"".equals(response.getPayDate())?new Date():Constant.YYYY_MM_DD_HH_MM_SS.parse(response.getPayDate()));
			syPurseWithdraw.settSyPsRrefundWay("2");
			syPurseWithdraw.settSyPsRefundStatus(SyTreadCode.valueOf("ALIPAY_"+respondCode).getSyTreadCode());
			syPurseWithdraw.settSyPsRefundOrderno(response.getOrderId());
			//更新钱包表 转账订单
			payAndWithdrawUpdate(syPurseWithdraw);

			if("10000".equals(respondCode)){//退款成功
				SyAccountBalance syAccountBalance = new SyAccountBalance();
				syAccountBalance.settSyAccount(userAcount);
				syAccountBalance.settSyMoney(withdrawMoney.multiply(new BigDecimal(-1)));
				syAccountBalance.settSyUpdateDate(syPurse.gettSyPsCtratetime());
				int count = balanceUpdate(syAccountBalance);//修改余额表
				if(count !=1){
					log.info(userAcount+"---修改余额条数["+count+"]");
					throw new Exception(userAcount+"：修改余额异常");
				}
			}
			

			log.info("支付宝企业转账接口：账户:"+userAcount+" 收款方账号："+withdrawAcount+"  提现金额:"+withdrawMoney+" 提现说明:"+response.getSubMsg());
		}
		
		return map;
	}

    /**
     * 提现方式查询
     */
	public Map<String, String> payWithdraw(String userAcount, String amount) {
		
		Map<String,String> map = new HashMap<String,String>();
		
		List<SyPurse>  syPurses = selectWithdrawWay(userAcount,amount);
		if(syPurses == null || syPurses.size()==0){
			map.put("ZFBOutaccount", "");
			map.put("WXOutaccount", "");
			map.put("userAcount", userAcount);
			map.put("amount", amount); 
			map.put("withdrawWay", "2");//payWay  --- 企业付款的方式  0 -- 微信退款   1  ---  支付宝退款   2 --- 企业付款  3 --- 微信支付宝退款都可以
			return  map;
		}else {
			boolean wxBoolean = false;
			boolean zfbBoolean = false;
			map.put("userAcount", userAcount);
			map.put("amount", amount); //退款金额
			for (SyPurse syPurse:syPurses){
				if ("1".equals(syPurse.gettSyFinanced())){ //微信充值
					map.put("WXOutaccount", syPurse.gettSyFinancedAccount());
					map.put("WXwithdrawOrderNo",syPurse.gettSyPsTreadno());
					wxBoolean =true; 
				}else if ("2".equals(syPurse.gettSyFinanced())){//支付宝充值
					map.put("ZFBOutaccount", syPurse.gettSyFinancedAccount());
					map.put("ZFBwithdrawOrderNo",syPurse.gettSyPsTreadno());
					zfbBoolean=true;
				}
			}
			if(wxBoolean && zfbBoolean){
				map.put("withdrawWay", "3");//微信支付宝退款都可以
			}else if (wxBoolean && !zfbBoolean){
				map.put("withdrawWay", "0");//  0 -- 微信退款
			}else if (!wxBoolean && zfbBoolean){
				map.put("withdrawWay", "1");// 1  ---  支付宝退款
			}
		}
		return map;
	}

    /**
     * 提现结果 查询接口
     * @throws AlipayApiException 
     * @throws Exception 
     */
	//@Transactional(rollbackFor={Exception.class,AlipayApiException.class})
	public Map<String, String> payWithdrawSearch(SyPurse syPurse) throws AlipayApiException, Exception {
		
		String  withdrawWay  = syPurse.gettSyPsRrefundWay();
		String  userAcount   = syPurse.gettSyAccount();
		String  respondCode  ="";
		String  withdrawOrderNo = syPurse.gettSyPsRefundOrderno();
		String  orderNo  = syPurse.gettSyPsOrderno();
		BigDecimal  withdrawMoney = syPurse.gettSyMoney();
		Map<String,String>  map = new HashMap<String,String>();
		
		if("1".equals(withdrawWay)){//退款接口 结果查询
			
			//AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
			AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
			request.setBizContent("{" +
			"\"out_trade_no\":\""+withdrawOrderNo+"\"," +
			"\"out_request_no\":\""+orderNo+"\"" +
			"  }");
			AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            respondCode = response.getCode();
            String refundAmount =  response.getTotalAmount();
            BigDecimal refundAmountB = BigDecimal.valueOf(Double.parseDouble(refundAmount));
            Date  date  = new Date(); 
            
			map.put("withdrawOrderNo", withdrawOrderNo);//提现订单号
			map.put("orderNo", response.getOutTradeNo());//提现交易订单号
			map.put("respondCode", respondCode); //返回码
			map.put("userAccount", userAcount);  //用户账户
			map.put("withdrawMoney",refundAmount);//退款金额
			
			/*log.info(userAcount+" 支付宝提现接口 退款 修改原充值订单接口.....");
			//修改原充值订单信息
			SyPurse syPurseOrg = new SyPurse();
			syPurseOrg.settSyAccount(userAcount);
			syPurseOrg.settSyPsOrderno(withdrawOrderNo);
			syPurseOrg.settSyPsRefundMoney(refundAmountB);
			syPurseOrg.settSyPsRefundDate(date);
			syPurseOrg.settSyPsRrefundWay("1");
			syPurseOrg.settSyPsRefundStatus(SyTreadCode.valueOf("ALIPAY_"+respondCode).getSyTreadCode());
			syPurseOrg.settSyPsRefundOrderno(orderNo);
			
			//更新钱包表 原充值订单
			payAndWithdrawUpdate(syPurseOrg);
			
			log.info(userAcount+" 支付宝提现接口 退款 修改提现订单接口.....");
			//修改 提现订单信息
			SyPurse syPurseWithdraw = new SyPurse();
			syPurseWithdraw.settSyAccount(userAcount);
			syPurseWithdraw.settSyPsOrderno(orderNo);
			syPurseWithdraw.settSyPsStatus(SyTreadCode.valueOf("ALIPAY_"+respondCode).getSyTreadCode());
			syPurseWithdraw.settSyPsRefundMoney(refundAmountB);
			syPurseWithdraw.settSyPsRefundDate(date);
			syPurseWithdraw.settSyPsRrefundWay("1");
			syPurseWithdraw.settSyPsRefundStatus(SyTreadCode.valueOf("ALIPAY_"+respondCode).getSyTreadCode());
			syPurseWithdraw.settSyPsRefundOrderno(withdrawOrderNo);
			//更新钱包表 原充值订单
			payAndWithdrawUpdate(syPurseWithdraw);
			
			
			if("10000".equals(respondCode)){//退款成功
				SyAccountBalance syAccountBalance = new SyAccountBalance();
				syAccountBalance.settSyAccount(userAcount);
				syAccountBalance.settSyMoney(refundAmountB.multiply(new BigDecimal(-1)));
				syAccountBalance.settSyUpdateDate(syPurse.gettSyPsCtratetime());
				int count = balanceUpdate(syAccountBalance);//修改余额表
				if(count !=1){
					log.info(userAcount+"---修改余额条数["+count+"]");
					throw new Exception(userAcount+"：修改余额异常");
				}
			}*/
		}else{ //支付宝企业 付款接口 提现查询
			//支付宝转账接口 提现查询
			AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
			request.setBizContent("{" +
			"    \"out_biz_no\":\""+orderNo+"\"," +
			"    \"order_id\":\""+withdrawOrderNo+"\"" +
			"  }");
			AlipayFundTransOrderQueryResponse response = alipayClient.execute(request);
			
			//String responBody = response.getBody();
			respondCode = response.getCode();
			
			map.put("withdrawOrderNo", withdrawOrderNo);//转账 唯一 单号
			map.put("orderNo", orderNo);//交易订单号
			map.put("respondCode", respondCode);
			map.put("userAccount", userAcount);
			
			log.info(userAcount+" 支付宝提现接口 转账 修改提现订单接口.....");
			/*//修改转账订单信息
			SyPurse syPurseWithdraw = new SyPurse();
			
			syPurseWithdraw.settSyAccount(userAcount);
			syPurseWithdraw.settSyPsOrderno(orderNo);
			syPurseWithdraw.settSyPsStatus(SyTreadCode.valueOf("ALIPAY_"+respondCode).getSyTreadCode());
			//syPurseWithdraw.settSyPsRefundMoney(withdrawMoney);
			syPurseWithdraw.settSyPsRefundDate(response.getPayDate()==null||"".equals(response.getPayDate())?new Date():Constant.YYYY_MM_DD_HH_MM_SS.parse(response.getPayDate()));
			syPurseWithdraw.settSyPsRrefundWay("2");
			syPurseWithdraw.settSyPsRefundStatus(SyTreadCode.valueOf("ALIPAY_"+respondCode).getSyTreadCode());
			syPurseWithdraw.settSyPsRefundOrderno(withdrawOrderNo);
			//更新钱包表 转账订单
			payAndWithdrawUpdate(syPurseWithdraw);

			if("10000".equals(respondCode)){//退款成功
				SyAccountBalance syAccountBalance = new SyAccountBalance();
				syAccountBalance.settSyAccount(userAcount);
				syAccountBalance.settSyMoney(withdrawMoney.multiply(new BigDecimal(-1)));
				syAccountBalance.settSyUpdateDate(syPurse.gettSyPsCtratetime());
				int count = balanceUpdate(syAccountBalance);//修改余额表
				if(count !=1){
					log.info(userAcount+"---修改余额条数["+count+"]");
					throw new Exception(userAcount+"：修改余额异常");
				}
			}
			*/

			log.info("支付宝企业转账接口：账户:"+userAcount+"  提现金额:"+withdrawMoney+" 提现说明:"+response.getSubMsg());
		
			
		}
		
		return map;
	}

    /**
     * 支付宝 交易关闭接口
     */
	public Map<String, String> ZFBpayAndWithdrawClose() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
