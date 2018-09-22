package com.sy.server.pc.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sy.common.bean.Page;
import com.sy.common.bean.SyRole;
import com.sy.server.mapper.SyRoleMapper;
import com.sy.server.pc.service.RoleService;
/**
 * 实现角色信息的servvice层数据
 * @author lenovo
 *
 */
@Service
public class RoleServiceImp implements RoleService{
    /**
     * 根据SyRoleExample类获取符合条件的角色数据
     */
	@Autowired
	private SyRoleMapper syRoleMapper;
	public List<SyRole> fallRoles(Page page) {
		// TODO Auto-generated method stub
		return syRoleMapper.fallRoles(page);
	}
	public int saveRole(SyRole syRole) {
		// TODO Auto-generated method stub
		return syRoleMapper.insert(syRole);
	}
	public int updateRole(SyRole syRole) {
		// TODO Auto-generated method stub
		return syRoleMapper.updateByPrimaryKey(syRole);
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return syRoleMapper.getCount();
	}
	public int deleteByPrimaryKey(String tSyRoleId) {
		// TODO Auto-generated method stub
		return syRoleMapper.deleteByPrimaryKey(tSyRoleId);
	}
	public int delRoles(List<String> array) {
		// TODO Auto-generated method stub
		return syRoleMapper.delRoles(array);
	}

}
