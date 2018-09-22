//问题：业务逻辑完全懵逼啊
package com.sy.server.app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sy.common.Exception.SyException;
import com.sy.common.annotation.DateUtil;
import com.sy.common.annotation.MapUtil;
import com.sy.common.bean.RespondBean;
import com.sy.common.bean.Yzmjk;
import com.sy.common.bean.blankjk;
import com.sy.common.busibean.SyItemInformation;
import com.sy.common.busibean.SyMessage;
import com.sy.common.busibean.SyPeopleInformation;
import com.sy.common.enums.MessageCodeConstant;
import com.sy.common.pools.DateActionConvert;
import com.sy.server.app.service.SyItemService;
import com.sy.server.app.service.SyMessageService;
import com.sy.server.app.service.SyPeopleService;
import com.sy.server.app.service.SyUserService;

/**
 * 捡到失物信息 控制层 当使用@RestController时使用返回对象 当使用@Controller时返回视图jsp
 * 
 * @author yumeng 、wangliang
 *
 */
@RestController
@RequestMapping("/find")
public class FindRestController {

	private Logger log = LoggerFactory.getLogger(FindRestController.class);

	@Autowired
	private SyItemService itemservice;

	@Autowired
	private SyPeopleService peopleservice;

	@Autowired
	private SyUserService syuserservice;

	@Autowired
	private SyMessageService symessageservice;

	
	/*
	 * 拾物详情接口
	 * 物/人的详细信息 wl
	 * 2017-05-29
	 * @param name
	 * @param ty
	 * @param st
	 * @param vs
	 * @return RespondBean
	 * 物：http://127.0.0.1:8080/sy/server/find/swxq?ty=1&itemId=4&st=Android&vs=4.3.0
	 * 人：http://127.0.0.1:8080/sy/server/find/swxq?ty=2&itemId=3&st=Android&vs=4.3.0
	 */
	/*@RequestMapping(value = "/swxq", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean returngoodinfo(@RequestParam(value = "itemId", required = true) String itemId,
			@RequestParam(value = "ty", required = true) String ty,
			@RequestParam(value = "st", required = true) String st,
			@RequestParam(value = "vs", required = true) String vs) {
		log.info("详细信息查询.....");
		SubSwxqjk subswxqjk = null;
		RespondBean rb =null;
		Swxqjk swxqjk = new Swxqjk();
		try {
			List<SubSyxsjk> data = new ArrayList<SubSyxsjk>();
			MapUtil jwd = new MapUtil();
			SyItemInformation si= null;//人物信息
			SyPeopleInformation sp = null;//人信息
			jwd.setItemId(Integer.valueOf(itemId));
			jwd.setSearchtype(ty);
			if(ty.equals("0")||ty.equals("1")){//物查询
				si = itemservice.selectByIdAndType(jwd);
				if(si!=null){
					SyUser user = syuserservice.query(Integer.valueOf(si.gettSyUserId()));
					SyMessage sm = symessageservice.selectByPrimaryKey(Integer.valueOf(si.gettSyMessageId()));
						subswxqjk = new SubSwxqjk();
						subswxqjk.setIsReward("");//是否悬赏   确定所属位置
						subswxqjk.setContent("");//信息  待完善  url
						subswxqjk.setMessageState(sm.gettSyStatus());//消息状态  获取所属详细状态  通过查询消息表获得
						subswxqjk.setPhone(user!=null?user.gettSyPhone():" ");//发布人手机号
						subswxqjk.setUserName(user!=null?user.gettSyUsername():" ");//用户名
						subswxqjk.setUserUrl(user!=null?user.gettSyImgpath():" ");//用户头像地址
						subswxqjk.setChatAccount("");//环信账号   确定所属位置
						rb = new RespondBean(subswxqjk,MessageCodeConstant.DETAIL_GET_SUCCESS);
				}else{
					rb = new RespondBean(MessageCodeConstant.DETAIL_GET_FAIL);
				}
			}else if(ty.equals("2")){//人查询
				sp = peopleservice.selectByPrimaryKey(Integer.valueOf(itemId));
				if(sp!=null){
					SyUser user = syuserservice.query(Integer.valueOf(sp.gettSyUserId()));
					SyMessage sm = symessageservice.selectByPrimaryKey(Integer.valueOf(sp.gettSyMessageId()));
						swxqjk.setCode("1");
						swxqjk.setMsg("");
						subswxqjk = new SubSwxqjk();
						subswxqjk.setIsReward("");//是否悬赏   确定所属位置 
						subswxqjk.setContent("");//信息  待完善  url
						subswxqjk.setMessageState(sm.gettSyStatus());//消息状态  获取所属详细状态  通过查询消息表获得
						subswxqjk.setPhone(user!=null?user.gettSyPhone():" ");//发布人手机号
						subswxqjk.setUserName(user!=null?user.gettSyUsername():" ");//用户名
						subswxqjk.setUserUrl(user!=null?user.gettSyImgpath():" ");//用户头像地址
						subswxqjk.setChatAccount("");//环信账号   确定所属位置
						rb = new RespondBean(subswxqjk,MessageCodeConstant.DETAIL_GET_PEOPLE_SUCCESS);
				}else{
					rb = new RespondBean(MessageCodeConstant.DETAIL_GET_PEOPLE_FAIL);
				}
			}else{
				rb = new RespondBean(MessageCodeConstant.DETAIL_BET_INFORMATION_STATUS);
			}
			log.info("详细信息查询完毕.....");
		} catch (Exception e) {
			rb = new RespondBean(MessageCodeConstant.DETAIL_EXCEPTION);
			log.info("详细信息查询失败" + e.getMessage());
		}
		return rb;
	}*/

	/**
	 * 信息发布和修改（物） wl 修改了悬赏金额数值转换 2016-03-06
	 * 
	 * 
	 * @param id
	 * @param ty
	 * @param st
	 * @param vs
	 * @return *
	 *         物：https://127.0.0.1:8080/sy/server/find/swfb?ty=type1&id=item_000001&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/swfb", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean returngoodupdate(@RequestParam(value = "msgid", required = true) String msgid,//消息ID
			@RequestParam(value = "uid", required = true) String uid,  //用户ID
			@RequestParam(value = "iName", required = true) String iName,//物品名称
			@RequestParam(value = "mt", required = true) String mt,//
			@RequestParam(value = "fs", required = true) String fs,//发布方式
			@RequestParam(value = "ty", required = true) String ty,//证件类型
			@RequestParam(value = "purl", required = false) String purl,//头像链接
			@RequestParam(value = "dlost", required = false) String dlost,//丢失日期
			@RequestParam(value = "je", required = false) String je,//悬赏金额
			//@RequestParam(value = "dsx", required = true) String dsx,//失效日期
			@RequestParam(value = "loca", required = false) String loca,//丢失地点
			@RequestParam(value = "kl", required = false) String kl,//口令
			@RequestParam(value = "jd", required = true) String jd,//经度
			@RequestParam(value = "wd", required = true) String wd,//纬度
			@RequestParam(value = "ms", required = false) String ms,//描述
			//@RequestParam(value = "stat", required = true) String stat,//状态
			@RequestParam(value = "st", required = true) String st,//设备
			@RequestParam(value = "vs", required = true) String vs,//版本
			@RequestParam(value = "operFlag", required = true) String operFlag) {
		RespondBean respBean = null;
		log.info("开始信息发布和修改......");
		//String insertFlag = "0";// 0  新增  1  更新
		try {
			if (operFlag.equals("0")) {//新增
				SyMessage symessage = initSyMessage(uid,iName,fs,ty,mt,je,jd,wd,kl);
				symessageservice.insert(symessage);
				SyItemInformation syitem = 	initSyItemInformation(uid,iName,ty,dlost,je,loca,ms,jd,wd,purl);
				syitem.settSyMessageId(symessage.gettSyMessageId().toString());
				itemservice.insert(syitem);
				respBean = new RespondBean(symessage.gettSyMessageId().toString(), MessageCodeConstant.PUBLISH_ITEM_ADD_SUCCESS);
			} else {
				SyMessage sm =  symessageservice.selectByPrimaryKey(Integer.parseInt(msgid));
				MapUtil mu = new MapUtil();
				mu.setMsgid(sm.gettSyMessageId().toString());
				SyItemInformation si  = itemservice.selectByMessageId(mu);
				if(sm != null){//修改信息表
					//修改数据时，重新获取发布日期，失效日期根据新的发布日期向后推一段时间。
					sm.settSyUserId(Integer.valueOf(uid));//用户id
					sm.settSyThingsName(iName);// 物品名称
					sm.settSySendType(Integer.valueOf(fs));// 发布方式 
					sm.settSyThingsType(ty);// 物品类型
					sm.settSyMessageType("0");// 0-发布，1-帮助，2-收藏
					sm.settSyPassword(kl);//口怜
					sm.settSyDate(new Date());//发布日期
					sm.settSyEnddate(new Date());//结束日期，目前暂时没有使用
					sm.settSyExpirationDate(DateUtil.getNextMonth(new Date()));//失效日期;
			       symessageservice.updateByPrimaryKey(sm);
				}
				if(si != null){//修改发布物品表
					si.settSyUserId(uid);  //用户id 
					si.settSyItemName(iName);// 物品名称
					si.settSyItemType(ty);// 物品类型
					si.settSyLoseDate(DateUtil.toDate(dlost));//丢失日期
					si.settSyBountyMoney(je==null || "".equals(je)?0 :Long.valueOf(je));// 悬赏金额
					si.settSyLoseAddress(loca);// 丢失地点
					si.settSyItemDescribe(ms);
					si.settSyPublishedDate(new Date());
					si.settSyX(jd);// 经度
					si.settSyY(wd);// 纬度
					itemservice.updateByPrimaryKey(si);
				}
				List<String> l = new ArrayList<String>();
				respBean = new RespondBean(sm.gettSyMessageId().toString(), MessageCodeConstant.PUBLISH_ITEM_UPDATE_SUCCESS);
			}
			log.info("信息发布和修改完成.....");
		} catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.PUBLISH_ITEM_ERROR);
			log.info("信息发布和修改失败" + e.getMessage());
		}
		return respBean;
	}
	
	
	public  SyMessage initSyMessage(String uid,String iname,String fs,String ty,String mt,String je,String jd,String wd,String kl){
		SyMessage symessage = new SyMessage();
		symessage.settSyUserId(Integer.valueOf(uid));//用户id
		symessage.settSyThingsName(iname);// 物品名称
		symessage.settSySendType(Integer.valueOf(fs));// 发布方式  
		symessage.settSyThingsType(ty);// 物品类型
		symessage.settSyMessageType("0");//信息类型   // 0-发布，1-帮助，2-收藏
		symessage.settSyDate(new Date());// 发布日期
		symessage.settSyEnddate(new Date());//结束日期，目前暂时没有使用
		symessage.settSyPassword(kl);//口怜
		symessage.settSyMessageState(0);// 消息状态
		symessage.settSyStatus("0");  // 消息状态  默认 0    
		symessage.settSyCheck("1"); //是否审核  默认   1     0 已审核  1 未审核   
		symessage.settSyDelStatus("0");//是否删除   默认  0  0 未删除  1 已删除 
		symessage.settSyExpirationDate(DateUtil.getNextMonth(new Date()));//失效日期
		return symessage;
	}
	public SyItemInformation initSyItemInformation(String uid,String iname,String ty,String lostDate,String je,String loca,String ms,String jd,String wd,String purl){
		SyItemInformation syitem = new SyItemInformation();
		syitem.settSyUserId(uid);  //用户id 
		syitem.settSyItemName(iname);// 物品名称
		syitem.settSyItemType(ty);// 物品类型
		try {
			syitem.settSyLoseDate(DateUtil.toDate(lostDate));
		} catch (SyException e) {
			log.info("发布物品时，转换日期错误！");
		}//丢失日期
		syitem.settSyBountyMoney(je==null || "".equals(je)? 0 : Long.valueOf(je));// 悬赏金额
		syitem.settSyLoseAddress(loca);// 丢失地点
		syitem.settSyItemDescribe(ms);
		syitem.settSyPublishedDate(new Date());
		syitem.settSyX(jd);// 经度
		syitem.settSyY(wd);// 纬度
		syitem.settSyPubType("0");
		syitem.settSyImgpath(purl);
		return syitem;
	}
	/*
	 * 1.接口参数 消息id(如果是发布，为””，如果是修改，必填) 用户id(必填) 姓名(必填) 性别(必填) 年龄(必填) 物品照片1 物品照片2
	 * 物品照片3 民族 证件类型 证件号 籍贯 丢失日期(必填) 失效日期 悬赏金额 丢失地点名称 丢失地点经度(必填) 丢失地点纬度(必填) 口令
	 * 体貌特征 公安文件1 公安文件2 状态 //保存、修改 操作平台(Android、iOS) 使用版本 2.接口返回结果 { "code":
	 * 1,//成功或者失败标志 "msg": "",//失败时的原因，成功时为"" "data": {//成功时返回的数据集，失败时为"" 发布消息id
	 * } }
	 */
	/**
	 * 信息发布和修改（人）
	 * 
	 * @param id
	 * @param ty
	 * @param st
	 * @param vs
	 * @return *
	 *         物：https://127.0.0.1:8080/sy/server/find/rwfb?ty=type1&id=item_000001&st=Android&vs=4.3.0
	 *         公安证明文件一般也是截图，所以1，2文件存路径，一起传过来
	 */
	@RequestMapping(value = "/rwfb", method = { RequestMethod.GET, RequestMethod.POST },produces="application/json;charset=utf-8")
	public @ResponseBody RespondBean returnpeopleupdate(@RequestParam(value = "msgid", required = true) String msgid,
			@RequestParam(value = "uid", required = true) String uid,  //用户id
			@RequestParam(value = "iName", required = true) String iName,//姓名
			@RequestParam(value = "sex", required = true) String sex,//性别
			@RequestParam(value = "age", required = true) String age,//年龄
			@RequestParam(value = "mz", required = false) String mz,//民族
			@RequestParam(value = "fs", required = true) String fs,//
			@RequestParam(value = "ty", required = false) String ty,//证件类型
			@RequestParam(value = "no", required = false) String no,//证件号
			@RequestParam(value = "jg", required = true) String jg,//籍贯
			@RequestParam(value = "purl", required = false) String purl,//照片链接
			@RequestParam(value = "dlost", required = false) String dlost,//丢失日期
			@RequestParam(value = "je", required = false) String je,//悬赏金额
			//@RequestParam(value = "dsx", required = true) String dsx,//失效日期
			@RequestParam(value = "loca", required = false) String loca,//丢失点
			@RequestParam(value = "kl", required = false) String kl,//口令
			@RequestParam(value = "ga", required = false) String ga,//公安文件  
			@RequestParam(value = "jd", required = true) String jd,//经度值
			@RequestParam(value = "wd", required = true) String wd,//纬度值
			@RequestParam(value = "ms", required = false) String ms,//描述
			//@RequestParam(value = "stat", required = true) String stat,//状态
			@RequestParam(value = "st", required = true) String st,//设备
			@RequestParam(value = "vs", required = true) String vs,
			@RequestParam(value = "operFlag", required = true) String operFlag) {//版本
		RespondBean respBean = null;
		log.info("开始信息发布和修改......");
		//构造以字符串内容为值的BigDecimal类型的变量bd   
		BigDecimal bd = new BigDecimal((je==null || "".equals(je))?"0":je);   
		//设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)   
		bd=bd.setScale(2, BigDecimal.ROUND_HALF_UP); 
		try {
			if(operFlag.equals("0")){
				SyMessage symessage = new SyMessage();
				symessage.settSyUserId(Integer.valueOf(uid));//用户id
				symessage.settSyThingsName(iName);//人员名称
				symessage.settSyThingsType("");// 物品类型
				symessage.settSySendType(Integer.valueOf(fs));// 发布方式 
				symessage.settSyMessageType("0");// 0-发布，1-帮助，2-收藏
				symessage.settSyDate(new Date());// 发布日期
				symessage.settSyEnddate(new Date());//结束日期，目前暂时没有使用
				symessage.settSyPassword(kl);//口令
				symessage.settSyMessageState(0);// 消息状态
				symessage.settSyStatus("0");  //消息状态
				symessage.settSyCheck("0"); //是否审核
				symessage.settSyDelStatus("0");//是否删除
				symessage.settSyExpirationDate(DateUtil.getNextMonth(new Date()));//失效日期
				symessageservice.insert(symessage);
				SyPeopleInformation sypeople = new SyPeopleInformation();
				sypeople.settSyUserId(Integer.valueOf(uid));
				sypeople.settSyMessageId(symessage.gettSyMessageId());//丢失人员信息关联信息表
				sypeople.settSyName(iName);// 物品名称
				sypeople.settSySex(Integer.valueOf(sex));// 物品类型-性别
				sypeople.settSyImgpath(purl);// 图片url
				sypeople.settSyLostDate(DateUtil.toDate(dlost));//丢失日期
				sypeople.settSyAge(Integer.valueOf(age));// 年纪
				sypeople.settSyNation(Integer.valueOf(mz.length()==0 ? "0" : mz));// 民族 （前台传标识过来）
				sypeople.settSyBountyMoney(bd);// 悬赏金额 wl
				sypeople.settSyCardType(Integer.valueOf(ty));// 证件类型 wl
				sypeople.settSyCardId(no);// 证件号码 wl
				sypeople.settSyPlace(jg);// 籍贯
				sypeople.settSyLostAddress(loca);// 丢失地点
				sypeople.settSyX(jd);// 经度
				sypeople.settSyY(wd);// 纬度
				sypeople.settSyFeatures(ms);// 体貌特征
				sypeople.settSyProveFile(ga); // 公安证明文件
				sypeople.settSyPublishedDate(new Date());
				peopleservice.insert(sypeople);
				respBean = new RespondBean(symessage.gettSyMessageId().toString(), MessageCodeConstant.PUBLISH_PEOPLE_ADD_SUCCESS);
				log.info("信息发布和修改完成.....");
			}else{
				SyMessage sm = symessageservice.selectByPrimaryKey(Integer.valueOf(msgid));
				MapUtil mu = new MapUtil();
				if(sm!=null){
					//修改数据时，重新获取发布日期，失效日期根据新的发布日期向后推一段时间。
					sm.settSyUserId(Integer.valueOf(uid));//用户id
					sm.settSyThingsName(iName);//人员名称
					sm.settSySendType(Integer.valueOf(fs));// 发布方式 
					sm.settSyMessageType("0");// 0-发布，1-帮助，2-收藏
					sm.settSyDate(new Date());// 发布日期
					sm.settSyEnddate(new Date());//结束日期，目前暂时没有使用
					sm.settSyPassword(kl);//口令
					sm.settSyExpirationDate(DateUtil.getNextMonth(new Date()));//失效日期
					symessageservice.updateByPrimaryKey(sm);
					mu.setMsgid(sm.gettSyMessageId().toString());
				}
				SyPeopleInformation sp = peopleservice.selectByMessageId(mu);
				if(sp !=null){
					sp.settSyUserId(Integer.valueOf(uid));
					sp.settSyName(iName);// 物品名称
					sp.settSySex(Integer.valueOf(sex));// 物品类型-性别
					sp.settSyImgpath(purl);// 图片url
					sp.settSyLostDate(DateUtil.toDate(dlost));//丢失日期
					sp.settSyAge(Integer.valueOf(age));// 年纪
					sp.settSyNation(Integer.valueOf(mz.length()==0 ? "0" : mz));// 民族 （前台传标识过来）
					sp.settSyBountyMoney(bd);// 悬赏金额 wl
					sp.settSyCardType(Integer.valueOf(ty));// 证件类型 wl
					sp.settSyCardId(no);// 证件号码 wl
					sp.settSyPlace(jg);// 籍贯
					sp.settSyLostAddress(loca);// 丢失地点
					sp.settSyX(jd);// 经度
					sp.settSyY(wd);// 纬度
					sp.settSyFeatures(ms);// 体貌特征
					sp.settSyProveFile(ga); // 公安证明文件
					peopleservice.updateByPrimaryKey(sp);
				}
				List<String> l = new ArrayList<String>();
				respBean = new RespondBean(sm.gettSyMessageId().toString(), MessageCodeConstant.PUBLISH_PEOPLE_UPDATE_SUCCESS);
				log.info("信息发布和修改完成.....");
			}
		} catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.PUBLISH_PEOPLE_ERROR);
			log.info("信息发布和修改失败" + e.getMessage());
		}
		return respBean;
	}

	/**
	 * 发布信息删除 1.接口参数 消息id(必填) 用户id(必填)---可以不用 操作平台(Android、iOS) 使用版本 2.接口返回结果 {
	 * "code": 1,//成功或者失败标志 "msg": ""//失败时的原因，成功时为"" }
	 */
	/**
	 * 发布信息删除
	 * 
	 * @param id
	 * @param ty
	 * @param st
	 * @param vs
	 * @return https://127.0.0.1:8080/sy/server/find/xxsc?id=item_000001&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/xxsc", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean returnmessagedel(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "uid", required = false) String uid,
			@RequestParam(value = "st", required = true) String st,
			@RequestParam(value = "vs", required = true) String vs) {
		log.info("删除信息.....");
		RespondBean respBean = null;
		try {
			int j = 0;
			int k = 0;
			int i = symessageservice.deleteByPrimaryKey(Integer.getInteger(id));
			MapUtil maputil = new  MapUtil();
			maputil.setMsgid(id);
			SyPeopleInformation sp = peopleservice.selectByMessageId(maputil);
			if(sp!= null ){
				 j = peopleservice.deleteByPrimaryKey(sp.gettSyPeopleInformationId());
				 if (i == 1&&j == 1) {
						respBean = new RespondBean(null,MessageCodeConstant.DELETE_MESSAGE_SUCCESS);
					} else {
						respBean = new RespondBean(MessageCodeConstant.DELETE_MESSAGE_NULL);
					}
			}
			SyItemInformation  si = itemservice.selectByMessageId(maputil);
			if(si!= null ){
				 k = itemservice.deleteByPrimaryKey(si.gettSyItemInformationId());
				 if (i == 1&&k == 1) {
						respBean = new RespondBean(null,MessageCodeConstant.DELETE_MESSAGE_SUCCESS);
					} else {
						respBean = new RespondBean(MessageCodeConstant.DELETE_MESSAGE_NULL);
					}
			}
			log.info("删除信息完毕.....");
		} catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.DELETE_MESSAGE_ERROR);
			log.info("删除信息失败" + e.getMessage());
		}
		return respBean;
	}
	

	
	
	
	/**
	 * 激活过期的帮助发布消息  wl
	 * @param msgId
	 * @param st
	 * @param vs
	 * @return
	 * @URL   http://127.0.0.1:8010/sy/server/find/activeMessage?msgId=3&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/activeMessage", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean activeJk(@RequestParam(value = "msgId", required = true) String msgId,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs){
		
		log.info("激活消息开始......");
		RespondBean respBean = null;
		MapUtil m = new MapUtil();
		m.setMsgid(msgId);
		try{
			SyMessage message = symessageservice.selectByPrimaryKey(Integer.valueOf(msgId));
			if(message != null){
				message.settSyMessageState(0);
				Date d = DateUtil.getNextMonth(new Date());
				message.settSyExpirationDate(d);
				symessageservice.updateByPrimaryKey(message);
				log.info("激活消息完成....."); 
				respBean = new RespondBean(null,MessageCodeConstant.ACTIVE_MESSAGE_SUCCESS);
			}else{
				respBean = new RespondBean(MessageCodeConstant.ACTIVE_MESSAGE_ERROR);
				log.info("消息不存在.....");  
			}
		}catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.ACTIVE_MESSAGE_EXCEPTION);
		}
		return respBean;
	}
	
}
