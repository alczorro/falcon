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

/**
 * @title: ICacheProvider.java
 * @package net.duckling.falcon.api.cache.impl.provider
 * @description: Cache提供者接口
 * @author clive
 * @date 2012-9-13 下午2:59:58
 */
public interface ICacheProvider {

    /**
     * 获取缓存保存的Value
     * @param key Cache使用的Key
     * @return 如果这个Key对应的Cache存在则返回相应的值，否则返回null
     */
    Object get(String key) ;

    /**
     * 保存值到Cache中。
     * 
     * @param key
     *            该值的Key
     * @param value
     *            对应的取值
     */
    void set(String key, Object value) ;
    void set(String key, Object value, int expireInSeconds);

    /**
     * 清除某项缓存
     * 
     * @param key
     *            该项缓存的Key值
     */
    void remove(String key);

    /**
     * @description 
     */
    void disconnect();

    /**
     * @description 
     * @return
     */
    String checkCacheState();

    String ALL_DOWN = "ALL_DOWN";
    String ALL_WORK = "ALL_WORK";
    String PART_DOWN = "PART_DOWN";

}
