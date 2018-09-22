package com.sy.server.app.service;

import java.util.List;

import com.sy.common.bean.SyAccountBalance;
import com.sy.common.bean.SyCashAccount;
import com.sy.common.bean.SyPurse;
import com.sy.common.bean.SyTread;

/**
 * 
 * 拾缘支付、提现、交易接口
 * @author huchao
 *
 */
public interface SyPayService {
	
	/**
	 * 支付、提现接口
	 * @param syPurse
	 * @param syBalance
	 * @param syCashAccount
	 */
	public  void  payAndWithdrawInit(SyPurse syPurse,SyCashAccount syCashAccount);
	
	/**
	 * 支付、提现修改接口
	 * @param syPurse
	 */
	public  void  payAndWithdrawUpdate(SyPurse syPurse/*,SyCashAccount syCashAccount*/);
	
	/**
	 * 用户之间的交易接口
	 * @param syBalance
	 * @param syCashAccount
	 */
	public void  userTreadInit(SyTread syTread, SyCashAccount syCashAccount1,SyCashAccount syCashAccount2);
	
	
	/**
	 * 用户之间的修改接口
	 * @param syBalance
	 * @param syCashAccount
	 */
	public void  userTreadUpdate(SyTread syTread/*,SyCashAccount syCashAccount,SyCashAccount syCashAccount2*/);
	
	/**
	 * 操作余额表
	 * @param syBalance
	 */
    public int balanceUpdate(SyAccountBalance syBalance);
    
    /**
     * 查询钱包实体
     * @param psOrderno
     * @return
     */
    public List<SyPurse>  selectByPsOrderno(String psOrderno);
    
    
}
