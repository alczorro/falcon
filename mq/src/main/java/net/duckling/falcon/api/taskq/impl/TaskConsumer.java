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

import java.util.List;

import net.duckling.common.util.CommonUtils;
import net.duckling.falcon.api.serialize.JSONMapper;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @title: TaskConsumer.java
 * @package net.duckling.falcon.api.taskq.impl
 * @description: 分布式任务队列的任务消费者,依赖于redis
 * @author clive
 * @date 2014-6-22 下午7:45:32
 */
public class TaskConsumer {

    private static final Logger LOG = Logger.getLogger(TaskQueueManager.class);

    private JedisPool pool;

    private int blockTimeout;

    private static final int DEFALUT_TIME_OUT = 0;

    /**
     * @param host
     *            redis服务主机地址
     * @param port
     *            redis服务端口
     */
    public TaskConsumer(String host, int port) {
        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMinIdle(100);
        conf.setMaxIdle(200);
        conf.setMaxTotal(200);
        pool = new JedisPool(conf, host, port,0, null, 0, "client_clive");
        this.blockTimeout = DEFALUT_TIME_OUT;
        LOG.info("Task consumer which use redis server init success.");
    }

    /**
     * @description 释放连接
     */
    public void close() {
        pool.destroy();
    }

    /**
     * @description 阻塞式任务消费者,默认情况下如果任务队列中没有任务将会永久阻塞下去,可以通过设置timeout参数来改变超时时间
     * @param qname
     *            队列名称
     * @param clazz
     *            任务类T的class对象
     * @param handler
     *            任务类T的处理接口
     */
    public <T> void consume(String qname, Class<T> clazz, IConsumerHandler<T> handler) {
        Jedis jdc = pool.getResource();
        T result = null;
        
        List<String> list = jdc.blpop(blockTimeout, qname);
        if (!CommonUtils.isNull(list)) {
            String str = list.get(1);
            result = JSONMapper.getBean(str, clazz);
            handler.handle(result);
        } else {
            LOG.info("Wait Timeout");
        }
        pool.returnResource(jdc);
    }
    
    public <T> void consumeReliable(String qname, String qbakname, Class<T> clazz, IConsumerHandler<T> handler){
        Jedis jdc = pool.getResource();
        T result = null;
        String str = jdc.brpoplpush(qname, qbakname, blockTimeout);
        result = JSONMapper.getBean(str, clazz);
        handler.handle(result);
        jdc.lrem(qbakname, 1, str);
        pool.returnResource(jdc);
    }
    
    public <T> void restartConsumer(String qbakname, Class<T> clazz, IConsumerHandler<T> handler){
        Jedis jdc = pool.getResource();
        T result = null;
        while(jdc.llen(qbakname) > 0){
            String str = jdc.lpop(qbakname);
            result = JSONMapper.getBean(str, clazz);
            handler.handle(result);
        }
        pool.returnResource(jdc);
    }

}
