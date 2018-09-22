package com.sy.server.pc.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sy.common.annotation.SyUtil;
import com.sy.common.bean.VersionPage;
import com.sy.common.busibean.SyVersion;
import com.sy.server.pc.service.VersionService;

/**
 * 2017-01-13
 * @author zbb
 *
 */
@Controller
@RequestMapping("/website")
public class WebSiteManageController {
	private  Logger   log  = LoggerFactory.getLogger(WebSiteManageController.class);
	@Autowired
	private VersionService versionService;
	/**
	 * 访问广告管理页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/website/advert")
    public String initadvert(Map<String, Object> model){
		log.info("========================进入广告管理页面============================");
    	return "website/advert";
    }
	
	/**
	 * 点击查询进入的方法
	 * 
	 * @param stardate
	 *            开始日期
	 * @param enddate
	 *            截止日期
	 * @param versionName
	 *            版本号
	 * @param model
	 *            实体对象
	 * @return
	 */
	@RequestMapping(value = "/website/versionserch", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String query(HttpServletRequest request) {
		log.info("request/website/version.html");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		JSONArray jsons = new JSONArray();
		List<SyVersion> list = null;
		JSONObject json = new JSONObject();
		try {
			System.out.println("获取的页码为："+request.getParameter("pageNo"));
			System.out.println("数据为："+request.getParameter("pageSize"));
			//Page
			int pageNo=Integer.parseInt(request.getParameter("pageNo"));
			int pageSize=Integer.parseInt(request.getParameter("pageSize"));
			Date starDate=sdf.parse(request.getParameter("starDate").toString());
			Date endDate=sdf.parse(request.getParameter("endDate").toString());
			String versionNo=request.getParameter("versionNo");
			int pageCount=versionService.getCount();
			VersionPage page=new VersionPage();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setIntPage((pageNo-1)*pageSize);
			page.setPageRow(pageSize);
			page.setStarDate(starDate);
			page.setEndDate(endDate);
			page.setVersionNo(versionNo);
			list=versionService.fallVersions(page);
			if (list.size() > 0) {
				Iterator iterator = list.iterator();
				while (iterator.hasNext()) {
					JSONObject jo = new JSONObject();
					SyVersion version = (SyVersion) iterator.next();
					jo.put("id", version.gettSyVersionId());
					jo.put("tSyVersionId", version.gettSyVersionId());
					jo.put("tSyVersionNumber", version.gettSyVersionNumber());
					jo.put("tSyCreateuser", version.gettSyCreateuser());
					jo.put("tSyCreatedate", sdf.format(version.gettSyCreatedate()));
					jo.put("tSyCreateinfo", version.gettSyCreateinfo());
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
	 * 保存版本信息表的方法
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/website/add", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String add(HttpServletRequest request) {
		log.info("request/website/add.html");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		int index = 0;
		SyVersion syVersion = new SyVersion();
		String data = request.getParameter("data");
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap = SyUtil.fromObject(data);
		try {
			syVersion.settSyVersionNumber(hashMap.get("addbbh").toString());
			syVersion.settSyCreateuser(hashMap.get("addcjr").toString());
			syVersion.settSyCreatedate(format1.parse(hashMap.get("date3").toString()));
			syVersion.settSyCreateinfo(hashMap.get("addsjxx").toString());
			index = versionService.saveVersion(syVersion);
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
	 * 修改版本信息表的方法
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/website/update", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String update(HttpServletRequest request) {
		log.info("request/website/update.html");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		int index = 0;
		SyVersion syVersion = new SyVersion();
		String data = request.getParameter("data");
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap = SyUtil.fromObject(data);
		try {
			//syVersion.settSyVersionId(hashMap.get("upid").toString());
			syVersion.settSyVersionNumber(hashMap.get("upbbh").toString());
			syVersion.settSyCreateuser(hashMap.get("upcjr").toString());
			syVersion.settSyCreatedate(format1.parse(hashMap.get("upDate4").toString()));
			syVersion.settSyCreateinfo(hashMap.get("upsjxx").toString());
			index = versionService.updateVersion(syVersion);
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
	 * 删除版本信息表的方法
	 * 根据前台传过来的 tSyVersionId 数组做删除操作
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/website/delete", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String delete(@RequestParam(value = "id[]") String[] tSyVersionId) {
		log.info("request/website/delete.html");
		int index=0;
		List<String> list = java.util.Arrays.asList(tSyVersionId);
		index=versionService.delVersion(list);
        if(index>0){
        	return "success";
        }else{
        	return "error";
        }
	}
	
	/**
	 * 地图管理页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/website/mapmanage")
    public String initMapmanage(Map<String, Object> model){
		log.info("========================进入地图页面============================");
    	return "website/mapmanage";
    }
	
	
	/**
	 * 网站信息页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/website/siteinfo")
    public String initSiteinfo(Map<String, Object> model){
		log.info("========================进入网站信息页面============================");
    	return "website/siteinfo";
    }
	
	/**
	 * 升级版本页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/website/version")
    public String initVersion(Map<String, Object> model){
		log.info("========================进入升级版本页面============================");
    	return "website/version";
    }
	
	/**
	 * 滚动信息页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/website/pmdxx")
    public String initPmdxx(Map<String, Object> model){
		log.info("========================进入滚动信息页面============================");
    	return "website/pmdxx";
    }
	
}
