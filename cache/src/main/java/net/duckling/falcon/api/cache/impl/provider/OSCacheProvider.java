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
package net.duckling.falcon.api.cache.impl.provider;

import java.util.Properties;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

public class OSCacheProvider implements ICacheProvider {
    
    private GeneralCacheAdministrator osCacheManager;

    private static final String KEY_CAPACITY = "cache.capacity";
    private static final String KEY_ALGORITHEM = "cache.algorithm";
    private static final String KEY_UNLIMITED_DISK = "cache.unlimited_disk";

    private static final int VALUE_CAPACITY = 100000;
    private static final String VALUE_ALGORITHEM = "com.opensymphony.oscache.base.algorithm.LRUCache";
    private static final boolean VALUE_UNLIMITED_DISK = false;

    public OSCacheProvider() {
        Properties prop = new Properties();
        prop.put(KEY_CAPACITY, VALUE_CAPACITY);
        prop.put(KEY_ALGORITHEM, VALUE_ALGORITHEM);
        prop.put(KEY_UNLIMITED_DISK, VALUE_UNLIMITED_DISK);
        osCacheManager = new GeneralCacheAdministrator(prop);
    }

    public Object get(String key) {
        try {
            return osCacheManager.getFromCache(key);
        } catch (NeedsRefreshException e) {
            osCacheManager.cancelUpdate(key);
            return null;
        }
    }

    public void set(String key, Object value) {
        osCacheManager.putInCache(key, value);
    }

    public void remove(String key) {
        osCacheManager.removeEntry(key);
    }

    public void disconnect() {
        osCacheManager.destroy();
    }

    public String checkCacheState() {
        return ALL_WORK;
    }

	@Override
	public void set(String key, Object value, int expireInSeconds) {
		set(key, value);
	}
}