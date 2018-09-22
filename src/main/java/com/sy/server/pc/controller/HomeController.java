package com.sy.server.pc.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
	private  Logger   log  = LoggerFactory.getLogger(HomeController.class);
	/**
	 * 嵌入登录页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/login")
    public String initHome(Map<String, Object> model){
		log.info("========================进入登录页面============================");
    	return "home/login";
    }
	
	
	/**
	 * 嵌入登录页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/index")
    public String initIndex(Map<String, Object> model){
		log.info("========================进入登录页面============================");
    	return "home/index";
    }
	
}
