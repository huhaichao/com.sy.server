package com.sy.server.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sy.common.annotation.TargetDatasource;
import com.sy.common.pools.DataSourceRegister;

/**
 * 数据源切面
 * @author huchao
 *
 */
@Aspect
@Order(-1)//保证在@TargetDatasource之前加载
@Component
public class DynamicDatasourcesAspect {
	
	private  Logger   logger  = LoggerFactory.getLogger(DynamicDatasourcesAspect.class);
    
	/**
	 * 调用TargetDatasource(ds)在方法前切换当前线程中的数据源
	 * @param point
	 * @param ds
	 */
	@Before("@annotation(ds)")
	public  void registerDatasource(JoinPoint point,TargetDatasource ds){
	    logger.info("datasource change .....");	
		String  name = ds.value();
		if(DataSourceRegister.containsDatasource(name)){
			logger.info("register datasourceid  ["+name+"] into currentyThread....");
		    DataSourceRegister.setDataSources(name);
		}else {
			logger.info("datasourceid is not exit,user default datasource");
		}
			
	}
	
	/**
	 * 调用@TargetDatasource(ds)在方法后移除当前线程中的数据源
	 * @param point
	 * @param ds
	 */
	@After("@annotation(ds)")
	public  void removeDatasource(JoinPoint point,TargetDatasource ds){
		logger.info("remove datasource from currentThread...");
		DataSourceRegister.removeDataSource();
	}
	
	
}
