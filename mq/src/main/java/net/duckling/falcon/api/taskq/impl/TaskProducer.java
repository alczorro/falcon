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

import net.duckling.falcon.api.serialize.JSONMapper;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @title: TaskProducer.java
 * @package net.duckling.falcon.api.taskq.impl
 * @description: 任务队列的生产者
 * @author clive
 * @date 2014-6-22 下午7:54:33
 */
public class TaskProducer {

    private JedisPool pool;

    private static final Logger LOG = Logger.getLogger(TaskProducer.class);

    /**
     * @param host
     *            redis主机名
     * @param port
     *            redis端口号
     */
    public TaskProducer(String host, int port) {
        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMinIdle(10);
        conf.setMaxIdle(20);
        pool = new JedisPool(conf, host, port);
        LOG.info("Task producer using redis has been inited success.");
    }

    /**
     * @description 释放连接
     */
    public void close() {
        pool.destroy();
    }

    /**
     * @description 任务队列的生产者
     * @param qname
     *            任务队列的名称
     * @param task
     *            任务实例
     */
    public <T> void produce(String qname, T task) {
        Jedis jdc = pool.getResource();
        if (task != null) {
            String taskStr = JSONMapper.getJSONString(task);
            jdc.lpush(qname, taskStr);
        }
        pool.returnResource(jdc);
    }

}
