package com.sy.server.pc.service;
import java.util.List;

import com.sy.common.bean.UserPage;
import com.sy.common.busibean.SyUser;

public interface UserService {
	
	public int getCount();
	
	public List<SyUser> fallUsers(UserPage userPage);
}
