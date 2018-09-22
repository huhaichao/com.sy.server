package com.sy.server.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sy.common.annotation.DateUtil;
import com.sy.common.annotation.MapUtil;
import com.sy.common.bean.CzSwMx;
import com.sy.common.busibean.SyItemInformation;
import com.sy.common.busibean.SyPeopleInformation;
import com.sy.server.app.service.SyItemService;
import com.sy.server.app.service.SyPeopleInformationService;

/**
 * 获取首页数据信息
 * 
 * @author yxx
 *
 */
@RestController
@RequestMapping("/InforMation")
public class SyInformationController {
	private Logger log = LoggerFactory.getLogger(SyInformationController.class);
	@Autowired
	private SyPeopleInformationService syPeopleInformationService;
	@Autowired
	private SyItemService syitemService;

	/**
	 * 根据参数获取周围的实体数据信息
	 * 
	 * @param x
	 *            精度
	 * @param y
	 *            纬度
	 * @param syType
	 *            类型
	 * @param pt
	 *            操作平台(android、ios)
	 * @param bb
	 *            使用版本
	 * @return
	 */
	@RequestMapping(value = "/getInforMation", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody CzSwMx returnCzswmx(@RequestParam(value = "latitude", required = true) String y,
			@RequestParam(value = "longitude", required = true) String x,
			@RequestParam(value = "syType", required = true) String syType,
			@RequestParam(value = "st", required = true) String pt,
			@RequestParam(value = "vs", required = true) String bb) {
		CzSwMx czSwMx = new CzSwMx();
		MapUtil mapUtil = new MapUtil();
		mapUtil.setErea(Double.parseDouble(x), Double.parseDouble(y));
		// mapUtil.settSwtype(syType);
		mapUtil.setVersionType(pt);
		mapUtil.setVersionNum(bb);
		List<Map> maps = new ArrayList();
		// 获取所有丢失人的数据信息
		List<SyPeopleInformation> people = new ArrayList<SyPeopleInformation>();
		// 获取丢失物品的数据信息
		List<SyItemInformation> item = new ArrayList<SyItemInformation>();
		try {
			if (syType.equals("1")) {//查询物品
				item = syitemService.selectByItem(mapUtil);
				for (SyItemInformation syItem : item) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("tSyX", syItem.gettSyX());
					map.put("tSyY", syItem.gettSyY());
					map.put("tSyid", syItem.gettSyItemInformationId());
					map.put("tSwType", syItem.gettSyPubType());
					map.put("tSwTitle", syItem.gettSyItemDescribe());
					maps.add(map);
				}
			} else if (syType.equals("2")) {//查询人物
				people = syPeopleInformationService.selectBySyPeopleInformations(mapUtil);
				for (SyPeopleInformation peoples : people) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("tSyX", peoples.gettSyX());// 获取精度
					map.put("tSyY", peoples.gettSyY());// 获取纬度
					map.put("tSyid", peoples.gettSyPeopleInformationId());// 获取ID
					map.put("tSwType", 2);// 获取类型为人
					map.put("tSwTitle", peoples.gettSyFeatures());// 获取详情
					maps.add(map);
				}
			} else {//查询所有
				item = syitemService.selectByItem(mapUtil);
				if (!item.isEmpty()) {
					for (SyItemInformation syItem : item) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("tSyX", syItem.gettSyX());
						map.put("tSyY", syItem.gettSyY());
						map.put("tSyid", syItem.gettSyItemInformationId());
						map.put("tSwType", syItem.gettSyPubType());
						map.put("tSwTitle", syItem.gettSyItemDescribe());
						maps.add(map);
					}
				}
				people = syPeopleInformationService.selectBySyPeopleInformations(mapUtil);
				if (!item.isEmpty()) {
					for (SyPeopleInformation peoples : people) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("tSyX", peoples.gettSyX());// 获取精度
						map.put("tSyY", peoples.gettSyY());// 获取纬度
						map.put("tSyid", peoples.gettSyPeopleInformationId());// 获取ID
						map.put("tSwType", 2);// 获取类型为人
						map.put("tSwTitle", peoples.gettSyFeatures());// 获取详情
						maps.add(map);
					}
				}
			}

			if (maps.isEmpty()) {
				czSwMx.setCode("0");
				czSwMx.setMsg("223");
				return czSwMx;
			} else {
				czSwMx.setCode("1");
				czSwMx.setMsg("224");
				czSwMx.setData(maps);
			}
			log.info("查询数据列表已经完毕......."+maps);
		} catch (Exception e) {
			// TODO: handle exception
			czSwMx.setCode("0");
			czSwMx.setMsg("222");
			log.info("查询数据列表异常......" + e.getMessage());
			return czSwMx;
		}
		return czSwMx;
	}

	/**
	 * 根据搜索接口获取需要查询的所有数据
	 * 
	 * @param x
	 *            精度
	 * @param y
	 *            纬度
	 * @param syType
	 *            类型
	 * @param syType
	 *            类型明细
	 * @param syName
	 *            失物名称
	 * @param syDate
	 *            搜索日期
	 * @param pt
	 *            操作平台(android、ios)
	 * @param bb
	 *            使用版本
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "null" })
	@RequestMapping(value = "/searchInforMation", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody CzSwMx returnCzswmxMx(@RequestParam(value = "latitude", required = false) String y,
			@RequestParam(value = "longitude", required = false) String x,
			@RequestParam(value = "syType", required = false) String syType,
			@RequestParam(value = "syTypeMx", required = false) String syTypeMx,
			@RequestParam(value = "syName", required = true) String syName,
			@RequestParam(value = "syDateStart", required = false) String syDateStart,
			@RequestParam(value = "syDateEnd", required = false) String syDateEnd,
			@RequestParam(value = "st", required = false) String pt,
			@RequestParam(value = "vs", required = true) String bb,
			@RequestParam(value = "page", required = true) String page) {
		CzSwMx czSwMx = new CzSwMx();
		MapUtil mapUtil = new MapUtil();
		List<Map> listMap = new ArrayList();
		try {
			mapUtil.setErea(Double.parseDouble(x), Double.parseDouble(y));
			mapUtil.settSwtypeMx(syTypeMx);//失物详细类型
			mapUtil.setSearchname(syName);
			mapUtil.settSyDateStart(DateUtil.toDate(syDateStart));
			mapUtil.settSyDateEnd(DateUtil.toDate(syDateEnd));
			mapUtil.setVersionType(pt);
			mapUtil.setVersionNum(bb);
			mapUtil.setPage(Integer.parseInt(page) * 20);
			mapUtil.setPageSize(20);
			log.info("searchInforMation 搜索查询入参为"+mapUtil);
			if (syType.equals("1")) {
				listMap = syitemService.selectByItemMap(mapUtil);
			} else if (syType.equals("2")) {//
				listMap = syPeopleInformationService.selectBySyPeopleInformationsMap(mapUtil);
			} else {
				List<Map> item = syitemService.selectByItemMap(mapUtil);
				for (int i = 0; i < item.size(); i++) {
					listMap.add(item.get(i));
				}
				List<Map> people = syPeopleInformationService.selectBySyPeopleInformationsMap(mapUtil);
				for (int i = 0; i < people.size(); i++) {
					listMap.add(people.get(i));
				}
			}

			if (listMap.isEmpty()) {
				czSwMx.setCode("0");
				czSwMx.setMsg("223");
				return czSwMx;
			} else {
				czSwMx.setCode("1");
				czSwMx.setMsg("224");
				czSwMx.setData(listMap);
			}
			log.info("查询数据列表已经完毕......."+listMap);
		} catch (Exception e) {
			// TODO: handle exception
			czSwMx.setCode("0");
			czSwMx.setMsg("222");
			log.info("查询数据列表异常......" + e.getMessage());
			return czSwMx;
		}
		return czSwMx;
	}

	/**
	 * 热门搜索关键字暂时先写死以后有新的算法的时候在来优化
	 */
	@RequestMapping(value = "/searchInform", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody CzSwMx searchInform(@RequestParam(value = "st", required = true) String pt,
			@RequestParam(value = "vs", required = true) String bb) {
		CzSwMx czSwMx = new CzSwMx();
		log.info("热门搜索关键字初始化....");
		/*Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("syName", "物品");
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("syName", "动物");
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("syName", "人");*/
		List<String> listMap = new ArrayList<String>();
		listMap.add("狗");
		listMap.add("猫");
		listMap.add("人");
		
		Map<String,Object>   key =  new HashMap<String, Object>();
		key.put("key", listMap);
		
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("syName", "数码产品");
		map1.put("id", "2");
		
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("syName", "家用电器");
		map2.put("id", "1");
		
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("syName", "玩具乐器");
		map3.put("id", "5");
		
		Map<String, String> map4 = new HashMap<String, String>();
		map4.put("syName", "宠物");
		map4.put("id", "10");
		
		List<Map<String,String>> listMap2 = new ArrayList<Map<String,String>>();
		listMap2.add(map1);
		listMap2.add(map1);
		
		key.put("keyType", listMap2);
		
		czSwMx.setCode("1");
		czSwMx.setMsg("225");
		czSwMx.setData(key);
		log.info("热门搜索关键字返回值:"+czSwMx.toString());
		return czSwMx;
	}

	/**
	 * 热门搜索关键字暂时先写死以后有新的算法的时候在来优化
	 */
	@RequestMapping(value = "/searchInfogjz", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody CzSwMx searchInfogjz(@RequestParam(value = "st", required = true) String pt,
			@RequestParam(value = "vs", required = true) String bb) {
		CzSwMx czSwMx = new CzSwMx();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("syName", "钱包");
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("syName", "二哈");
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("syName", "小明");
		List<Map> listMap = new ArrayList<Map>();
		listMap.add(map1);
		listMap.add(map2);
		listMap.add(map3);
		czSwMx.setCode("1");
		czSwMx.setMsg("225");
		czSwMx.setData(listMap);
		return czSwMx;
	}
}
