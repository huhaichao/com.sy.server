package com.sy.server.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sy.common.bean.SubUserGuidejk;
import com.sy.common.bean.UserGuidejk;
import com.sy.server.app.service.SyUserGuideService;

/**
 * 版本相关信息设置 控制层 当使用@RestController时使用返回对象 当使用@Controller时返回视图jsp
 * 
 * @author wangliang
 */
@RestController
@RequestMapping("/guide")
public class UserGuideRestController {

	private Logger log = LoggerFactory.getLogger(UserGuideRestController.class);
	@Autowired
	private SyUserGuideService syUserGuideService;

	/**
	 * 用户指南信息控制
	 * 
	 * @param st
	 * @param vs
	 * @return https://127.0.0.1:8080/sy/server/guide/userGuide?st=Android&vs=4.3
	 */
	@RequestMapping(value = "/userGuide", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody UserGuidejk returnUserGuidejk(@RequestParam(value = "st", required = true) String st,
			@RequestParam(value = "vs", required = true) String vs) {
		log.info("查询用户指南信息.....");
		UserGuidejk vjk = new UserGuidejk();
		SubUserGuidejk subUserGuidejk = new SubUserGuidejk();
		String pageUrl0 = "pageUrl0";
		String pageUrl1 = "pageUrl1";
		if(st.equals("Android")){
			vjk.setData(pageUrl0);
			vjk.setCode("1");
			vjk.setMsg("210");
		}else{
			vjk.setData(pageUrl1);
			vjk.setCode("1");
			vjk.setMsg("211");
		}
		/*@SuppressWarnings("unchecked")
		List<SyUserGuide> versions = (List<SyUserGuide>) syUserGuideService.selectByItem(mu);
		if (versions.size() > 0) {
			Iterator iterator = versions.iterator();
			while (iterator.hasNext()) {
				SyUserGuide sy = (SyUserGuide) iterator.next();
				subUserGuidejk.settSyUserguideName(sy.gettSyUserguideName());
				vjk.getData().add(subUserGuidejk);
			}
			vjk.setCode("1");
			vjk.setMsg("查询用户指南信息成功......");
		} else {
			vjk.setCode("0");
			vjk.setMsg("查询用户指南信息失败......");
		}*/
		log.info("查询用户指南信息完成.....");
		return vjk;
	}

}
