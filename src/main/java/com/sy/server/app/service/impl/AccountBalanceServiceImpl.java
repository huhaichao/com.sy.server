package com.sy.server.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.bean.SyAccountBalance;
import com.sy.common.bean.SyAccountBalanceExample;
import com.sy.server.app.service.AccountBalanceService;
import com.sy.server.mapper.SyAccountBalanceMapper;

@Service
public class AccountBalanceServiceImpl implements AccountBalanceService {
	
	
	@Autowired
	private  SyAccountBalanceMapper  syAccountBalanceMapper;
	
    /**
     * 余额查询接口
     */
	public SyAccountBalance selectBalance(String userId) {
		
		SyAccountBalanceExample  example  = new SyAccountBalanceExample();
		
		example.createCriteria().andTSyUserIdEqualTo(userId);
		
		List<SyAccountBalance>  list = syAccountBalanceMapper.selectByExample(example);
		
		return list.get(0);
	}
	


    /**
     * 修改 余额及冻结金额接口
     */
	public int updateBalanceAndCoolMoney(SyAccountBalance syAccountBalance) {
		return syAccountBalanceMapper.updateBySyAccountSelective(syAccountBalance);
	}

}
