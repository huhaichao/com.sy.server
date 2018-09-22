package com.sy.server.pc.service.imp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.bean.UserPage;
import com.sy.common.busibean.SyUser;
import com.sy.server.mapper.SyUserMapper;
import com.sy.server.pc.service.UserService;
@Service
public class UserServiceImp implements UserService{
	@Autowired
	public SyUserMapper syUserMapper;
	/**
	 * 根据userpage获取所有的数据信息
	 */
	/*public List<SyUser> fallUsers(UserPage userPage) {
		// TODO Auto-generated method stub
		return syUserMapper.fallUsers(userPage);
	}*/
	/**
	 * 获取所有的数据条数
	 */
	/*public int getCount() {
		// TODO Auto-generated method stub
		return syUserMapper.getCount();
	}*/

	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<SyUser> fallUsers(UserPage userPage) {
		// TODO Auto-generated method stub
		return null;
	}

}
