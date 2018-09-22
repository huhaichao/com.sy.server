package com.sy.server.pc.controller;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sy.common.bean.UserPage;
import com.sy.common.busibean.SyUser;
import com.sy.server.pc.service.UserService;
/**
 * 会员管理系统
 * 
 * @author lenovo
 *
 */
@Controller
@RequestMapping("/Member")
public class MemberController {
	private Logger log = LoggerFactory.getLogger(MemberController.class);
	//@Autowired
	//private Users roleService;
	@Autowired
	private UserService userService;

	/**
	 * 返回会员信息页面
	 * 
	 * @return
	 */
	@RequestMapping("/User/user")
	public String initMember() {
		log.info("=================进入会员信息页面===============");
		return "User/user";
	}

	/**
	 * 返回账户信息页面
	 * 
	 * @return
	 */
	@RequestMapping("/account")
	public String initAccount() {
		log.info("=================进入会员账户页面===============");
		return "User/account";
	}

	/**
	 * 发布寻人信息页面
	 * 
	 * @return
	 */
	@RequestMapping("/pubPeople")
	public String initPubpeople() {
		log.info("=================进入发布寻人信息页面===============");
		return "User/account";
	}

	/**
	 * 发布拾物信息页面
	 * 
	 * @return
	 */
	@RequestMapping("/pubPickuper")
	public String initPubpickuper() {
		log.info("=================进入发布拾物信息页面===============");
		return "User/pub_loster";
	}

	/**
	 * 发布失物信息页面
	 * 
	 * @return
	 */
	@RequestMapping("/pubLoster")
	public String getPubloster() {
		log.info("=================进入发布失物信息页面===============");
		return "User/pub_loster";
	}
	
	/**
	 * 进入会员管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/User/userserch", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String queryUserserch(HttpServletRequest request) {
		log.info("=================进入会员管理页面===============");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		JSONArray jsons = new JSONArray();
		List<SyUser> list = null;
		JSONObject json = new JSONObject();
		try {	
			int userIntegral=0;
			int userSex=0;
			int userStatus=0;
			int pageNo=Integer.parseInt(request.getParameter("pageNo"));
			int pageSize=Integer.parseInt(request.getParameter("pageSize"));
			Date starDate=sdf.parse(request.getParameter("starDate").toString());
			Date endDate=sdf.parse(request.getParameter("endDate").toString());
			String userName=request.getParameter("userName");
			if(request.getParameter("userIntegral").length()>0){
				userIntegral=Integer.parseInt(request.getParameter("userIntegral"));
			}
			if(request.getParameter("userSex").length()>0){
				userSex=Integer.parseInt(request.getParameter("userSex"));
			}
			if(request.getParameter("userStatus").length()>0){
				userStatus=Integer.parseInt(request.getParameter("userStatus"));
			}
			int pageCount=userService.getCount();
			UserPage page=new UserPage();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setIntPage((pageNo-1)*pageSize);
			page.setPageRow(pageSize);
			page.setStarDate(starDate);
			page.setEndDate(endDate);
			page.setUserName(userName);
			page.setUserSex(userSex);
			page.setUserStatus(userStatus);
			page.setIntEgral(userIntegral);
			list=userService.fallUsers(page);
			if (list.size() > 0) {
				Iterator<SyUser> iterator = list.iterator();
				while (iterator.hasNext()) {
					JSONObject jo = new JSONObject();
					SyUser syUser = (SyUser) iterator.next();
					  jo.put("id", syUser.gettSyUserId());
					  jo.put("tSyUserId", syUser.gettSyUserId());
					  jo.put("tSyUsername", syUser.gettSyUsername());
					  jo.put("tSySex", syUser.gettSySex());
					  jo.put("tSyNickname", syUser.gettSyNickname());
					  jo.put("tSyIntegral", syUser.gettSyIntegral());
					  jo.put("tSyCreateTime", sdf.format(syUser.gettSyCreateTime()));
					  jo.put("tSyIsonline", syUser.gettSyIsonline());
					  jo.put("tSyAccountState", syUser.gettSyAccountState()==1?"冻结":"解冻"); 
					  jo.put("tSyUserStatus", syUser.gettSyUserStatus()==1?"男":"女");
					jsons.put(jo);
				}
			}

			json.put("total", pageCount);
			json.put("records", jsons);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(list);
		return json.toString();
	}


}
