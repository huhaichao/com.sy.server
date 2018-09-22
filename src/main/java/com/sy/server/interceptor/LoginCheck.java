package com.sy.server.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sy.common.bean.RespondBean;
import com.sy.common.constant.Constant;
import com.sy.common.enums.MessageCodeConstant;
import com.sy.server.app.controller.UserRestController;
import com.sy.server.cache.CacheOperation;

/**
 * 登录过滤器
 * @author huchao
 *
 */
@Component
public class LoginCheck implements HandlerInterceptor {
	
	@Autowired
	private CacheOperation cacheOperation;
	
	private Logger log = LoggerFactory.getLogger(LoginCheck.class);
	
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		log.info("url="+arg0.getRequestURI());
		log.info("param="+arg0.getParameterMap());
		String loginKey = arg0.getHeader("loginKey");
		
		if(loginKey == null){
			 loginKey = arg0.getParameter("loginKey");
		}
		
		if(loginKey==null || "".equals(loginKey)){
			PrintWriter  pw =arg1.getWriter();
			respondBody(pw,new RespondBean(MessageCodeConstant.NOINFO_LOGINKEY));
			return false;
		}
		String userId = arg0.getParameter("userId");
		
		if(userId==null || "".equals(userId)){
			
            PrintWriter  pw =arg1.getWriter();
			respondBody(pw,new RespondBean(MessageCodeConstant.NOINFO_USERID));
			return false;
		}
		
		Object userLoginkey = cacheOperation.getFromDaysCache(Constant.CACHELOGINKEY+userId);
		
		if(userLoginkey == null || "".equals(userLoginkey.toString())){
			PrintWriter  pw =arg1.getWriter();
            respondBody(pw,new RespondBean(MessageCodeConstant.NO_LOGIN));
			return false;
		}
		
		if (loginKey.equals(userLoginkey.toString())){
			log.info(userId+"===loginKey="+loginKey+"===校验登录成功!");
			return true;
		}else {
			log.info(userId+"===loginKey="+loginKey+"==="+userLoginkey+"无法匹配登录信息,登录校验不过");
            PrintWriter  pw =arg1.getWriter();
            respondBody(pw,new RespondBean(MessageCodeConstant.NO_LOGIN));
			return false;
		}
		
	}
	
	
	private void  respondBody(PrintWriter pw,RespondBean rp){
		if(rp==null)
			rp = new RespondBean(MessageCodeConstant.NO_LOGIN);
		pw.write(rp.toString());
		pw.flush();
		
		if (pw!=null) {
			pw.close();
		}
	}

}
