package com.sy.server.pc.service;

import java.util.List;

import com.sy.common.bean.VersionPage;
import com.sy.common.busibean.SyVersion;

/**
 * 手工新增接口实现
 * @author zbb
 *
 */

public interface VersionService {
	
	public List<SyVersion> fallVersions(VersionPage page);
	public int saveVersion(SyVersion syVersion);
	public int updateVersion(SyVersion syVersion);
	public int getCount();
	public int deleteByPrimaryKey(String tSyVersionId);
	public int delVersion(List<String> array);

}
