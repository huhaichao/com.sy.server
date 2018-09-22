package com.sy.server.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * 将ehcahe缓存管理 注入springboot 上下文中
 * @author huchao
 *
 */
@Configuration
@EnableCaching
public class CacheConfigRegister {
	
	/**
	 * 将ehcache缓存管理类注入到springboot 上下文中
	 * @return
	 */
	@Bean
	public  EhCacheCacheManager  ehCacheCacheManager(EhCacheManagerFactoryBean ehCacheManagerFactoryBean){
		return new EhCacheCacheManager(ehCacheManagerFactoryBean.getObject());
	}
	
	
	
	/**
	 * 将ehcache缓存工厂注入到springboot 上下文中
	 * @return
	 */
	@Bean
	public  EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
		EhCacheManagerFactoryBean  ehcacheFactory = new EhCacheManagerFactoryBean();
		ehcacheFactory.setConfigLocation(new ClassPathResource("ehcache.xml"));
		ehcacheFactory.setShared(true);
		return ehcacheFactory;
	}

}
