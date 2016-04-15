/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package net.duckling.falcon.api.cache.impl;

import java.io.IOException;

import net.duckling.falcon.api.cache.ICacheService;
import net.duckling.falcon.api.cache.impl.provider.ICacheProvider;
import net.duckling.falcon.api.cache.impl.provider.MemcachedProvider;
import net.duckling.falcon.api.cache.impl.provider.OSCacheProvider;
import net.spy.memcached.OperationTimeoutException;

import org.apache.log4j.Logger;

/**
 * @title: MemcachedService.java
 * @package net.duckling.dhome.common.cache
 * @description: 缓存服务
 * @author clive
 * @date 2012-9-19 下午1:45:58
 */
public class MemcachedCacheService implements ICacheService {

	private static final Logger LOG = Logger.getLogger(MemcachedCacheService.class);

	private ICacheProvider current;
	private ICacheProvider memcache;
	private ICacheProvider oscache;

	private String memcachedURL;

	public String getMemcachedURL() {
		return memcachedURL;
	}

	public void setMemcachedURL(String memcachedURL) {
		this.memcachedURL = memcachedURL;
	}

	public void doDestroy() {
		current.disconnect();
	}

	public void doInit() {
		initMemcacheProvider(memcachedURL);
		oscache = new OSCacheProvider();
		if (memcache == null) {
			switchCacheMode();
		} else {
			current = memcache;
		}
	}

	private void initMemcacheProvider(String url) {
		// turn it off when ${umt.memcachedURL} is "null"
		if ("null".equalsIgnoreCase(url)) {
			memcache = null;
			LOG.info("memcachedURL is set to 'null', no memcache.");
		}
		else {
			try {
				memcache = new MemcachedProvider(url);
			} catch (IOException e) {
				LOG.error("Can not creat a memcached connection.", e);
			}
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void switchCacheMode() {
		current = oscache;
		if (memcache != null) {
			String state = memcache.checkCacheState();
			if (ICacheProvider.ALL_DOWN.equals(state)) {
				memcache.disconnect();
			} else {
				current = memcache;
			}
		}
	}

	@Override
	public Object get(String key) {
		Object obj = null;
		try {
			obj = current.get(key);
		} catch (CacheServiceBrokenException ex) {
			handleException(ex);
			obj = current.get(key);
		} catch (OperationTimeoutException ex) {
			LOG.error("Cache service time out.", ex);
			obj = null;
		}
		return obj;
	}

	@Override
	public void set(String key, Object value) {
		try {
			current.set(key, value);
		} catch (CacheServiceBrokenException ex) {
			handleException(ex);
			current.set(key, value);
		} catch (OperationTimeoutException ex) {
			LOG.error("Cache service time out.", ex);
		}
	}

	@Override
	public void remove(String key) {
		try {
			current.remove(key);
		} catch (CacheServiceBrokenException ex) {
			handleException(ex);
			current.remove(key);
		} catch (OperationTimeoutException ex) {
			LOG.error("Cache service time out.", ex);
		}
	}

	private void handleException(CacheServiceBrokenException ex) {
		LOG.error("Handle for cacheServiceDown exception", ex);
		switchCacheMode();
	}

	@Override
	public void set(String key, Object value, int expireInSeconds) {

	}

}
