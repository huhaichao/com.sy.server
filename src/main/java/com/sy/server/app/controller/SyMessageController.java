package com.sy.server.app.controller;
import java.util.ArrayList;
import java.util.Date;
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
import com.sy.common.bean.CommData;
import com.sy.common.bean.RespondBean;
import com.sy.common.busibean.SyBrowse;
import com.sy.common.busibean.SyMessage;
import com.sy.common.enums.MessageCodeConstant;
import com.sy.server.app.service.SyMessageService;



/**
 * 获取发布、帮助的api接口
 * @author lenovo
 *
 */
@RestController
@RequestMapping("/message")
public class SyMessageController {
	private Logger log = LoggerFactory.getLogger(SyMessageController.class);
	@Autowired
	private SyMessageService syMessageService;
	//@Autowired
	//private SyItemService syitemService;
	//@Autowired
	//private SyPeopleInformationService SyPeopleInformationService;
/**
 * 获取我的发布/帮助列表/收藏列表
 * @param userId
 * @param messgeType
 * @param st
 * @param vs
 * @return
 * http://127.0.0.1:8010/sy/server/message/getMessages?userId=123456&messgeType=0&st=ios&vs=4.3.0
 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getMessages", produces="application/json;charset=UTF-8" ,method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean returnCommData(@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "messgeType", required = true) String messgeType,
			@RequestParam(value = "page", required = true) String page,
			@RequestParam(value = "st", required = true) String st,
			@RequestParam(value = "vs", required = true) String vs) {
		RespondBean respBean = null;
		MapUtil mapUtil = new MapUtil();
		mapUtil.setUid(userId);
		mapUtil.setMsgtype(messgeType);
		mapUtil.setVersionType(st);
		mapUtil.setVersionNum(vs);
		mapUtil.setPage(Integer.parseInt(page) * 20);
		mapUtil.setPageSize(20);
		List<Map> listMap = new ArrayList();
		try {
			// 获取发布和帮助的数据信息
			if (messgeType.equals("0") || messgeType.endsWith("1")) {
				listMap = syMessageService.selectByMessages(mapUtil);
			} else {// 获取收藏列表
				listMap = syMessageService.selectByBrowse(mapUtil);
			}
			respBean = new RespondBean(listMap, MessageCodeConstant.LIST_MESSAGE_SUCCESS);
		} catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.LIST_MESSAGE_ERROR);
			log.info("查询发布消息数据发生异常，异常信息为：" + e.getMessage());
		}
		return respBean;
	}
	/**
	 * 根据用户id、消息id、消息类型获取所需要的数据详情
	 * @param userId
	 * @param messgeID
	 * @param messgeType
	 * @param st
	 * @param vs
	 * @return
	 * http://127.0.0.1:8010/sy/server/message/getMessagesMx?userId=123456&messgeType=0&messgeID=1&st=ios&vs=4.3.0
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getMessagesMx",produces="application/json;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean returnCommDatamx(@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "messgeID", required = true) String messgeID,
			@RequestParam(value = "messgeType", required = true) String messgeType,
			@RequestParam(value = "st", required = true) String st,
			@RequestParam(value = "vs", required = true) String vs) {
		RespondBean respBean = null;
		MapUtil mapUtil=new MapUtil();
		//mapUtil.setUid(userId);
		//mapUtil.setMsgtype(messgeType);
		mapUtil.setMsgid(messgeID);
		mapUtil.setVersionType(st);
		mapUtil.setVersionNum(vs);
		try {
			//List<SyMessage> syMessages=syMessageService.selectByItem(mapUtil);
			//如果类型为0或者1就是物品的详情页否则就是查询丢失人物的详情页
			if(messgeType.equals("0")||messgeType.equals("1")){
				List<Map> syItemInformations=syMessageService.selectByItemMx(mapUtil);
				if(syItemInformations.isEmpty()){
					return new RespondBean(MessageCodeConstant.DETAIL_PUBLISH_NULL);
				}else{
					if(syItemInformations.get(0).get("T_SY_USER_ID").equals(userId)){
						syItemInformations.get(0).put("SCBZ", 0);
					}else{
						SyMessage syMessage=syMessageService.selectByPrimaryKey(Integer.parseInt(messgeID));
						if(syMessage==null){
						syItemInformations.get(0).put("SCBZ", 1);
						}else{
						syItemInformations.get(0).put("SCBZ", 2);	
						}
					}
					respBean = new RespondBean(syItemInformations.get(0), MessageCodeConstant.DETAIL_PUBLISH_SUCCESS);
				}
				
			}else{
				List<Map> syPeopleInformations=syMessageService.selectByPeopleMx(mapUtil);
				if(syPeopleInformations.isEmpty()){
					return new RespondBean(MessageCodeConstant.DETAIL_PUBLISH_NULL);
				}else{
					if(syPeopleInformations.get(0).get("T_SY_USER_ID").equals(userId)){
						syPeopleInformations.get(0).put("SCBZ", 0);
					}else{
						SyMessage syMessage=syMessageService.selectByPrimaryKey(Integer.parseInt(messgeID));
						if(syMessage==null){
							syPeopleInformations.get(0).put("SCBZ", 1);
						}else{
							syPeopleInformations.get(0).put("SCBZ", 2);	
						}
					}
					respBean = new RespondBean(syPeopleInformations.get(0), MessageCodeConstant.DETAIL_PUBLISH_SUCCESS);
				}
			}
		} catch (Exception e) {
			new RespondBean(MessageCodeConstant.DETAIL_PUBLISH_ERROR);
			log.info("查询发布消息数据发生异常，异常信息为："+e.getMessage());
		}
		

		return respBean;
	}
	
	/**
	 * 根据用户id、消息id、消息类型进行更新操作
	 * @param userId
	 * @param messgeID
	 * @param messgeType
	 * @param st
	 * @param vs
	 * @return
	 * http://127.0.0.1:8010/sy/server/message/delMessagesMx?userId=123456&messgeType=0&messgeID=1&st=ios&vs=4.3.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/delMessagesMx", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean delCommData(@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "messgeID", required = true) String messgeID,
			@RequestParam(value = "messgeType", required = true) String messgeType,
			@RequestParam(value = "st", required = true) String st,
			@RequestParam(value = "vs", required = true) String vs) {
		RespondBean respBean = null;
		MapUtil mapUtil = new MapUtil();
		mapUtil.setUid(userId);
		mapUtil.setMsgtype(messgeType);
		mapUtil.setMsgid(messgeID);
		mapUtil.setVersionType(st);
		mapUtil.setVersionNum(vs);
		try {
			// 如果类型为0或者1就是物品的详情页否则就是查询丢失人物的详情页
			List list = new ArrayList();
			SyMessage message = syMessageService.selectByPrimaryKey(Integer.parseInt(messgeID));
			message.settSyDelStatus("0");
			message.settSyDelDate(new Date());
			int status = syMessageService.updateByPrimaryKey(message);
			if (status > 0) {
				list.add("sucess:删除成功");
				respBean = new RespondBean(list, MessageCodeConstant.DELETE_MESSAGE_SUCCESS);
			} else {
				respBean = new RespondBean(MessageCodeConstant.DELETE_MESSAGE_NULL);
			}
		} catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.DELETE_MESSAGE_ERROR);
			log.info("删除消息表发生异常，异常信息为：" + e.getMessage());
		}
		return respBean;
	}
	
	
	
	/**
	 * 我的收藏信息-列表信息
	 * @param uid
	 * @param st
	 * @param vs
	 * @param page
	 * @return 
	 * 
	 * http://127.0.0.1:8010/sy/server/message/collectlist?uid=user_000001&st=Android&vs=4.3.0&page=1
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/collectlist", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean myCollectMessageList(@RequestParam(value = "uid", required = true) String uid,
			@RequestParam(value = "messgeType", required = false) String st,
			@RequestParam(value = "st", required = false) String messgeType,
			@RequestParam(value = "vs", required = false) String vs,
			@RequestParam(value = "page", required = false) int page) {
		List<Map> collect = new ArrayList<Map>();
		RespondBean respBean = null;
		MapUtil mapUtil = new MapUtil();
		try {
			mapUtil.setUid(uid);//用户唯一主键标识
			mapUtil.setPage(page*20);//默认每页显示20条，通过页面计算查询多少条数据
			mapUtil.setVersionType(st);
			mapUtil.setVersionNum(vs);
			
			List<Map> item = syMessageService.selectByItemMap(mapUtil);
			for (int i = 0; i < item.size(); i++) {
				collect.add(item.get(i));
			}
			List<Map> people = syMessageService.selectBySyPeopleInformationsMap(mapUtil);
			for (int i = 0; i < people.size(); i++) {
				collect.add(people.get(i));
			}
			respBean = new RespondBean(collect, MessageCodeConstant.COLLECT_MSG_LIST_SUCCESS);
		} catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.COLLECT_MSG_LIST_ERROR);
			log.info("获取收藏列表发生异常：" + e.getMessage());
		}
		return respBean;
	}
	
	/**
	 * 我的收藏信息-详细信息
	 * @param uid
	 * @param st
	 * @param vs
	 * @param page
	 * @return 
	 * 
	 * http://127.0.0.1:8010/sy/server/message/collectdetial?uid=user_000001&st=Android&vs=4.3.0&page=1&msgid=7&msgtype=0
	 
	@RequestMapping(value = "/collectdetial", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean myCollectMessageDetial(@RequestParam(value = "uid", required = true) String uid,
			@RequestParam(value = "msgid", required = true) String msgid,
			@RequestParam(value = "msgtype", required = true) String msgtype,
			@RequestParam(value = "st", required = true) String st,
			@RequestParam(value = "vs", required = true) String vs) {
		RespondBean respBean = null;
		MapUtil mapUtil = new MapUtil();
		try {
			mapUtil.setUid(uid);//用户唯一主键标识
			mapUtil.setMsgid(msgid);
			mapUtil.setMsgtype(msgtype);
			mapUtil.setVersionType(st);
			mapUtil.setVersionNum(vs);
			//List<SyMessage> syMessages=syMessageService.selectByItem(mapUtil);
			//如果类型为0或者1就是物品的详情页否则就是查询丢失人物的详情页
			if(msgtype.equals("0")||msgtype.equals("1")){
				List<Map> syItemInformations=syitemService.selectByItem(mapUtil);
				if(syItemInformations.isEmpty()){
					respBean = new RespondBean(MessageCodeConstant.COLLECT_MSG_DETIAL_ERROR);
				}else{
					respBean = new RespondBean(syItemInformations, MessageCodeConstant.COLLECT_MSG_DETIAL_SUCCESS);
				}
				
			}else{
				List<Map> syPeopleInformations=SyPeopleInformationService.selectBySyPeopleInformations(mapUtil);
				if(syPeopleInformations.isEmpty()){
					respBean = new RespondBean(MessageCodeConstant.COLLECT_MSG_DETIAL_ERROR);
				}else{
					respBean = new RespondBean(syPeopleInformations, MessageCodeConstant.COLLECT_MSG_DETIAL_SUCCESS);
				}
			}
		} catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.COLLECT_MSG_DETIAL_ERROR);
		}
		return respBean;
	}
	*/
	/**
	 * 收藏人或者物
	 * @param uid
	 * @param msgid
	 * @param st
	 * @param vs
	 * @return
	 * 
	 * http://127.0.0.1:8010/sy/server/message/addCollect?uid=user_000008&msgid=1&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/addCollect", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean addCollectDate(@RequestParam(value = "uid", required = true) String uid,
			@RequestParam(value = "msgid", required = true) String msgid,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		RespondBean respBean = null;
		SyBrowse syBrowse = new SyBrowse();
		Date strDate = DateUtil.getFormatDate(new Date(),DateUtil.FORMAT_yyyy_MM_dd);
		try {
			syBrowse.settSyMessageId(msgid);//用户唯一标识
			syBrowse.settSyUserId(uid);//用户唯一标识
			syBrowse.settSyDate(strDate);//收藏日期
			syMessageService.insertByCollect(syBrowse);
			respBean = new RespondBean(null, MessageCodeConstant.COLLECT_MSG_ADD_SUCCESS);
		} catch (Exception e) {
			respBean = new RespondBean(null, MessageCodeConstant.COLLECT_MSG_ADD_ERROR);
			log.info("收藏信息失败" + e.getMessage());
		}
		return respBean;
	} 
	
	
	/**
	 * 取消收藏人或者物
	 * @param uid
	 * @param msgid
	 * @param st
	 * @param vs
	 * @return
	 * 
	 * http://127.0.0.1:8010/sy/server/message/delCollect?bid=11&msgid=11&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/delCollect", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean delCollectDate(@RequestParam(value = "bid", required = true) int bid,
			@RequestParam(value = "msgid", required = true) String msgid,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		RespondBean respBean = null;
		SyBrowse syBrowse = new SyBrowse();
		try {
			syBrowse.settSyBrowseId(bid);//收藏唯一标识
			syBrowse.settSyMessageId(msgid);//用户唯一标识
			syMessageService.deleteByCollect(syBrowse);
			respBean = new RespondBean(null, MessageCodeConstant.COLLECT_MSG_DEL_SUCCESS);
		} catch (Exception e) {
			respBean = new RespondBean(null, MessageCodeConstant.COLLECT_MSG_DEL_ERROR);
			log.info("取消收藏信息失败" + e.getMessage());
		}
		return respBean;
	} 
	
	
	
	/**
	 * 删除发布消息信息
	 * @param uid
	 * @param msgid
	 * @param st
	 * @param vs
	 * @return
	 * 
	 * http://127.0.0.1:8010/sy/server/message/delMessage?userId=11&messgeID=11&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/delMessage", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean delMessage(@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "messgeID", required = true) String messgeID,
			@RequestParam(value = "messgeType", required = true) String messgeType,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		MapUtil mapUtil=new MapUtil();
		mapUtil.setUid(userId);
		mapUtil.setMsgid(messgeID);
		RespondBean respBean = null;
		try {
			int bool=syMessageService.delMessage(mapUtil);
			if(bool>0){
				respBean = new RespondBean(null, MessageCodeConstant.DEL_MESSAGE_SUCCESS);
			}else{
				respBean = new RespondBean(null, MessageCodeConstant.DEL_MESSAGE_FALI);
			}
			
			
		} catch (Exception e) {
			respBean = new RespondBean(null, MessageCodeConstant.DEL_MESSAGE_ERRO);
			log.info("删除数据发生异常" + e.getMessage());
		}
		return respBean;
	} 
		
	
}
