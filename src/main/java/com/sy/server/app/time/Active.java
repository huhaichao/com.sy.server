package com.sy.server.app.time;

import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sy.common.annotation.DateUtil;
import com.sy.common.annotation.MapUtil;
import com.sy.common.busibean.SyMessage;
import com.sy.server.app.service.SyMessageService;

@Component 
public class Active {
	
	@Autowired
	private SyMessageService symessageservice;
	private org.slf4j.Logger log = LoggerFactory.getLogger(Active.class);
	//@Scheduled(fixedRate=30000) //零点执行
	@Scheduled(cron="0 0 00 * * ?") //零点执行
    public void MessageActive() {   
    	//获取所有的messages
    	MapUtil  mu = new MapUtil();
    	List<SyMessage> mgs = symessageservice.getListMessages(mu);
    	for(SyMessage m :mgs){
    		if(DateUtil.getCompareTime(m.gettSyExpirationDate(),new Date())){
    			m.settSyMessageState(1);
    			symessageservice.updateByPrimaryKey(m);
    			log.info("mesId  "+m.gettSyMessageId()+"已过期，并失效");
    		}else{
    			log.info("mesId"+m.gettSyMessageId()+"未过期");
    		}
    	}
    }    
  
   /* @Scheduled(fixedRate=10000)  
    public void testTasks() {      
    	log.info("每20秒执行一次。开始……");  
        //statusTask.healthCheck();  
    	log.info("每20秒执行一次。结束。");  
    }    */
}
