package com.sy.server.pc.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.common.bean.VersionPage;
import com.sy.common.busibean.SyVersion;
import com.sy.server.mapper.SyVersionMapper;
import com.sy.server.pc.service.VersionService;

@Service
public class VersionServiceImp implements VersionService {

	@Autowired
	private SyVersionMapper syVersionMapper;
	public List<SyVersion> fallVersions(VersionPage page) {
		// TODO Auto-generated method stub
		return syVersionMapper.fallVersions(page);
	}

	public int saveVersion(SyVersion syVersion) {
		// TODO Auto-generated method stub
		return syVersionMapper.insert(syVersion);
	}

	public int updateVersion(SyVersion syVersion) {
		// TODO Auto-generated method stub
		return syVersionMapper.updateByPrimaryKey(syVersion);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return syVersionMapper.getCount();
	}

	public int deleteByPrimaryKey(String tSyVersionId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int delVersion(List<String> array) {
		// TODO Auto-generated method stub
		return syVersionMapper.delVersion(array);
	}

	
}
