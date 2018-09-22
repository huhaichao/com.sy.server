package com.sy.server.pc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sy.server.pc.service.UserService;
/**
 * 统计管理
 * @author wangliang
 *
 */
@Controller
@RequestMapping("/Statistical")
public class StatisticalManagerController {
	private Logger log = LoggerFactory.getLogger(StatisticalManagerController.class);
	//@Autowired
	//private Users roleService;
	@Autowired
	private UserService userService;
	/**
	 * 进入访问量统计默认查询
	 * @return
	 */
	@RequestMapping("/statistical/visits")
	public String initAjax() {
		log.info("=================进入访问量统计默认查询===============");
		return "statistical/visits";
	}
	/**
	 * 进入在线用户量统计默认查询
	 * @return
	 */

	@RequestMapping("/statistical/onlines")
	public String initOnlines() {
		log.info("=================进入在线用户量统计默认查询===============");
		return "statistical/onlines";
	}
	/**
	 * 进入注册用户量统计默认查询
	 * 
	 * @return
	 */
	@RequestMapping("/statistical/registers")
	public String initRegisters() {
		log.info("=================进入注册用户量统计查询===============");
		return "statistical/registers";
	}
	/**
	 * 进入在发布信息量统计默认查询
	 */
	@RequestMapping("/statistical/publishs")
	public String initPublishs() {
		log.info("=================进入在发布信息量统计默认查询===============");
		return "statistical/Publishs";
	}
	/**
	 * 进入在交易量信息统计默认查询
	 * @return
	 */
	@RequestMapping("/statistical/trades")
	public String initRrades() {
		log.info("=================进入在交易量信息统计默认查询===============");
		return "statistical/trades";
	}
	
	
	
	
	

}
