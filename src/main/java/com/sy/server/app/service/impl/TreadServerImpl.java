package com.sy.server.app.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sy.common.bean.SyAccountBalance;
import com.sy.common.bean.SyCashAccount;
import com.sy.common.bean.SyTread;
import com.sy.common.busibean.SyMessage;
import com.sy.common.busibean.SyMessageExample;
import com.sy.common.utils.IdGenerator;
import com.sy.server.app.service.SyPayAbstractService;
import com.sy.server.app.service.TreadServer;
import com.sy.server.mapper.SyMessageMapper;

@Service
public class TreadServerImpl extends SyPayAbstractService implements TreadServer {
	
	
	@Autowired
    private SyMessageMapper syMessageMapper ;
	
    /**
     * 同步交易操作 操作
     */
	@Transactional(rollbackFor={Exception.class})
	public Map<String, String> usersTreadSyn(SyTread syTread, Map<String, Double> map) {
		// TODO Auto-generated method stub
		Double  XSmoney = syTread.gettSyTreadAmount(); //悬赏金额
		 SyCashAccount syCashAccount1 = null ;SyCashAccount syCashAccount2 = null;
		Date  createDate = syTread.gettSyTreadCreatedate();
		String messageId = syTread.gettSyMessageId().toString();
		
		if(XSmoney>0) { //对于有悬赏金额的，添加余额操作，以及资金流水操作。
			BigDecimal pickUpMoney = new BigDecimal(XSmoney); //拾主 应该得到的钱
			BigDecimal lostMoney = pickUpMoney.multiply(new BigDecimal(-1)); //失主 应该失去的钱
			
			syCashAccount1 = new SyCashAccount(); //失主的资金流水
			syCashAccount1.settSyCashCreateDate(createDate);
			syCashAccount1.settSyMessageId(messageId);
			syCashAccount1.settSyTreadTreadno(syTread.gettSyTreadTreadno());
			syCashAccount1.settSyCashAmount(lostMoney);
			syCashAccount1.settSyAccount(syTread.gettSyTreadOutaccount());
			syCashAccount1.settSyPsOrderno("123456");
			syCashAccount1.settSyCashAccountNo(IdGenerator.getInstance().generate());
			syCashAccount1.settSyCashOutorinStatus("1");
			syCashAccount1.settSyCashType("2");
			
			syCashAccount2 = new SyCashAccount(); //拾主的资金流水
			syCashAccount2.settSyAccount(syTread.gettSyTreadInaccount());
			syCashAccount2.settSyCashCreateDate(createDate);
			syCashAccount2.settSyCashAccountNo(IdGenerator.getInstance().generate());
			syCashAccount2.settSyCashAmount(pickUpMoney);
			syCashAccount2.settSyCashOutorinStatus("0");
			syCashAccount2.settSyCashType("2");
			syCashAccount2.settSyDeleteStatus("0");
			syCashAccount2.settSyMessageId(messageId);
			syCashAccount2.settSyPsOrderno("123456");
			syCashAccount2.settSyTreadTreadno(syTread.gettSyTreadTreadno());
			
		}
		userTreadInit(syTread,syCashAccount1,syCashAccount2);
		
		if (XSmoney>0){
			Double accountMoneyLost = map.get("accountMoneyLost"); //失主的账户余额
			Double coolMoneyLost =map.get("coolMoneyLost");  //失主的冻结金额
			Double  syBalanceLost= map.get("syBalanceLost");  //失主的总金额  = 账户余额+冻结金额
			BigDecimal  XSmoneyB = new BigDecimal(XSmoney);
			SyAccountBalance syBalance1 =new  SyAccountBalance(); //修改失主的余额
			SyAccountBalance syBalance2 =new  SyAccountBalance(); //修改拾主的余额
			if (XSmoney<=coolMoneyLost){//冻结的金额大于悬赏金额 --- 扣除悬赏金额
				BigDecimal lostMoney = XSmoneyB.multiply(new BigDecimal(-1)); //失主 应该失去的钱
				syBalance1.settSyCoolMoney(lostMoney);
			}else if (XSmoney>coolMoneyLost){//冻结的金额 小于悬赏的金额，需要从余额中 扣除
				BigDecimal lostBalacen = new BigDecimal(coolMoneyLost-XSmoney);
				BigDecimal lostMoney = XSmoneyB.multiply(new BigDecimal(-1)); //失主 应该失去的钱
				syBalance1.settSyCoolMoney(lostMoney);
				syBalance1.settSyMoney(lostBalacen);
			}
			
			syBalance1.settSyAccount(syTread.gettSyTreadOutaccount());
			balanceUpdate(syBalance1);
			syBalance2.settSyMoney(XSmoneyB);
			syBalance2.settSyAccount(syTread.gettSyTreadOutaccount());
			balanceUpdate(syBalance2);
			
		}
		//修改消息表改成已完成
		SyMessage	syMessage  = new SyMessage();
		syMessage.settSyMessageState(4);//暂定标识   交易 --- 已完成 
		SyMessageExample  syMessageExample  = new SyMessageExample();
		syMessageExample.createCriteria().andTSyMessageIdEqualTo(syTread.gettSyMessageId());
		syMessageMapper.updateByExampleSelective(syMessage, syMessageExample);
		Map<String,String> mapResult = new HashMap<String,String>();
		
		mapResult.put("treadState", "success");//交易成功
		mapResult.put("xsMoney", XSmoney.toString());
 		return mapResult;
	}
	

}
