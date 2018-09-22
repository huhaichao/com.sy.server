package com.sy.server.app.controller;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sy.common.annotation.MapUtil;
import com.sy.common.bean.SubVersionjk;
import com.sy.common.bean.Versionjk;
import com.sy.common.busibean.SyVersion;
import com.sy.server.app.service.SyVersionService;

/**
 * 版本相关信息设置 控制层 当使用@RestController时使用返回对象 当使用@Controller时返回视图jsp
 * 
 * @author wangliang
 */
@RestController
@RequestMapping("/vs")
public class VersionRestController {

	private Logger log = LoggerFactory.getLogger(VersionRestController.class);
	@Autowired
	private SyVersionService syVersionService;

	/**
	 * 依据应用平台类型及版本号获取相应的版本
	 * 接口地址:https://127.0.0.1:8080/sy/server/vs/version?st=Android&vs=4.3
	 * 接口参数:
	 * @param st 平台类型 
	 * @param vs 应用版本号
	 * @return https://127.0.0.1:8080/sy/server/vs/version?st=Android&vs=4.3
	 */
	@RequestMapping(value = "/version", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Versionjk returnVersionjk(@RequestParam(value = "st", required = true) String st,
			@RequestParam(value = "vs", required = true) String vs) {
		log.info("版本信息开始登陆.....");
		Versionjk vjk = new Versionjk();
		SubVersionjk subVersionjk = new SubVersionjk();
		MapUtil mu = new MapUtil();
		mu.setVersionType(st);
		mu.setVersionNum(vs);
		@SuppressWarnings("unchecked")
		List<SyVersion> versions = (List<SyVersion>) syVersionService.selectByItem(mu);
		if (versions.size() > 0) {
			Iterator iterator = versions.iterator();
			while (iterator.hasNext()) {
				SyVersion syVersion = (SyVersion) iterator.next();
				subVersionjk.setVersionDes(syVersion.gettSyCreateinfo());
				subVersionjk.setVersionNum(syVersion.gettSyVersionNumber());
				subVersionjk.setVersionUrl(syVersion.gettSyBz2());
				vjk.getData().add(subVersionjk);
			}
			vjk.setCode("1");
			vjk.setMsg("220");
		} else {
			vjk.setCode("0");
			vjk.setMsg("221");
		}
		log.info("查询版本信息完成.....");
		return vjk;
	}

}
