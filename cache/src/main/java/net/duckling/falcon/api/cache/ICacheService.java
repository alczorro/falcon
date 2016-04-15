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
package net.duckling.falcon.api.cache;

/**
 * @title: ICacheService.java
 * @package net.duckling.dhome.common.cache
 * @description: 缓存服务对外接口
 * @author clive
 * @date 2012-9-13 下午2:57:04
 */
public interface ICacheService {

    /**
     * @description 通过关键字获得可序列化对象
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * @description 将可序列化对象放入缓存中
     * @param key
     * @param value
     */
    void set(String key, Object value);
    /**
     * @description 将可序列化对象放入缓存中
     * @param key
     * @param value
     * @param expireInSeconds
     */
    void set(String key, Object value, int expireInSeconds);

    /**
     * @description 从缓存中删除该关键字对应的条目
     * @param key
     */
    void remove(String key);
    
    /**
     * @description 缓存初始化服务，建立连接
     */
    void doInit();
    
    /**
     * @description 缓存关闭服务，断开连接
     */
    void doDestroy();
    
}
