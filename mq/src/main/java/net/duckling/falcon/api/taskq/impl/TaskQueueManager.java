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

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TaskQueueManager {

    private static final Logger LOG = Logger.getLogger(TaskQueueManager.class);

    private JedisPool pool;

    public TaskQueueManager(String host, int port) {
        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMinIdle(10);
        conf.setMaxIdle(20);
        this.pool = new JedisPool(conf, host, port);
        LOG.info("Task queue manager has been initialized successfully.");
    }

    public long length(String qname) {
        Jedis jdc = pool.getResource();
        long reuslt = jdc.llen(qname);
        pool.returnResource(jdc);
        return reuslt;
    }

    public boolean isQueueExist(String qname) {
        Jedis jdc = pool.getResource();
        boolean reuslt = jdc.exists(qname);
        pool.returnResource(jdc);
        return reuslt;
    }

    public void dropQueue(String qname) {
        Jedis jdc = pool.getResource();
        jdc.del(qname);
        pool.returnResource(jdc);
    }

    public void close() {
        pool.destroy();
    }
    
    public void initLocalConnect(){
        String localHost =  pool.getResource().getClient().getSocket().getLocalAddress().getHostAddress();
        String localPort = String.valueOf(pool.getResource().getClient().getSocket().getLocalPort());
        Jedis jdc = pool.getResource();
        String clientList = jdc.clientList();
        
        System.out.println(clientList);
        pool.returnResource(jdc);
        System.out.println("Local client host:" + localHost+" and port:" + localPort);
    }
    
    public static void main(String[] args) {
        TaskQueueManager tqm = new TaskQueueManager("10.10.2.7",6379);
        tqm.initLocalConnect();
    }

}
