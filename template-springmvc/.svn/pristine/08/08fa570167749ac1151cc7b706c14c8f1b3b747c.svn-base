package com.kanq.train.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 *	Spring Cache抽象详解: http://jinnianshilongnian.iteye.com/blog/2001040 <br> 
 * 
 * <p>包装Spring cache抽象
 */
public class SpringCacheManagerWrapper implements CacheManager{

	private org.springframework.cache.CacheManager springCacheManager;

	/**
	 * 设置spring cache manager
	 *
	 * @param cacheManager
	 */
	public void setCacheManager(
			org.springframework.cache.CacheManager cacheManager) {
		this.springCacheManager = cacheManager;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		org.springframework.cache.Cache springCache = springCacheManager
				.getCache(name);
		return new SpringCacheWrapper(springCache);
	}


}
