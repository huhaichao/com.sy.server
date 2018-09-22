package com.sy.server.app.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sy.common.bean.SyAccountBalance;
import com.sy.common.bean.SyCashAccount;
import com.sy.common.bean.SyPurse;
import com.sy.common.bean.SyPurseExample;
import com.sy.common.bean.SyTread;
import com.sy.server.mapper.SyAccountBalanceMapper;
import com.sy.server.mapper.SyCashAccountMapper;
import com.sy.server.mapper.SyPurseMapper;
import com.sy.server.mapper.SyTreadMapper;

/**
 * 拾缘 支付、提现、交易接口操作类
 * @author huchao
 *
 */
public abstract class SyPayAbstractService implements SyPayService {
	
	@Autowired
	private SyPurseMapper syPurseMapper;
	@Autowired
	private SyAccountBalanceMapper syAccountBalanceMapper;
	@Autowired
	private  SyCashAccountMapper  syCashAccountMapper;
	@Autowired
	private  SyTreadMapper  syTreadMapper;
	
    private Logger  log = Logger.getLogger(SyPayAbstractService.class);

    /**
     * 充值|提现预下单操作
     * @param syPurse
     * @param syBalance
     * @param syCashAccount
     */
	public void payAndWithdrawInit(SyPurse syPurse, SyCashAccount syCashAccount){
		log.info("提现|充值预下单操作....");
		syPurseMapper.insert(syPurse);
		log.info("提现|充值预下单操作....添加钱包记录");
		syCashAccountMapper.insert(syCashAccount);
		log.info("提现|充值预下单操作....添加资金流水记录");
	}
	
	/**
     * 充值|提现成功操作
     * @param syPurse
     * @param syBalance
     * @param syCashAccount
     */
	public void payAndWithdrawUpdate(SyPurse syPurse/*, SyCashAccount syCashAccount*/){
		log.info("提现|充值状态修改操作....");
		syPurseMapper.updateByTsypsorderno(syPurse);
		log.info("提现|充值状态修改操作....添加钱包记录");
		/*syCashAccountMapper.insert(syCashAccount);
		log.info("提现|充值预下单操作....添加资金流水记录");*/
	}
    
	/**
	 * 交易预下单操作
	 * @param syTread
	 * @param syCashAccount1
	 * @param syCashAccount2
	 */
	public void userTreadInit(SyTread syTread, SyCashAccount syCashAccount1,SyCashAccount syCashAccount2) {
		log.info("交易预下单操作....");
		//添加一条交易记录
		syTreadMapper.insert(syTread);
		log.info("交易预下单操作....添加交易记录");
		//涉及到交易双方 -- 添加2条交易流水  -- 只有涉及到金额 流转的才添加资金流水
		if (syCashAccount1 !=null) 
		 syCashAccountMapper.insert(syCashAccount1);
		if (syCashAccount2 !=null)
		 syCashAccountMapper.insert(syCashAccount2);
		log.info("交易预下单操作....添加交易双方流水记录");
	}
	
	/**
	 * 交易状态修改操作
	 * @param syTread
	 * @param syCashAccount1
	 * @param syCashAccount2
	 */
	public void userTreadUpdate(SyTread syTread/*, SyCashAccount syCashAccount1,SyCashAccount syCashAccount2*/) {
		log.info("交易状态修改操作....");
		//添加一条交易记录
		syTreadMapper.updateByPrimaryKey(syTread);
		log.info("交易状态修改操作....添加交易记录");
		/*//涉及到交易双方 -- 添加2条交易流水
		syCashAccountMapper.insert(syCashAccount1);
		syCashAccountMapper.insert(syCashAccount2);
		log.info("交易预下单操作....添加交易双方流水记录");*/
	}

	/**
	 * 修改余额操作
	 */
	public int balanceUpdate(SyAccountBalance syBalance) {
		String syAccount =syBalance.gettSyAccount();
		//修改余额
		log.info("修改余额操作开始...."+syAccount);
		/*synchronized (syAccount){*/
		int count =	syAccountBalanceMapper.updateBySyAccountSelective(syBalance);
		/*}*/
		log.info("修改余额操作结束...."+syAccount);
		
		return count;
	}
    
	/**
	 * 根据充值提现 订单号查询记录 确认是否存在此订单
	 */
    public List<SyPurse>  selectByPsOrderno(String psOrderno){
    	 log.info("获取充值提现记录.....");
		 SyPurseExample  syPurseExample = new SyPurseExample();
		 syPurseExample.createCriteria().andTSyPsOrdernoEqualTo(psOrderno);
		 return syPurseMapper.selectByExample(syPurseExample);
    }
   
   /**
    * 获取用户   以往的支付  的退款方式
    * 
    * @param userAcount  提现拾缘账户
    * @param userAcount  提现金额
    * @return
    */
   public  List<SyPurse> selectWithdrawWay(String userAcount,String amount){
	   
	   /*SyPurseExample  syPurseExample = new SyPurseExample();
	   
	   
	   syPurseExample.setOrderByClause("T_SY_MONEY asc"); //按照金额  从小到大 排序
	   syPurseExample.createCriteria().andTSyAccountEqualTo(userAcount)
	   .andTSyMoneyGreaterThanOrEqualTo(money)  //大于等于退款金额的记录
	   .andTSyBusinessTypeEqualTo("0").andTSyPsRefundStatusEqualTo("2");  //提现---未退款的账户
*/	   
	   SyPurse  syPurse = new SyPurse();
	   BigDecimal money =  BigDecimal.valueOf(Double.parseDouble(amount));
	   syPurse.settSyAccount(userAcount);
	   syPurse.settSyMoney(money);
	   
	   return syPurseMapper.selectPayWay(syPurse);
   }
   
     
}
