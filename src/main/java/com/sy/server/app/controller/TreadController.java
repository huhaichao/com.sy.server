package com.sy.server.app.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sy.common.bean.RespondBean;
import com.sy.common.bean.SyTread;
import com.sy.common.busibean.SyMessage;
import com.sy.common.constant.Constant;
import com.sy.common.enums.MessageCodeConstant;
import com.sy.common.utils.IdGenerator;
import com.sy.common.utils.MD5;
import com.sy.server.app.service.SyMessageService;
import com.sy.server.app.service.SyUserService;
import com.sy.server.app.service.TreadServer;
import com.sy.server.cache.CacheOperation;

@RestController
@RequestMapping("/tread")
public class TreadController {
	
	private Logger  log = Logger.getLogger(TreadController.class);
	
	@Autowired
	private CacheOperation cacheOperation;
	
	@Autowired
	private  SyUserService syUserService;
	
	@Autowired
	private  SyMessageService syMessageService;
	
	@Autowired
	private TreadServer  treadServer;
 	
    /**
     * 同步 交易接口
     * @param lostUserId   丢失用户id
     * @param pickupUserid 拾到用户id
     * @param messageId    消息id
     * @param payPassword  支付密码
     * @param sign         签名
     * @param vs           版本
     * @return
     */
	@PostMapping(value="/treadSyn",produces="application/json;charset=utf-8")
	public RespondBean doTreadSyn(@RequestParam(value="lostUserId",required = true) String lostUserId,
			@RequestParam(value="pickupUserid",required = true) String pickupUserid,
			@RequestParam(value="messageId",required = true) String messageId,
			@RequestParam(value="payPassword",required = true) String payPassword,
			@RequestParam(value="sign",required = true) String sign,
			@RequestParam(value="vs",required = true)String vs){
		
		//验证签名
		String signkey =  (String)cacheOperation.getFromDaysCache(Constant.SIGNKEY+lostUserId);
		Map<String,String> para = new HashMap<String,String>();
		para.put("lostUserId", lostUserId);
		para.put("pickupUserid", pickupUserid);
		para.put("messageId", messageId);
		para.put("payPassword", payPassword);
		para.put("sign", sign);
		para.put("vs", vs);
		if(!MD5.encryption4JHZF(para, signkey).equals(sign)){
		    log.info("同步交易接口 上送报文延签失败");	
			return  new RespondBean(MessageCodeConstant.PAY_SIGNERROR);
		}
		
		Integer lostUserIdInt = Integer.parseInt(lostUserId) ;
		Integer pickupUseridInt = Integer.parseInt(pickupUserid);
		
		List<Map<String,Object>> users = syUserService.queryLostAndPickup(lostUserIdInt, pickupUseridInt);
		if (users == null || users.size()==0 || users.isEmpty()  || users.size()!=2){
			log.info("同步交易接口 对方账户不存在......");	
			return  new RespondBean(MessageCodeConstant.TREAD_ACCOUNTEXCEPTION);
		}
		
		
		SyTread  syTread  = new SyTread();
		Integer messageIdInt = Integer.parseInt(messageId);
		Map<String,Object> messageMap = syMessageService.selectByMessageIdFromView(messageIdInt);
		
		Object moneyO = messageMap.get("T_SY_BOUNTY_MONEY");
		Double  moneyD =moneyO==null?0.0:Double.parseDouble(moneyO.toString());
		Map<String,Double> mapBalance = new HashMap<String,Double>();
		for (Map<String,Object> map : users){
			String userid = map.get("T_SY_USER_ID").toString();
			String account = map.get("T_SY_ACCOUNT").toString();
			String syPayPassword = map.get("T_SY_PAY_PASSWORD").toString();
			String accountMoney = map.get("T_SY_MONEY").toString(); //账户余额
			String coolMoney = map.get("T_SY_COOL_MONEY").toString();//冻结金额
			String syBalance  = map.get("t_sy_balance").toString();//账户金额+冻结金额
			
			if(lostUserId.equals(userid)){
				syTread.settSyTreadOutaccount(account);
				Double accountMoneyLost = accountMoney == null ?0.00:Double.parseDouble(accountMoney);
				Double coolMoneyLost = coolMoney == null ?0.00:Double.parseDouble(coolMoney);
				Double syBalanceLost = syBalance == null ?0.00:Double.parseDouble(syBalance);
				
				
				
				if(moneyD > syBalanceLost){ //悬赏金额  大于 总余额 提示余额不足
					log.info(account+"悬赏金额["+moneyD+"]大于账户余额["+syBalanceLost+"]");
					return  new RespondBean(MessageCodeConstant.TREAD_ACCOUNTBALANCENOMENOY);
				}
				
				//验证口令
				if(moneyD>0 && Integer.parseInt(payPassword) != Integer.parseInt(syPayPassword)){
					log.info(messageId+"验证口令 无效");
					return  new RespondBean(MessageCodeConstant.TREAD_TREADPASSWORDFAIL);
				}
				
				mapBalance.put("accountMoneyLost", accountMoneyLost); //失主的账户余额
				mapBalance.put("coolMoneyLost", coolMoneyLost);  //失主的冻结金额
				mapBalance.put("syBalanceLost", syBalanceLost);  //失主的总金额  = 账户余额+冻结金额
			}else if (pickupUserid.equals(userid)){
				syTread.settSyTreadInaccount(account);
			}
		}
		syTread.settSyDeleleStatus("0");
		syTread.settSyMessageId(messageIdInt);
		syTread.settSyTreadCreatedate(new Date());
		syTread.settSyTreadAmount(moneyD);
		syTread.settSyTreadStatus("success");
		String tSyTreadTreadno = IdGenerator.getInstance().generate();
		syTread.settSyTreadTreadno(tSyTreadTreadno);
		syTread.settSyTreadType("tread");
		
		Map<String,String> mapReault  = treadServer.usersTreadSyn(syTread, mapBalance);
		String signRes =MD5.encryption4JHZF(mapReault, signkey);
		mapReault.put("sign", signRes);
		mapReault.put("lostUserId", lostUserId);
		mapReault.put("pickupUserid", pickupUserid);
		mapReault.put("messageId", messageId);
		log.info(messageId +"同步交易成功");
		return  new RespondBean(mapReault,MessageCodeConstant.TREAD_SUCCESS);
	}
	
	
	
	

}
