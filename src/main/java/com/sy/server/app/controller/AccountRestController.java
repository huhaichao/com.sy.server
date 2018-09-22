package com.sy.server.app.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 账户相关处理  控制层
 * 当使用@RestController时使用返回对象
 * 当使用@Controller时返回视图jsp
 * @author yumeng
 *
 */
@RestController
@RequestMapping("/account")
public class AccountRestController {
	
	private  Logger   log  = LoggerFactory.getLogger(AppRestController.class);
	
//	@Autowired
//	private  DemoService  demoService;
//	
//	/**
//	 * https://127.0.0.1:8080/sy/server/app/query?id=123456
//	 * 查询
//	 * @return
//	 */
//	@RequestMapping(value="/query",method={RequestMethod.GET,RequestMethod.POST})
//	public @ResponseBody String query(@RequestParam(value="id",required=false)String id){
//		log.info("demo query start.....");
//		return  "json{id:234;name:df;}";
//	}
//	
//	/**
//	 * https://127.0.0.1:8080/sy/server/app/save?name=demo&desc=demo-test
//	 * 保存
//	 * @param name
//	 * @param desc
//	 * @return
//	 */
//	@RequestMapping(value="/save",method={RequestMethod.GET,RequestMethod.POST})
//	public @ResponseBody String Save(@RequestParam(value="name",required=false)String name,
//			@RequestParam(value="desc",required=false)String desc                        
//			){
//		   log.info("demo save start.....");
//		   Demo  d  = new Demo();
//		   d.setId(Long.valueOf("1234567"));
//		   d.setDemoname(name);
//		   d.setDate(new Date());
//		   d.setDemodesc(desc.getBytes());
//		   demoService.insert(d);
//		   log.info("demo save end.....");
//		   return " ";   
//	}
}
