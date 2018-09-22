package com.sy.server.app.controller;

import java.math.BigDecimal;
//问题：找短信平台
//问题：需要看手机推送来的密码如何处理才能进行校验
//问题： 验证码如何处理，前台得到，，后台无法校验
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bcloud.msg.http.HttpSender;
import com.sy.common.annotation.CommonDisk;
import com.sy.common.annotation.DateUtil;
import com.sy.common.annotation.MapUtil;
import com.sy.common.annotation.SyUtil;
import com.sy.common.bean.Dljk;
import com.sy.common.bean.RespondBean;
import com.sy.common.bean.SubDljk;
import com.sy.common.bean.SubWdxxjk;
import com.sy.common.bean.SubWdxxlbsub;
import com.sy.common.bean.SubYhxxjk;
import com.sy.common.bean.SyAccountBalance;
import com.sy.common.bean.Wdxxjk;
import com.sy.common.bean.Yhxxjk;
import com.sy.common.bean.Yzmjka;
import com.sy.common.bean.Zcjk;
import com.sy.common.bean.blankjk;
import com.sy.common.busibean.SyCreditBalance;
import com.sy.common.busibean.SyItemInformation;
import com.sy.common.busibean.SyMessage;
import com.sy.common.busibean.SyMessagePush;
import com.sy.common.busibean.SyPeopleInformation;
import com.sy.common.busibean.SyUser;
import com.sy.common.constant.Constant;
import com.sy.common.enums.MessageCodeConstant;
import com.sy.common.pools.DateActionConvert;
import com.sy.common.pools.RandomUtil;
import com.sy.common.utils.Shuffle;
import com.sy.server.app.service.SyItemService;
import com.sy.server.app.service.SyMessagePushService;
import com.sy.server.app.service.SyMessageService;
import com.sy.server.app.service.SyPeopleService;
import com.sy.server.app.service.SyUserService;
import com.sy.server.cache.CacheOperation;
import com.sy.server.mapper.SyAccountBalanceMapper;
import com.sy.server.mapper.SyCreditBalanceMapper;

/**
 * 用户相关信息设置 控制层 当使用@RestController时使用返回对象 当使用@Controller时返回视图jsp
 * 
 * 
 * @author YUMENG 用户加密解密均通过MD5进行
 */
@RestController
@RequestMapping("/user")
public class UserRestController {

	private Logger log = LoggerFactory.getLogger(UserRestController.class);

	// ----------------------------------------------------------------
	// ****************************************************************
	// *****************用户加密解密均通过MD5进行*************************
	// ****************************************************************
	// ----------------------------------------------------------------

	@Autowired
	private SyUserService syuserservice;

	@Autowired
	private SyMessageService symessageservice;

	@Autowired
	private SyItemService syitemservice;

	@Autowired
	private SyPeopleService sypeopleservice;

	@Autowired
	private SyMessagePushService symessagepushervice;
	
	@Autowired
	private CacheOperation cacheOperation;
	
	@Autowired
	private SyAccountBalanceMapper syAccountBalanceMapper;

	@Autowired
	private SyCreditBalanceMapper syCreditBalanceMapper;
	/**
	 * 用户登录 接口测试地址:
	 * https://127.0.0.1:8080/sy/server/user/login?phone=13607039988&&pwd=000001&st=Android&vs=4.3.0
	 * 接口参数:
	 * 
	 * @param phone
	 * @param pwd
	 * @param st
	 * @param vs
	 * @return https://127.0.0.1:8080/sy/server/user/login?ty=type2&id=people_000001&pwd=123456&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Dljk returnloginjk(@RequestParam(value = "phone", required = true) String phone,
			@RequestParam(value = "pwd", required = true) String pwd,
			@RequestParam(value = "st", required = true) String st,
			@RequestParam(value = "vs", required = true) String vs) {
		log.info("用户开始登陆.....");
		Dljk dljk = new Dljk();
		SubDljk subdljk = new SubDljk();
		try {
			MapUtil jwd = new MapUtil();
			jwd.setPhone(phone);
			List<SyUser> users = syuserservice.selectByPhone(jwd);
			if (users.size() > 0) {
				Iterator iterator = users.iterator();
				while (iterator.hasNext()) {
					SyUser syuser = (SyUser) iterator.next();
					//System.out.println("手机端录入密码加密---:"+SyUtil.MD5(pwd));
					//System.out.println("数据库存入的密码---:"+syuser.gettSyPassword());
					if (!SyUtil.MD5(pwd).equals(syuser.gettSyPassword())) {
						dljk.setCode("0");
						dljk.setMsg("110");// 110 密码错误
						return dljk;
					} else {
						subdljk.setUserid(syuser.gettSyUserId().toString());
						subdljk.setUserstate(
								(String) CommonDisk.sydisk().get("user_state_" + syuser.gettSyUserStatus()));
						subdljk.setUsername(syuser.gettSyUsername());
						subdljk.setNickname(syuser.gettSyNickname());//add zbb 新增返回昵称  2017-06-13
						subdljk.setUserpictureurl(syuser.gettSyImgpath());
						subdljk.setUserintegration(String.valueOf(syuser.gettSyIntegral()));
						dljk.setData(subdljk);
						cacheOperation.putIntoDaysCache(Constant.CACHELOGINKEY+syuser.gettSyUserId(), SyUtil.MD5(System.currentTimeMillis()+syuser.gettSyUserId()+""));
						cacheOperation.putIntoDaysCache(Constant.SIGNKEY+syuser.gettSyUserId(), SyUtil.MD5(System.currentTimeMillis()+RandomUtil.getCode(6)+""));
					}
				}
				dljk.setCode("1");
				dljk.setMsg("111");// 111 成功登陆
			} else {
				dljk.setCode("0");
				dljk.setMsg("112");// 112 该用户未注册
			}
			log.info("用户登录完成.....");
		} catch (Exception e) {
			dljk.setCode("0");
			dljk.setMsg("113");// 113 用户登录失败
			log.info("用户登录失败" + e.getMessage());
		}
		return dljk;
	}

	/**
	 * 用户注册
	 * 
	 * @param name
	 * @param desc
	 * @return http://127.0.0.1:8010/sy/server/user/register?phone=13607039988&pwd=000001&validte=000000&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/register", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Zcjk returnregisterjk(@RequestParam(value = "phone", required = true) String phone,
			@RequestParam(value = "pwd", required = true) String pwd,
			@RequestParam(value = "validte", required = true) String validte,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		log.info("用户开始注册......");
		MapUtil jwd = new MapUtil();
		Zcjk zcjk = new Zcjk();
		try {
			jwd.setPhone(phone);

			List<SyUser> users = syuserservice.selectByPhone(jwd);
			if (users.size() >= 1) {
				// 要是手机已经注册，则返回失败
				zcjk.setCode("0");
				zcjk.setMsg("100");// 100 该手机已经被注册
			} else {
				// Iterator iterator=users.iterator();
				// while(iterator.hasNext()){
				// SyUser syuser= (SyUser)iterator.next();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				//SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				String strDate = DateUtil.dateConvertFormat(new Date(), format);
				Date strDate1 = DateUtil.getFormatDate(new Date(),DateUtil.fORMAT_Old);
				//System.out.println("创建日期---:"+strDate1);
				SyUser syuser = new SyUser();
				syuser.settSyAccount(Shuffle.shuffleNumber(phone)+strDate);//账户
				syuser.settSyPhone(phone);
				syuser.settSyNickname("拾缘");//昵称
				syuser.settSyUsername(phone);//用户名称  用户登陆后即可修改
				syuser.settSySex(0);//用户性别
				syuser.settSyImgpath("/user/picture/");
				syuser.settSyPassword(SyUtil.MD5(pwd));
				syuser.settSyAccountState(0);//账户状态
				syuser.settSyCreateTime(strDate1);//创建时间
				syuser.settSyDelStatus("0");//删除状态
				syuser.settSyIsonline(1);//是否在线
				syuser.settSyIslimir(0);//是否限制
				syuser.settSyUserStatus(0);//用户状态
				syuserservice.insert(syuser);
				zcjk.setCode("1");
				zcjk.setMsg("101");// 101 该手机注册成功
				
				//钱包、积分插入数据
				syAccountBalanceMapper.insert(getSyAccountBalance(syuser));
				syCreditBalanceMapper.insert(getSyCreditBalance(syuser));
			}
			// if (pwd.compareTo(syuserservice.selectByPhone(maputil)()) == 0)
			log.info("用户注册完成.....");
		} catch (Exception e) {
			zcjk.setCode("0");
			zcjk.setMsg("102");// 102 注册失败，请联系服务人员!
			log.info("手机注册失败" + e.getMessage());
		}
		return zcjk;
	}

	//初始化钱包余额数据
	public SyAccountBalance getSyAccountBalance(SyUser syUser)
	{
		SyAccountBalance sab = new SyAccountBalance();
		BigDecimal bd = new BigDecimal(0);
		sab.settSyAccount(syUser.gettSyAccount());//账户名称
		sab.settSyCoolDate(null);//冻结时间
		sab.settSyCoolMoney(bd);//冻结金额
		sab.settSyDate(new Date());//新增时间
		sab.settSyMoney(bd);//余额
		sab.settSyStatus("0");//余额状态
		sab.settSyUpdateDate(null);//信息更新时间
		sab.settSyUserId(syUser.gettSyUserId().toString());//所属用户id
		return sab;
	}
	//初始化钱包余额数据
	public SyCreditBalance getSyCreditBalance(SyUser syUser)
	{
		SyCreditBalance scb = new SyCreditBalance();
		BigDecimal bd = new BigDecimal(0);
		scb.settSyAccount(syUser.gettSyAccount());//账户名称
		scb.settSyCoolDate(null);//冻结时间
		scb.settSyCoolMoney(bd);//冻结金额
		scb.settSyDate(new Date());//新增时间
		scb.settSyMoney(bd);//积分余额
		scb.settSyStatus("0");//积分余额状态
		scb.settSyUpdateDate(null);//信息更新时间
		scb.settSyUserId(syUser.gettSyUserId().toString());//所属用户id
		return scb;
	}
	
	/**
	 * 获取验证码 * 接口地址: 接口参数:
	 * 
	 * @param phone
	 * @param st
	 * @param vs
	 * @return http://127.0.0.1:8010/sy/server/user/identifyingcode?phone=15718821505&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/identifyingcode", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Yzmjka returnidentifyingcodejk(@RequestParam(value = "phone", required = true) String phone,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		// 需要确定短信商接口云之讯短信
		log.info("开始获取验证码......");
		Yzmjka yzmjka = new Yzmjka();
		try {
			String messageStr = "您的手机动态码：str。工作人员不会向您索要，如果非本人操作请忽略。";
			MapUtil jwd = new MapUtil();
			jwd.setPhone(phone);
			yzmjka.setCode("1");
			yzmjka.setMsg("120"); // 120 验证码发送成功
			String str = RandomUtil.getCode(6);
			yzmjka.setData(str);
			messageStr = messageStr.replace("str", str);
			String returnString = HttpSender.send(Constant.URL,Constant.ACCOUNT,Constant.PSWD,phone,messageStr, Constant.NEEDSTATUS, "123", "");
			String[] strArray  = returnString.split(",");
			log.info("短信平台返回结果集:\r\n"+returnString);
			if(strArray[1].startsWith("0")){
				log.info("用户获取验证码完成.....后台生成验证码:"+str+"   "+returnString);
			}else{
				log.info("短信平台服务异常.....后台生成验证码:"+str+"   "+returnString);

			}
		} catch (Exception e) {
			yzmjka.setCode("0");
			yzmjka.setMsg("121");// 121 验证码发送失败
			log.info("用户获取验证码失败" + e.getMessage());
		}
		return yzmjka;
	}

	/**
	 * 忘记登录密码/修改登录密码
	 * 
	 * @param phone
	 * @param cd
	 * @param np
	 * @param st
	 * @param vs
	 * @return http://127.0.0.1:8010/sy/server/user/forgetpwd?phone=18697712690&cd=123&np=654321&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/forgetpwd", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody blankjk returnpasswordjk(@RequestParam(value = "phone", required = true) String phone,
			@RequestParam(value = "cd", required = false) String cd,
			@RequestParam(value = "np", required = false) String np,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		// 需要确定短信商接口云之讯短信
		log.info("忘记密码......");
		blankjk blankjk = new blankjk();
		try {
			MapUtil jwd = new MapUtil();
			jwd.setPhone(phone);
			SyUser syuser = new SyUser();
			List<SyUser> users = syuserservice.selectByPhone(jwd);
			if (users.isEmpty()) {
				blankjk.setCode("0");
				blankjk.setMsg("112");//
			} else {
				syuser = (SyUser) users.get(0);
				syuser.settSyPassword(SyUtil.MD5(np));
				syuserservice.updateByPrimaryKey(syuser);
			}
			blankjk.setCode("1");
			blankjk.setMsg("130");
			log.info("用户修改密码完成.....");
		} catch (Exception e) {
			blankjk.setCode("0");
			blankjk.setMsg("131");
			log.info("用户修改密码失败" + e.getMessage());
		}
		return blankjk;
	}

	/**
	 * 获取个人信息接口
	 * 
	 * @param uid
	 *            用户id
	 * @param st
	 *            平台
	 * @param vs
	 *            版本
	 * @return http://127.0.0.1:8010/sy/server/user/userinfo?uid=1&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/userinfo", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Yhxxjk returnuserinfojk(@RequestParam(value = "uid", required = true) Integer uid,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		log.info("获取个人信息......");
		Yhxxjk yhxx = new Yhxxjk();
		SubYhxxjk subyhxx = new SubYhxxjk();
		try {
			SyUser syuser = new SyUser();
			SyUser user = syuserservice.query(uid);
			if (user != null) {
				subyhxx.setName(user.gettSyUsername());// 姓名
				subyhxx.setNickname(user.gettSyNickname());// 昵称
				subyhxx.setSex(user.gettSySex().toString());// 性别   0 男  1 女
				subyhxx.setBirthday(
						user.gettSyDateofbirth() == null ? null : DateUtil.dateConvertStr(user.gettSyDateofbirth()));// 出生年月日
				subyhxx.setNumber(user.gettSyCardid());// 证件号
				subyhxx.setEmail(user.gettSyMail());// 电子邮箱
				subyhxx.setPhone(user.gettSyPhone());// 绑定手机
				subyhxx.setImgurl(user.gettSyImgpath());// 用户头像url
				yhxx.setData(subyhxx);
			} else {
				yhxx.setCode("0");
				yhxx.setMsg("140");//
				return yhxx;
			}
			yhxx.setCode("1");
			yhxx.setMsg("141");
			log.info("获取个人信息完成.....");
		} catch (Exception e) {
			yhxx.setCode("0");
			yhxx.setMsg("142");
			log.info("获取个人信息失败" + e.getMessage());
		}
		return yhxx;
	}

	/**
	 * 修改个人信息接口
	 * 
	 * @param uid
	 *            // 用户id
	 * @param name
	 *            // 名字
	 * @param nickname
	 *            // 昵称
	 * @param sex//
	 *            性别
	 * @param bd
	 *            // 出生年月日
	 * @param no
	 *            // 证件号
	 * @param em
	 *            // 电子邮箱
	 * @param ph
	 *            // 电话
	 * @param st
	 *            // 平添
	 * @param vs
	 *            // 版本
	 * @return http://127.0.0.1:8010/sy/server/user/usermodify?uid=3&name=王亮&sex=0&nickname=jack&
	 * bd=19881205&no=232325198812897654&em=wangliang86@yeah.net&ph=15718821501&uri=/pht/user/usermodify&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/usermodify", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Yhxxjk returnmodifyuserjk(@RequestParam(value = "uid", required = true) Integer uid,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "nickname", required = false) String nickname,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "bd", required = false) String bd,
			@RequestParam(value = "no", required = false) String no,
			@RequestParam(value = "em", required = false) String em,
			@RequestParam(value = "ph", required = false) String ph,
			@RequestParam(value = "imgurl", required = false) String uri,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		log.info("修改个人信息......");
		Yhxxjk yhxx = new Yhxxjk();
		SubYhxxjk subyhxx = new SubYhxxjk();
		try {
			SyUser user = syuserservice.query(uid);
			if (user != null) {
				subyhxx.setBirthday(bd);
				subyhxx.setEmail(em);
				subyhxx.setName(name);
				subyhxx.setNickname(nickname);
				subyhxx.setNumber(no);
				subyhxx.setSex(sex);
				subyhxx.setPhone(ph);
				// subyhxx.setType(ty);//by2字段 用作成：证件类型
				subyhxx.setImgurl(uri);// 头像地址
				yhxx.setData(subyhxx);
				user.settSyDateofbirth(DateActionConvert.dateReconvert(bd));// 生日
				user.settSyMail(em);//邮件
				user.settSyNickname(nickname);//昵称
				user.settSyUsername(name);//用户名
				user.settSyCardid(no);// 身份证
				user.settSySex(Integer.valueOf(sex));
				user.settSyPhone(ph);
				// user.settSyBz2(ty);
				user.settSyImgpath(uri);
				syuserservice.updateByPrimaryKey(user);
				yhxx.setCode("1");
				yhxx.setMsg("151");
				log.info("修改个人信息完成.....");
			} else {
				yhxx.setCode("0");
				yhxx.setMsg("150");
				return yhxx;
			}
		} catch (Exception e) {
			yhxx.setCode("0");
			yhxx.setMsg("152");
			log.info("修改个人信息失败" + e.getMessage());
		}
		return yhxx;
	}

	/*
	 * 我的发布消息/我的帮助消息
	 * 
	 * @param uid
	 * @param msty
	 * @param st
	 * @param vs
	 * @return https://127.0.0.1:8080/sy/server/user/message?uid=1&msty=0&st=Android&vs=4.3.0
	 */
	/*@RequestMapping(value = "/message", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Wdxxjk returnmymessagejk(@RequestParam(value = "uid", required = true) String uId,
			@RequestParam(value = "msty", required = true) String msty,   
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		log.info("我的发布和帮助消息......");
		Wdxxjk wdxx = new Wdxxjk();
		SubWdxxjk subwdxx = new SubWdxxjk();
		try {
			MapUtil jwd = new MapUtil();
			jwd.setUid(uId);
			jwd.setMsgtype(msty);//// 消息类型（求助，发布）}
			List<SyMessage> lmsg = symessageservice.selectByItem(jwd);
			if (lmsg.isEmpty()) {
				wdxx.setCode("0");
				wdxx.setMsg("180");
				return wdxx;
			} else {
				Iterator iterator = lmsg.iterator();
				while (iterator.hasNext()) {
					SyMessage symessage = (SyMessage) iterator.next();
					//subwdxx.setGoodstype(String.valueOf(symessage.gettSyThingsType()));// 商品类型
					subwdxx.setMsgid(symessage.gettSyMessageId().toString());
					subwdxx.setMsgName(symessage.gettSyThingsName());
					//subwdxx.setGoodsname(symessage.gettSyThingsName());
					subwdxx.setMsgstate((String) CommonDisk.sydisk().get(symessage.gettSyMessageState()));
					// if(symessage.gettSyMessageType()==1){//消息类型（求助，发布）}
					MapUtil maputil = new MapUtil();
					maputil.setMsgid(symessage.gettSyMessageId().toString());
					if (symessage.gettSyThingsType().equals("1")) {// 失物类型（物）
						SyItemInformation si = syitemservice.selectByMessageId(maputil);
						if(si!=null){
							String strDate = DateUtil.dateConvertStr(si.gettSyLoseDate(),DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss);
							subwdxx.setGoodstype(si.gettSyItemType());// 商品类型
							subwdxx.setGoodsname(si.gettSyItemName());
							subwdxx.setLostdate(strDate);
							subwdxx.setLostAdress(si.gettSyLoseAddress());
							subwdxx.setPicUrl(si.gettSyImgpath());
							subwdxx.setJe(Long.toString(si.gettSyBountyMoney()));
						}else{
							subwdxx.setLostdate(" ");
						}
					} else {
						SyPeopleInformation sp = sypeopleservice.selectByMessageId(maputil);
						if(sp!=null){
							String strDate = DateUtil.dateConvertStr(sypeopleservice.selectByMessageId(maputil).gettSyLostDate(),DateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss);
							subwdxx.setLostdate(strDate);
							subwdxx.setLostAdress(sp.gettSyLostAddress());
							subwdxx.setPicUrl(sp.gettSyBz1());//丢失人员头像
							subwdxx.setJe(sp.gettSyBountyMoney().toString());
						}else{
							subwdxx.setLostdate(" ");
						}
					}
					wdxx.getData().add(subwdxx);
				}
			}
			wdxx.setCode("1");
			wdxx.setMsg("181");
			log.info("获取我的发布和帮助消息完成.....");
		} catch (Exception e) {
			wdxx.setCode("0");
			wdxx.setMsg("182");
			log.info("获取我的发布和帮助消息失败" + e.getMessage());
		}
		return wdxx;
	}*/

	/**
	 * 我的消息列表 1.接口参数 用户id(必填) 操作平台(Android、iOS) 使用版本 2.接口返回结果 { "code":
	 * 1,//成功或者失败标志 "msg": ""//失败时的原因，成功时为"" "data": [//成功时返回的数据集，失败时为"" 
	 * { 时间1,
	 * "msgs1": [ { 消息id1, 消息内容 }, { 消息id2, 消息内容 } ] 
	 * }, 
	 * { 时间2, 
	 * "msgs2": [ { 消息id3, 消息内容 }, { 消息id4, 消息内容 } ] 
	 * }
	 * 
	 */
	/**
	 * 我的消息列表  推送消息列表     邮件信息
	 * 
	 * @param uid
	 * @param st
	 * @param vs
	 * @return http://127.0.0.1:8010/sy/server/user/msglist?uid=user_000001&st=Android&vs=4.3.0&page=0
	 */
	@RequestMapping(value = "/msglist", produces="application/json;charset=UTF-8",method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean returnUserMessageInfo(@RequestParam(value = "uid", required = true) String uid,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs,
			@RequestParam(value = "page", required = false) int page) {
		RespondBean respBean = null;
		MapUtil mapUtil = new MapUtil();
		try {
			mapUtil.setUid(uid);//用户唯一主键标识
			mapUtil.setPage(page*20);//默认每页显示20条，通过页面计算查询多少条数据
			mapUtil.setPageUp((page+1)*20);//默认每页显示20条，通过页面计算查询多少条数据
			mapUtil.setVersionType(st);
			mapUtil.setVersionNum(vs);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
			List<SyMessagePush> msgs = symessagepushervice.selectByItem(mapUtil);//根据用户唯一主键标识，查询我的消息表信息
			List<SubWdxxlbsub> lsubwdxxsub = new ArrayList<SubWdxxlbsub>();
			for (SyMessagePush symsgpush : msgs) {
				String strDate =format.format(symsgpush.gettSyPushDate());  
				SubWdxxlbsub subwdxxsub = new SubWdxxlbsub();
				subwdxxsub.settSyMessagePushId(symsgpush.gettSyMessagePushId());//消息id
				subwdxxsub.settSyTheme(symsgpush.gettSyTheme());//消息主题 
				subwdxxsub.settSyPushDate(strDate);//推送时间
				subwdxxsub.settSyPushContent(symsgpush.gettSyPushContent());//消息内容
				subwdxxsub.settSyRwStatus(symsgpush.gettSyRwStatus());//读取状态  0   未读取  1   已读取
				lsubwdxxsub.add(subwdxxsub);
			}
			respBean = new RespondBean(lsubwdxxsub, MessageCodeConstant.PUSH_MSG_LIST_SUCCESS);
		} catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.PUSH_MSG_LIST_ERROR);
			log.info("获取推送消息列表失败" + e.getMessage());
		}
		return respBean;
	}

	/**
	 * 15.我的消息详情接口 1.接口参数 消息id(必填) 操作平台(Android、iOS) 使用版本 2.接口返回结果 { "code":
	 * 1,//成功或者失败标志 "msg": "",//失败时的原因，成功时为"" "data": {//成功时返回的数据集，失败时为"" 时间 } }
	 * 
	 * @return http://127.0.0.1:8010/sy/server/user/msgdetail?msgid=1&st=Android&vs=4.3.0
	 */
	
	@RequestMapping(value = "/msgdetail", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean returnUserMessageDetail(@RequestParam(value = "msgid", required = true) String msgid,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		RespondBean respBean = null;
		//SubWdxxlbsub subwdxxsub = new SubWdxxlbsub();
		try {
			SyMessagePush symsgpush = symessagepushervice.selectByPrimaryKey(Integer.parseInt(msgid));//根据消息唯一主键标识，查询我的消息表信息
			/*List<SubWdxxlbsub> lsubwdxxsub = new ArrayList<SubWdxxlbsub>();
			subwdxxsub.setMsgid(symsgpush.gettSyMessagePushId());//消息id
			subwdxxsub.setMsginfo(symsgpush.gettSyPushContent());//消息内容
			subwdxxsub.setMsgDate(symsgpush.gettSyPushDate());//推送时间
			subwdxxsub.setMsgtheme(symsgpush.gettSyTheme());//消息主题
			subwdxxsub.setMsguser(symsgpush.gettSyPushPeople());//推送人
			subwdxxsub.setUserid(symsgpush.gettSyPushWasPeople());//被推人ID
			subwdxxsub.setMsgdelstatus(symsgpush.gettSyDelStatus());//删除标识（逻辑删除）
			subwdxxsub.setMsgdeldate(symsgpush.gettSyDelDate());//删除时间（逻辑删除）
			lsubwdxxsub.add(subwdxxsub);*/
			respBean = new RespondBean(symsgpush, MessageCodeConstant.PUSH_MSG_DETIAL_SUCCESS);
		} catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.PUSH_MSG_DETIAL_ERROR);
			log.info("获取推送消息明细失败" + e.getMessage());
		}
		return respBean;
	}

	/**
	 * 16.我的消息删除接口 1.接口参数 消息id(必填) 用户id(必填) 操作平台(Android、iOS) 使用版本 2.接口返回结果 {
	 * "code": 1,//成功或者失败标志 "msg": ""//失败时的原因，成功时为"" }
	 * 
	 * @return http://127.0.0.1:8010/sy/server/user/msgdel?uid=user_000001&st=Android&vs=4.3.0&msgid=1
	 */
	@RequestMapping(value = "/msgdel", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean returnDelUserMessage(@RequestParam(value = "uid", required = true) String uid,
			@RequestParam(value = "msgid", required = true) String msgid,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		RespondBean respBean = null;
		MapUtil mapUtil = new MapUtil();
		try {
			mapUtil.setMsgid(msgid);//消息唯一标识
			mapUtil.setUid(uid);//用户唯一标识
			mapUtil.setVersionType(st);
			mapUtil.setVersionNum(vs);
			symessagepushervice.deleteByItem(mapUtil);
			respBean = new RespondBean(null, MessageCodeConstant.PUSH_MSG_DEL_SUCCESS);
		} catch (Exception e) {
			respBean = new RespondBean(null, MessageCodeConstant.PUSH_MSG_DEL_ERROR);
			log.info("删除推送消息失败" + e.getMessage());
		}
		return respBean;
	}
	

	/**
	 * wangliang 更新推送消息读取状态
	 * @param uid
	 * @param msgid
	 * @param st
	 * @param vs  http://127.0.0.1:8010/sy/server/user/msgUpdate?uid=user_000001&st=Android&vs=4.3.0&msgId=5
	 * @return 
	 */
	@RequestMapping(value = "/msgUpdate", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean returnUpdateUserMessage(@RequestParam(value = "uId", required = false) String uId,
			@RequestParam(value = "msgId", required = true) String msgId,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		RespondBean respBean = null;
		SyMessagePush smp = new SyMessagePush();
		try {
			SyMessagePush symsgpush = symessagepushervice.selectByPrimaryKey(Integer.parseInt(msgId));//根据消息唯一主键标识，查询我的消息表信息
			if(symsgpush != null){
				symsgpush.settSyRwStatus(Constant.PMState_Y);
				symessagepushervice.updateByPrimaryKey(symsgpush);
				respBean = new RespondBean(null, MessageCodeConstant.PUSH_MSG_UPDATE_SUCCESS);
			}else{
				respBean = new RespondBean(MessageCodeConstant.PUSH_MSG_UPDATE_ERROR);
			}
		} catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.PUSH_MSG_UPDATE_EXCEPTION);
			log.info("更新推送消息状态失败" + e.getMessage());
		}
		return respBean;
	}
	
	
	/**
	 * wangliang  app系统退出
	 * @param uid
	 * @param st
	 * @param vs  http://127.0.0.1:8010/sy/server/user/sysExit?uId=user_000001&st=Android&vs=4.3.0
	 * @return 
	 */
	@RequestMapping(value = "/sysExit", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean returnSysExit(@RequestParam(value = "uId", required = false) String uId,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		RespondBean respBean = null;
		try {
			SyUser user = syuserservice.query(Integer.valueOf(uId));
			if(user != null){
				user.settSyIsonline(0);//用户离线
				//更新用户表登录状态
				syuserservice.updateByPrimaryKey(user);
				//除去缓存
				cacheOperation.removeFromForeverCache(Constant.CACHELOGINKEY+user.gettSyUserId());
				respBean = new RespondBean(null,MessageCodeConstant.EXIT_MSG_SUCCESS);
			}else{
				respBean = new RespondBean(null,MessageCodeConstant.EXIT_MSG_ERROR);
			}
		} catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.EXIT_MSG_EXCEPTION);
			log.info("系统退出失败" + e.getMessage());
		}
		return respBean;
	}
	
	
	/**
	 * 设置支付密码
	 * 
	 * @param uid
	 * @param pd
	 * @param st
	 * @param vs
	 * @return http://127.0.0.1:8010/sy/server/user/setPayPassWord?uId=44&pd=123&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/setPayPassWord", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean setPayPasswordjk(@RequestParam(value = "uId", required = true) String uId,
			@RequestParam(value = "pd", required = false) String pd,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		// 需要确定短信商接口云之讯短信
		log.info("设置支付密码......");
		RespondBean respBean = null;
		try {
			SyUser user = syuserservice.query(Integer.valueOf(uId));
			if (user!=null) {
				user.settSyPayPassword(Integer.valueOf(pd));
				syuserservice.updateByPrimaryKey(user);
				respBean = new RespondBean(null,MessageCodeConstant.PAY_MSG_SUCCESS);
			} else {
				respBean = new RespondBean(MessageCodeConstant.PAY_MSG_ERROR);
			}
			log.info("用户设置支付密码完成.....");
		} catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.PAY_MSG_EXCEPTION);
			log.info("用户设置支付密码失败" + e.getMessage());
		}
		return respBean;
	}
	
	
	/**
	 * 修改支付密码
	 * @param uid
	 * @param phone
	 * @param verCode
	 * @param oPwd
	 * @param nPwd
	 * @param st
	 * @param vs
	 * @return http://127.0.0.1:8010/sy/server/user/alterPayPassWord?uId=47&phone=15718821505&verCode=123456&oPwd=123456&nPwd=11111&st=Android&vs=4.3.0
	 */
	@RequestMapping(value = "/alterPayPassWord", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean alterPayPasswordjk(@RequestParam(value = "uId", required = true) String uId,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "verCode", required = false) String verCode,
			@RequestParam(value = "oPwd", required = false) String oPwd,
			@RequestParam(value = "nPwd", required = false) String nPwd,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs) {
		// 需要确定短信商接口云之讯短信
		log.info("修改支付密码开始......");
		RespondBean respBean = null;
		try {
			SyUser user = syuserservice.query(Integer.valueOf(uId));
			if (user!=null) {
				if(oPwd.equals(user.gettSyPayPassword().toString())){
					user.settSyPayPassword(Integer.valueOf(nPwd));
					syuserservice.updateByPrimaryKey(user);
					respBean = new RespondBean(null,MessageCodeConstant.ALTER_PAY_MSG_SUCCESS);
				}
			} else {
				respBean = new RespondBean(MessageCodeConstant.ALTER_PAY_MSG_ERROR);
			}
			log.info("用户修改支付密码完成....."); 
		} catch (Exception e) {
			respBean = new RespondBean(MessageCodeConstant.ALTER_PAY_MSG_EXCEPTION);
			log.info("用户修改支付密码失败" + e.getMessage());
		}
		return respBean;
	}
	

	/**
	 * 更改绑定手机号
	 * @param uId  用户id
	 * @param phone 
	 * @param st
	 * @param vs
	 * http://127.0.0.1:8010/sy/server/user/alterPhone?uId=47&phone=18888888&st=Android&vs=4.3.0
	 * @return
	 */
	@RequestMapping(value = "/alterPhone", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBean alterPhoneJk(@RequestParam(value = "uId", required = true) String uId,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "st", required = false) String st,
			@RequestParam(value = "vs", required = false) String vs){
		
		log.info("更改绑定手机号开始......");
		RespondBean respBean = null;
		try{
			SyUser user = syuserservice.query(Integer.valueOf(uId));
			if(user != null){
				user.settSyPhone(phone);
				syuserservice.updateByPrimaryKey(user);
				log.info("更改绑定手机号完成....."); 
				respBean = new RespondBean(null,MessageCodeConstant.ALTER_USER_PHONE_SUCCESS);
			}else{
				respBean = new RespondBean(MessageCodeConstant.ALTER_USER_PHONE_ERROR);
				log.info("用户不存在.....");  
			}
		}catch (Exception e) {
			// TODO: handle exception
			respBean = new RespondBean(MessageCodeConstant.ALTER_USER_PHONE_EXCEPTION);
		}
		return respBean;
	}
	
}
