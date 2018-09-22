package com.sy.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.sy.server.interceptor.LoginCheck;

/**
 * 登录验证拦截器
 * @author huchao
 *
 */
@Configuration
public class SYWebMvcConfigurer extends WebMvcConfigurerAdapter {/*
	
	   @Autowired
	   private LoginCheck  loginCheck;
	   *//**
	    * 登录验证拦截器
	    *//*
	
	   @Override
	   public void addInterceptors(InterceptorRegistry registry) {
	    	
	        registry.addInterceptor(loginCheck)
	        .addPathPatterns("/**")
	        .excludePathPatterns("/login")
	        ;
	        super.addInterceptors(registry);
	    }
*/}
