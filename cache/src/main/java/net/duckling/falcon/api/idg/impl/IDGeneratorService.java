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
package net.duckling.falcon.api.idg.impl;

import java.util.HashMap;
import java.util.Map;

import net.duckling.falcon.api.idg.IIDGeneratorService;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class IDGeneratorService implements IIDGeneratorService {

    private static final Logger LOG = Logger.getLogger(IDGeneratorService.class);

    private JedisPool pool;

    public IDGeneratorService(String host, int port) {
        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMinIdle(10);
        conf.setMaxIdle(20);
        pool = new JedisPool(conf, host, port);
    }

    /*
     * ?要删除旧的对象吗？ 是不是有的话就不初始化了？ 何时需要重新初始化？ redis中的状态和mysql的状态不一致怎么办？ 如何同步这两边？
     */

    @Override
    public void init(String appName, String groupName, Map<Integer, Integer> idMap) {
        Jedis jdc = pool.getResource();
        String tableName = getMapName(appName, groupName);
        boolean existFlag = jdc.exists(tableName);
        if (existFlag) {
            LOG.info("Skip init map because the map object is exist in memory.");
        } else {
            Map<String, String> strMap = new HashMap<String, String>();
            for (Map.Entry<Integer, Integer> entry : idMap.entrySet()) {
                strMap.put(entry.getKey() + "", entry.getValue() + "");
            }
            jdc.hmset(tableName, strMap);
            LOG.info("Create group id map using name = [" +tableName + "]");
        }
        pool.returnResource(jdc);
    }

    public boolean isExist(String appName, String groupName) {
        Jedis jdc = pool.getResource();
        boolean exist = jdc.exists(getMapName(appName, groupName));
        pool.returnResource(jdc);
        return exist;
    }

    private String getMapName(String appName, String groupName) {
        return appName + "." + groupName;
    }

    @Override
    public int getNextID(String appName, String groupName, int groupId) {
        Jedis jdc = pool.getResource();
        long newId = jdc.hincrBy(getMapName(appName, groupName), groupId + "", 1);
        pool.returnResource(jdc);
        return (int) newId;
    }

    @Override
    public void removeAll(String appName,String groupName) {
        Jedis jdc = pool.getResource();
        jdc.del(getMapName(appName,groupName));
        pool.returnResource(jdc);
    }

    @Override
    public void close() {
        pool.destroy();
        LOG.info("ID Generator Service is closed.");
    }

    @Override
    public boolean delete(String appName, String groupName, int groupId) {
        Jedis jdc = pool.getResource();
        long result = jdc.hdel(getMapName(appName, groupName), groupId + "");
        pool.returnResource(jdc);
        return result == 1;
    }

    @Override
    public Map<Integer, Integer> getGroupMap(String appName, String groupName) {
        Jedis jdc = pool.getResource();
        Map<String, String> result = jdc.hgetAll(getMapName(appName,groupName));
        Map<Integer,Integer> intMap = new HashMap<Integer,Integer>();
        for(Map.Entry<String, String> en:result.entrySet() ){
            intMap.put(Integer.parseInt(en.getKey()), Integer.parseInt(en.getValue()));
        }
        pool.returnResource(jdc);
        return intMap;
    }

}
