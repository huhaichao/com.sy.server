package com.sy.server.app.service;

import com.sy.common.bean.SyAccountBalance;

public interface AccountBalanceService {
	
	
	public  SyAccountBalance selectBalance(String userId);
	
	public  int updateBalanceAndCoolMoney(SyAccountBalance syAccountBalance);

}
