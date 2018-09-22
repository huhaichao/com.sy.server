package com.sy.server.app.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sy.common.bean.RespondBean;
import com.sy.common.bean.SyAccountBalance;
import com.sy.common.constant.Constant;
import com.sy.common.enums.MessageCodeConstant;
import com.sy.common.utils.MD5;
import com.sy.server.app.service.AccountBalanceService;
import com.sy.server.cache.CacheOperation;

/**
 * 对于用户账户余额 开放接口
 * @author huchao
 *
 */
@RestController
@RequestMapping("/accountBalance")
public class AccountBalanceController {
	
	private Logger  log = Logger.getLogger(AccountBalanceController.class);
	
	@Autowired
	private AccountBalanceService accountBalanceService;
	
	@Autowired
	private CacheOperation cacheOperation;
	
	/**
	 * 用户id
	 * @param userId
	 * @return
	 */
	@PostMapping("/searchBalance")
	public RespondBean searchBalance(String userId,String vs){
		
		log.info("账户余额查询接口入参["+userId+"]");
		
		RespondBean   respondBean  = null;
		try {
			SyAccountBalance  syAccountBalance = accountBalanceService.selectBalance(userId);
			BigDecimal  amountBalance =  syAccountBalance.gettSyMoney() ;//账户余额
			BigDecimal  amountCoolBalance = syAccountBalance.gettSyMoney() ;//账户冻结余额
			
			Map<String,Object> mapResult = new HashMap<String,Object>(); 
			
			mapResult.put("userid", userId);
			mapResult.put("amountBalance", amountBalance);
			mapResult.put("amountCoolBalance", amountCoolBalance);
			
			respondBean = new RespondBean(mapResult,MessageCodeConstant.SUCESSS);
			log.info("账户余额查询接口入参["+userId+"]  查询成功");
		} catch (Exception e) {
			log.info("账户余额查询接口入参["+userId+"]  查询异常:"+e.getMessage());
			respondBean = new RespondBean(MessageCodeConstant.EXCEPTION);
		}
		return respondBean;
	}

	
	/**
	 * 冻结余额接口
	 * @param userId
	 * @param coolMoney
	 * @param vs
	 * @param userAccpunt
	 * @param sign
	 * @return
	 */
	@PostMapping("/coolBalance")
	public  RespondBean coolBalance(String userId,String coolMoney,String vs ,String userAccount,String sign){
		
		log.info("冻结余额 接口 入参userId=["+userId+"]coolMoney=["+coolMoney+"]userAccount=["+userAccount+"]sign=["+sign+"]vs=["+vs+"]");

		RespondBean   respondBean  = null;
		try {
			String signkey =  (String)cacheOperation.getFromDaysCache(Constant.SIGNKEY+userId);
			Map<String,String> para = new HashMap<String,String>();
			para.put("userid", userId);
			para.put("coolMoney", coolMoney);
			para.put("userAccount", userAccount);
			para.put("vs", vs);
			if(!MD5.encryption4JHZF(para, signkey).equals(sign)){
			    log.info("冻结余额接口 上送报文延签失败");	
				return  new RespondBean(MessageCodeConstant.PAY_SIGNERROR);
			}
			
			Date create_time = new Date();
			
			BigDecimal  coolMoneyB =   BigDecimal.valueOf(Double.parseDouble(coolMoney));
			
			SyAccountBalance  syAccountBalanceOld = accountBalanceService.selectBalance(userId);
			BigDecimal  amountBalance =  syAccountBalanceOld.gettSyMoney() ;//账户余额
			if(coolMoneyB.compareTo(amountBalance)>0){
				return  new RespondBean(MessageCodeConstant.TREAD_ACCOUNTBALANCENOMENOY);
			}
			
			SyAccountBalance syAccountBalance  = new SyAccountBalance();
			
			syAccountBalance.settSyMoney(coolMoneyB.multiply(new BigDecimal(-1)));
			syAccountBalance.settSyCoolMoney(coolMoneyB);
			syAccountBalance.settSyUserId(userId);
			syAccountBalance.settSyAccount(userAccount);
			syAccountBalance.settSyUpdateDate(create_time);
			syAccountBalance.settSyCoolDate(create_time);
			
			accountBalanceService.updateBalanceAndCoolMoney(syAccountBalance);
			
            Map<String,String> mapResult = new HashMap<String,String>(); 
			mapResult.put("userid", userId);
			mapResult.put("coolMoney", coolMoney);
			mapResult.put("status", "SUCCESS");
			
			String signRes =MD5.encryption4JHZF(mapResult, signkey);
			mapResult.put("sign", signRes);
			respondBean = new RespondBean(mapResult,MessageCodeConstant.SUCESSS);
		} catch (Exception e) {
			log.info("冻结余额userId=["+userId+"]  查询异常:"+e.getMessage());
			respondBean = new RespondBean(MessageCodeConstant.EXCEPTION);
		}
		return respondBean;
	}
	
	
	
	
	
}
