package com.sy.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sy.server.cache.CacheOperation;


/**
 * 缓存测试
 * @author huchao
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=SyApplication.class)
public class EhcacheTest {
	private  Logger   log  = LoggerFactory.getLogger(EhcacheTest.class);
	@Autowired
	private CacheOperation cacheOperation;
	
	/**
	 * 数据缓存测试
	 */
	@Test
	public  void CacheTest(){
		//CacheOperation cacheOperation  = CacheOperation.newInstanced();
//		Demo  demo = new Demo();
//		demo.setDate(new Date());
//		demo.setId(13131L);
//		demo.setDemoname("keyname");
		/**
		 * 将demo 保存到600scache缓存中
		 * 
//		 */
//		try {
//			log.info("将demo 保存到600scache缓存中");
//			cacheOperation.add(Constant.SECONDSCACHE, Constant.CACHEPREFIX+"demo", demo);
//			cacheOperation.putIntoForeverCache(Constant.CACHEPREFIX+"demo", demo);
//			cacheOperation.add(Constant.FOREVERCACHE, Constant.CACHEPREFIX+"demo", demo);
//			cacheOperation.putIntoSecondsCache(Constant.CACHEPREFIX+"demo", demo);
//			
//			cacheOperation.addIfAbsent(Constant.SECONDSCACHE, Constant.CACHEPREFIX+"demo", demo);
//			cacheOperation.putIfAbsentIntoForeverCache(Constant.CACHEPREFIX+"demo", demo);
//			cacheOperation.addIfAbsent(Constant.FOREVERCACHE, Constant.CACHEPREFIX+"demo", demo);
//			cacheOperation.putIfAbsentIntoSecondsCache(Constant.CACHEPREFIX+"demo", demo);
//		} catch (SyException e) {
//			log.error("exception is "+e.getMessage());
//		}
		
		/**
		 * 获取 demo 缓存
		 */
//		Demo demofromcache =cacheOperation.get(Constant.SECONDSCACHE, Constant.CACHEPREFIX+"demo",Demo.class);
//		System.out.println("SECONDSCACHE --- "+demofromcache.getDemoname());
//		Demo demofromcache3 =cacheOperation.get(Constant.FOREVERCACHE, Constant.CACHEPREFIX+"demo",Demo.class);
//		System.out.println("FOREVERCACHE --- "+demofromcache3.getDemoname());
//		
//		Demo demofromcache4 =cacheOperation.getFromSecondsCache( Constant.CACHEPREFIX+"demo",Demo.class);
//		System.out.println("SECONDSCACHE --- "+demofromcache4.getDemoname());
//		Demo demofromcache5 =cacheOperation.getFromForeverCache(Constant.CACHEPREFIX+"demo",Demo.class);
//		System.out.println("FOREVERCACHE --- "+demofromcache5.getDemoname());
//		
//		Demo demofromcache6 =(Demo)cacheOperation.getFromSecondsCache(Constant.CACHEPREFIX+"demo");
//		System.out.println("SECONDSCACHE --- "+demofromcache6.getDemoname());
//		Demo demofromcache7 =(Demo)cacheOperation.getFromForeverCache(Constant.CACHEPREFIX+"demo");
//		System.out.println("SECONDSCACHE --- "+demofromcache7.getDemoname());
//		/**
//		 * 获取缓存大小
//		 */
//		System.out.println(cacheOperation.size("600scache"));
//		System.out.println("FOREVERCACHE ---"+cacheOperation.sizeOfForeverCache());
//		System.out.println("SECONDSCACHE ---"+cacheOperation.sizeOfSecondsCache());
//		/**
//		 * 移除demo缓存
//		 */
//		cacheOperation.remove("600scache","demo");
//		Demo demofromcache2 =cacheOperation.get(Constant.SECONDSCACHE, Constant.CACHEPREFIX+"demo",Demo.class);
//		if(demofromcache2==null){
//			//错误日志输出
//			log.error("demofromcache2 is null");
//		}else {
//			log.info(demofromcache2.getDemoname());
//		}
	}

}
