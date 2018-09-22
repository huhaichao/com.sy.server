package com.sy.server.web.controller;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sy.common.annotation.SyUtil;
import com.sy.common.bean.Page;
import com.sy.common.bean.RolePage;
import com.sy.common.bean.SyRole;
import com.sy.server.pc.service.RoleService;

/**
 * demo 控制层 当使用@RestController时使用返回对象 当使用@Controller时返回视图jsp
 * 
 * @author huchao
 *
 */
@Controller
@RequestMapping("/web")
public class WebController {

	private Logger log = LoggerFactory.getLogger(WebController.class);
	@Autowired
	private RoleService roleService;
	/**
	 * https://127.0.0.1:8080/sy/server/web/index 测试html
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{pagename}", method = { RequestMethod.GET, RequestMethod.POST })
	public String query(@PathVariable String pagename, Map<String, Object> model) {
		log.info("request " + pagename + ".html");
		System.out.println("=======================进入方法"+pagename+"====================");
		return pagename;
	}
	/**
	 * 查询所有的Role对象信息
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/system/role", method = { RequestMethod.GET, RequestMethod.POST })
	public String query(Map<String, Object> model) {
		log.info("request/system/role.html");
		return "system/role";
	}

	/**
	 * 点击查询进入的方法
	 * 
	 * @param stardate
	 *            开始日期
	 * @param enddate
	 *            截止日期
	 * @param roletype
	 *            类型
	 * @param model
	 *            实体对象
	 * @return
	 */
	@RequestMapping(value = "/system/roleserch", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String query(HttpServletRequest request) {
		log.info("request/system/role.html");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		JSONArray jsons = new JSONArray();
		List<SyRole> list = null;
		JSONObject json = new JSONObject();
		try {
			System.out.println("获取的页码为："+request.getParameter("pageNo"));
			System.out.println("数据为："+request.getParameter("pageSize"));
			//Page
			int pageNo=Integer.parseInt(request.getParameter("pageNo"));
			int pageSize=Integer.parseInt(request.getParameter("pageSize"));
			Date starDate=sdf.parse(request.getParameter("starDate").toString());
			Date endDate=sdf.parse(request.getParameter("endDate").toString());
			String roleName=request.getParameter("roleName");
			int pageCount=roleService.getCount();
			RolePage page=new RolePage();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setIntPage((pageNo-1)*pageSize);
			page.setPageRow(pageSize);
			page.setStarDate(starDate);
			page.setEndDate(endDate);
			page.setRoleName(roleName);
			//list = roleService.findRoles(queryRole);
			list=roleService.fallRoles(page);
			if (list.size() > 0) {
				Iterator iterator = list.iterator();
				while (iterator.hasNext()) {
					JSONObject jo = new JSONObject();
					SyRole role = (SyRole) iterator.next();
					jo.put("id", role.gettSyRoleId());
					jo.put("tSyRoleId", role.gettSyRoleId());
					jo.put("tSyRoleName", role.gettSyRoleName());
					jo.put("tSyRoleCreatedate", sdf.format(role.gettSyRoleCreatedate()));
					jo.put("tSyRoleDescription", role.gettSyRoleDescription());
					jsons.put(jo);
				}
			}

			json.put("total", pageCount);
			json.put("records", jsons);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(list);
		return json.toString();
	}

	/**
	 * 保存用戶信息表的方法
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/system/add", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String add(HttpServletRequest request) {
		log.info("request/system/add.html");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		int index = 0;
		SyRole syRole = new SyRole();
		String data = request.getParameter("data");
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap = SyUtil.fromObject(data);
		try {
			syRole.settSyRoleId(hashMap.get("addfzid").toString());
			syRole.settSyRoleName(hashMap.get("addjsmc").toString());
			syRole.settSyRoleCreatedate(format1.parse(hashMap.get("date3").toString()));
			syRole.settSyRoleDescription(hashMap.get("addJsms").toString());
			//syRole.settSyRightOtder(Integer.parseInt(hashMap.get("sort").toString()));
			//syRole.settSyGroupId("11111");
			index = roleService.saveRole(syRole);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        if(index>0){
        	return "success";
        }else{
        	return "error";
        }
	}

	/**
	 * 更新用戶信息表的方法
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/system/update", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String update(HttpServletRequest request) {
		log.info("request/system/add.html");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		int index = 0;
		SyRole syRole = new SyRole();
		String data = request.getParameter("data");
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap = SyUtil.fromObject(data);
		try {
			syRole.settSyRoleId(hashMap.get("upFzid").toString());
			syRole.settSyRoleName(hashMap.get("upJsmc").toString());
			syRole.settSyRoleCreatedate(format1.parse(hashMap.get("upDate4").toString()));
			syRole.settSyRoleDescription(hashMap.get("upJsms").toString());
			//syRole.settSyGroupId("2222");
			index = roleService.updateRole(syRole);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(index>0){
        	return "success";
        }else{
        	return "error";
        }
	}
	
	/**
	 * 根据前台穿过来的 tSyRoleId 数组做删除操作
	 * @param tSyRoleId
	 * @return
	 */
	@RequestMapping(value = "/system/delete", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String delete(@RequestParam(value = "tSyRoleId[]") String[] tSyRoleId) {
		log.info("request/system/add.html");
		int index=0;
		List<String> list = java.util.Arrays.asList(tSyRoleId);
		index=roleService.delRoles(list);
        if(index>0){
        	return "success";
        }else{
        	return "error";
        }
	}
}
