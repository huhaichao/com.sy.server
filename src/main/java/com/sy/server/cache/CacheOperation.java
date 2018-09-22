package com.sy.server.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sy.common.Exception.SyException;
import com.sy.common.constant.Constant;

import net.sf.ehcache.CacheManager;

/**
 * 内存缓存类，对于常用的公用数据缓存。
 * 对于与数据库交互的查询缓存，请用mybatis提供的二级缓存。
 * 
 * @author huchao
 *
 */
@Component()
@Scope("singleton")
public class CacheOperation {
	
	private Logger  log = LoggerFactory.getLogger(CacheOperation.class);
	
	@Autowired
	private  EhCacheCacheManager  ehCacheCacheManager;
	
	/*private  CacheOperation (){};
	private  static CacheOperation  cacheOperation= new CacheOperation();
	
	public static CacheOperation newInstanced(){
		   return cacheOperation;
	}*/
	/**
	 * 将值以key-value的形式添加到缓存中
	 * 假如存在key,以新的值替换老的值
	 * @param cache --- 配置的缓存策略
	 * @param key   --- 缓存的键
	 * @param value --- 缓存的值
	 * @throws SyException 
	 */
	public void add(String cache,String key ,Object value) throws SyException{
		Cache caches = ehCacheCacheManager.getCache(cache);
		if (caches==null) 
			throw new SyException("not exsit this "+caches+" cache....");
		caches.put(key, value);
		log.info("add cache:"+cache+"   key:"+key+" value:"+value.toString()+" ....");
	}
	
	/**
	 * 将值以key-value的形式添加到DaysCACHE的缓存中
	 * 假如存在key,以新的值替换老的值
	 * @param key   --- 缓存的键
	 * @param value --- 缓存的值
	 * @throws SyException 
	 */
	public void putIntoDaysCache(String key ,Object value) throws SyException{
		add(Constant.DAYSCACHE,key,value);
	}
	
	/**
	 * 将值以key-value的形式添加到FOREVERCACHE的缓存中
	 * 假如存在key,以新的值替换老的值
	 * @param key   --- 缓存的键
	 * @param value --- 缓存的值
	 * @throws SyException 
	 */
	public void putIntoForeverCache(String key ,Object value) throws SyException{
		add(Constant.FOREVERCACHE,key,value);
	}
	
	/**
	 * 将值以key-value的形式添加到缓存中
	 * 假如存在key,则不保存
	 * @param cache --- 配置的缓存策略
	 * @param key   --- 缓存的键
	 * @param value --- 缓存的值
	 * @throws SyException 
	 */
	public void addIfAbsent(String cache,String key ,Object value) throws SyException{
		Cache caches = ehCacheCacheManager.getCache(cache);
		if (caches==null) 
			throw new SyException("not exsit this "+caches+" cache....");
		caches.putIfAbsent(key, value);
		log.info("addIfAbsent  cache:"+cache+"   key:"+key+" value:"+value.toString()+" ....");
	}
	
	/**
	 * 将值以key-value的形式添加到FOREVERCACHE的缓存中
	 * 假如存在key,则不保存
	 * @param key   --- 缓存的键
	 * @param value --- 缓存的值
	 * @throws SyException 
	 */
	public void putIfAbsentIntoForeverCache(String key ,Object value) throws SyException{
		addIfAbsent(Constant.FOREVERCACHE,key,value);
	}
	/**
	 * 将值以key-value的形式添加到DaysCACHE的缓存中
	 * 假如存在key,则不保存
	 * @param key   --- 缓存的键
	 * @param value --- 缓存的值
	 * @throws SyException 
	 */
	public void putIfAbsentIntoDaysCache(String key ,Object value) throws SyException{
		addIfAbsent(Constant.DAYSCACHE,key,value);
	}
	/**
	 * 获取当前缓存策略中key的值
	 * @param cache---当前缓存策略
	 * @param key---缓存的键
	 * @return
	 */
	public Object get(String cache,String key){
		Cache caches = ehCacheCacheManager.getCache(cache);
		if (caches==null) 
			return null;
		ValueWrapper vm= caches.get(key);
		Object value = vm.get();
		log.info("get  cache:"+cache+"   key:"+key+" value:"+value.toString()+" ....");
		return value;
	};
	
	/**
	 * 获取DaysCACHE的缓存策略中key的值
	 * @param cache---当前缓存策略
	 * @param key---缓存的键
	 * @return
	 */
	public Object getFromDaysCache(String key){
		return get(Constant.DAYSCACHE,key);
	};
    
	/**
	 * 获取DaysCACHE的缓存策略中key的值
	 * @param cache---当前缓存策略
	 * @param key---缓存的键
	 * @return
	 */
	public Object getFromForeverCache(String key){
		return get(Constant.FOREVERCACHE,key);
	};
	/**
	 * 获取当前缓存策略中key对应的类型的值
	 * @param cache---当前缓存策略
	 * @param key--- 缓存的键
	 * @param cl---返回值类型
	 * @return
	 */
    public <T> T  get(String cache,String key,Class<T> cl){
    	Cache caches = ehCacheCacheManager.getCache(cache);
    	if (caches==null) return null;
    	T value = caches.get(key,cl);
    	log.info("get  cache:"+cache+"   key:"+key+" value:"+value.toString()+" ....");
		return  value;
    }
    /**
	 * 获取DaysCACHE缓存策略中key对应的类型的值
	 * @param key--- 缓存的键
	 * @param cl---返回值类型
	 * @return
	 */
    public <T> T  getFromDaysCache(String key,Class<T> cl){
		return  get(Constant.DAYSCACHE,key, cl);
    }
    
    /**
   	 * 获取FOREVERCACHE缓存策略中key对应的类型的值
   	 * @param key--- 缓存的键
   	 * @param cl---返回值类型
   	 * @return
   	 */
    public <T> T  getFromForeverCache(String key,Class<T> cl){
	    return  get(Constant.FOREVERCACHE,key, cl);
    }
    
    /**
     * 清除对应缓存策略对应的key的值。
     * @param cache
     * @param key
     */
    public  void  remove(String cache,String key){
    	Cache caches = ehCacheCacheManager.getCache(cache);
    	if (caches!=null) 
    	     caches.evict(key);
    	log.info("remove  cache:"+cache+"   key:"+key+"  ....");
    }
    
    /**
     * 清除DaysCACHE缓存策略对应的key的值。
     * @param cache
     * @param key
     */
    public  void  removeFromDaysCache(String key){
    	remove(Constant.DAYSCACHE,key);
    }
    /**
     * 清除FOREVERCACHE缓存策略对应的key的值。
     * @param cache
     * @param key
     */
    public  void  removeFromForeverCache(String key){
    	remove(Constant.FOREVERCACHE,key);
    }
    /**
     * 清空当前缓存策略中的所有值
     * @param cache
     * @param key
     */
    public void removeall(String cache){
    	Cache caches = ehCacheCacheManager.getCache(cache);
    	if (caches!=null) 
    	     caches.clear();
    	log.info("removeall  cache:"+cache+" ....");
    };
    
    /**
     * 清空FOREVERCACHE缓存策略中的所有值
     * @param cache
     * @param key
     */
    public void removeallFromForeverCache(){
    	removeall(Constant.FOREVERCACHE);
    };
    
    /**
     * 清空DaysCACHE缓存策略中的所有值
     * @param cache
     * @param key
     */
    public void removeallFromDaysCache(){
    	removeall(Constant.DAYSCACHE);
    };
    
    /**
     * 清空所有的缓存的类容
     */
    public void clear(){
    	CacheManager cacheManager= ehCacheCacheManager.getCacheManager();
    	if(cacheManager!=null)
    	    cacheManager.clearAll();
    	log.info("clear  ....");
    }
    
    /**
     * 当前缓存策略大小
     * @param cache
     * @return
     */
    public int size (String cache){
    	net.sf.ehcache.Cache ca= ehCacheCacheManager.getCacheManager().getCache(cache);
    	if(ca==null){
    		log.error(cache+" cache not exist ......");
    		return 0;
    	}
    	return ca.getSize();
    }
    
    /**
     * DaysCACHE缓存策略大小
     * @param cache
     * @return
     */
    public int sizeOfDaysCache (){
    	return size(Constant.DAYSCACHE);
    }
    /**
     * FOREVERCACHE缓存策略大小
     * @param cache
     * @return
     */
    public int sizeOfForeverCache (){
    	return size(Constant.FOREVERCACHE);
    }
}
