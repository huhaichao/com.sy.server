/*package com.sy.server;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.sy.common.bean.SyCashAccount;
import com.sy.common.bean.SyPurse;
import com.sy.common.utils.IdGenerator;
import com.sy.server.app.controller.AliPayController;
import com.sy.server.app.service.SyZFBPayService;
import com.sy.server.cache.CacheOperation;

*//**
 * 缓存测试
 * @author huchao
 *
 *//*
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=SyApplication.class)
public class AliPayTest {
	

	
	private Logger  log = Logger.getLogger(AliPayTest.class);
	
	@Resource
	private  SyZFBPayService  SyZFBPayService  ;
	
	@Autowired
	private CacheOperation cacheOperation;
	
	*//**
	 * 支付宝充值  预下单操作
	 * @param totalAmount
	 * @param vs
	 * @return
	 *//*
	//支付宝预下单接口
	@Test
	public void preAlipay(){
		try {
			String  amount ="1.0";
			String  userAccount = "1581882159520170509";
			
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
			Object respondBean = SyZFBPayService.ZFBpayAndWithdrawIint(syPurse, syCashAccount);
			log.info(respondBean);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("app充值操作失败："+e.getMessage());
		}
		
	}
    
    
	*//**
	 * 支付宝    异步通知服务下单结果接口
	 * @param userAccount
	 * @param orderNo
	 * @param psStatus
	 * @param financedAccount
	 * @param vs
	 * @return
	 *//*
    @PostMapping(value="/payAsynNotify",produces="application/json;charset=UTF-8")
	public String  aliPayAsynNotify(HttpServletRequest request,HttpServletResponse response){
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
    	String  result = SyZFBPayService.ZFBpayAndWithdrawAsynNotify(params);
    	log.info("支付宝下单回调接口结果:"+result);
    	return  result;
	}
    
    
    *//**
	 * app端返回支付宝同步 通知结果，验证签名
	 * @param userAccount
	 * @param orderNo
	 * @param psStatus
	 * @param financedAccount
	 * @param vs
	 * @return
	 *//*
    @PostMapping(value="/paySynNotify",produces="application/json;charset=UTF-8")
	public RespondBean  aliPaySynNotify(@RequestParam(value="userAccount",required = true) String userAccount,@RequestParam(value="aliSynRespond") String aliSynRespond,
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
    	}else{
    		MessageCodeConstant messageCodeConstant = MessageCodeConstant.valueOf("ALIPAY_"+resultStatus);
    		if(messageCodeConstant==null){
    			messageCodeConstant =MessageCodeConstant.ALIPAY_PAY_PAYNORESULT;
    		}
    		respondBean = new RespondBean(messageCodeConstant);
    	}
    	
    	return respondBean;
	}
	
 
}
*/