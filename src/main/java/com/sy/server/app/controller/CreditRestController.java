package com.sy.server.app.controller;

import java.text.SimpleDateFormat;
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

import com.sy.common.annotation.MapUtil;
import com.sy.common.bean.Creditjk;
import com.sy.common.bean.RespondBean;
import com.sy.common.bean.RespondBeanAndBalance;
import com.sy.common.bean.SubCreditjk;
import com.sy.common.busibean.SyIntegral;
import com.sy.common.enums.MessageCodeConstant;
import com.sy.server.app.service.SyCreditService;

/**
 * 我的积分相关设置 控制层 当使用@RestController时使用返回对象 当使用@Controller时返回视图jsp
 * 
 * @author wangliang
 */
@RestController
@RequestMapping("/credit")
public class CreditRestController {

	private Logger log = LoggerFactory.getLogger(CreditRestController.class);
	@Autowired
	private SyCreditService syCreditService;

	/**
	 * 积分明细 列表 wl
	 * 接口地址:http://127.0.0.1:8010/sy/server/credit/creditList?uId=33&st=Android&vs=4.3&page=0
	 * 接口参数:
	 * @param userId 用户Id
	 * @param st 平台类型
	 * @param vs 应用版本号
	 * @param page 返回页码值
	 * @return RespondBean
	 */
	@RequestMapping(value = "/creditList",produces="application/json;charset=UTF-8",method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody RespondBeanAndBalance returnCreditDetailjk(@RequestParam(value = "uId", required = true) String userId,
			@RequestParam(value = "st", required = true) String st,
			@RequestParam(value = "vs", required = true) String vs,
			@RequestParam(value = "page", required = true) int page) {
		log.info("积分明细信息开始登陆.....");
		RespondBeanAndBalance rb =null;
		Creditjk pjk = new Creditjk();
		List<SubCreditjk> lists = new ArrayList<SubCreditjk>();
		MapUtil mu = new MapUtil();
		mu.setPage(page*20);
		mu.setPageUp((page+1)*20);
		mu.setUid(userId);
		mu.setVersionType(st);
		mu.setVersionNum(vs);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
		String strDate =format.format(new Date());  
		Integer sum = 0;
		try{
			@SuppressWarnings("unchecked")
			List<SyIntegral> syCredits = (List<SyIntegral>) syCreditService.selectByItem(mu);
			if (syCredits.size() > 0) {
				for(SyIntegral syCredit:syCredits){ 
					Integer cSum = syCredit.gettSyIntegaralNumber();
					SubCreditjk subCreditjk = new SubCreditjk();
					subCreditjk.setCreditId(syCredit.gettSyIntegralId());
					subCreditjk.setCreditReason(syCredit.gettSyIntegaralType());
					subCreditjk.setCreditNumber(syCredit.gettSyIntegaralNumber());
					subCreditjk.setCreditDate(strDate);
					lists.add(subCreditjk);
					sum =sum+cSum;
				}
				rb = new RespondBeanAndBalance(lists,sum,MessageCodeConstant.CREDIT_GET_LIST_SUCCESS);
			} else {
				rb = new RespondBeanAndBalance(MessageCodeConstant.CREDIT_GET_LIST_FALI);
			}
			log.info("积分明细信息查询完成.....");
		}catch (Exception e) {
			rb = new RespondBeanAndBalance(MessageCodeConstant.CREDIT_LIST_EXCEPTION);
			log.info("积分明细信息查询失败.....");
		}
		return rb;
	}
}
