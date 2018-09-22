package com.sy.server.pc.service;
import java.util.List;
import com.sy.common.bean.Page;
import com.sy.common.bean.SyRole;

/**
 * server 层接口
 * @author yxx
 *
 */
public interface RoleService {

	public List<SyRole> fallRoles(Page page);
	public int saveRole(SyRole syRole);
	public int updateRole(SyRole syRole);
	public int getCount();
	public int deleteByPrimaryKey(String tSyRoleId);
	public int delRoles(List<String> array);
	//public List<SyItemInformation> selectByDetailItem(MapUtil maputil);
	
}
