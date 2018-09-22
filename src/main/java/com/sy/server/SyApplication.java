package com.sy.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sy.server.aop.DynamicDatasourcesLoad;
/**
 * spring boot 启动类
 * @author huchao
 *
 */
@Import({DynamicDatasourcesLoad.class})
@EnableTransactionManagement
@SpringBootApplication
@EnableScheduling 
public class SyApplication {
	public static void main(String[] args) {
        SpringApplication.run(SyApplication.class, args);
	}
}
