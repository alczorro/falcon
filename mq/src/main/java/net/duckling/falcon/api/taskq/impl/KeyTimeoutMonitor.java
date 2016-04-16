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
package net.duckling.falcon.api.taskq.impl;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @title: TimeoutQueueMonitor.java
 * @package net.duckling.falcon.api.taskq.impl
 * @description: 本类用于监控redis的缓存队列中的数据是否超时,如果超时则将其重新放入原始队列
 *               使用轮询式监控,轮询时间由pollingIntervals控制
 *               所有的监控的数据都通过regist方法进行注册,注册的数据也存放于redis表中
 *               
 * @author clive
 * @date 2014-9-2 下午7:32:35
 */
public class KeyTimeoutMonitor {
    
    private int pollingIntervals;
    
    public int getPollingIntervals() {
        return pollingIntervals;
    }

    public void setPollingIntervals(int pollingIntervals) {
        this.pollingIntervals = pollingIntervals;
    }

    private static final int DEFAULT_MAX_LIVE_TIME = 3600; //1hour
    private JedisPool pool;
    
    
    public KeyTimeoutMonitor(String host, int port){
        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMinIdle(100);
        conf.setMaxIdle(200);
        conf.setMaxTotal(200);
        pool = new JedisPool(conf, host, port,0, null, 0, "client_clive");
    }
    
    private int ttl; //最大存活时间
    private Set<String> monitorKeySet; //待监控的键值集合
    
    public boolean regist(String key, int ttl, String srcq, String dstq){
        // save into redis KeyLiveLog
        return false;
    }
    
    private Set<KeyLiveLog> getKeyLiveLog(){
        return null;
    }
    
    public void run(){
        while(true){
            try {
                Thread.sleep(pollingIntervals * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Jedis jdc = pool.getResource();
            Set<KeyLiveLog> klset = this.getKeyLiveLog();
            for(KeyLiveLog kl:klset){
                String key = kl.getKey();
                long attl = jdc.ttl(key);
                if(attl > kl.getTtl()){
                    this.rollback(key, kl.getSrcq(), kl.getDstq());
                }
            }
            pool.returnResource(jdc);
        }
        
    }
    
    private boolean rollback(String key, String srcQueue, String dstQueue){
        Jedis jdc = pool.getResource();
        if(dstQueue == null){
            jdc.lrem(srcQueue, 1, key);
        } else {
            jdc.lpop(key);
        }
        pool.returnResource(jdc);
        return false;
    }

}
