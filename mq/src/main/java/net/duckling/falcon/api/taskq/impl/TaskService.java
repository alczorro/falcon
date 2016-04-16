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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import net.duckling.falcon.api.serialize.JSONMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * @title: TaskService.java
 * @package net.duckling.falcon.api.taskq.impl
 * @description: 基于redis的任务队列
 * @author clive
 * @date 2014-9-2 上午10:35:41
 */
public class TaskService {

    private static final Logger LOG = Logger.getLogger(TaskService.class);
    
    /*
     * q:${qname}:gid -> int q:${qname}:bean -> { id -> task_log}
     * q:${qname}:user:${user}:date -> { date1,date2,date3...}
     * q:${qname}:user:${user}:date:${date}:id -> {id1,id2,id3,...}
     * q:${qname}:user:${user}:status:${status}:id -> {id1,id2,id3,...}
     * q:${qname}:group:${groupid}:id -> {id1,id2,id3,...}
     */

    private JedisPool pool;

    public TaskService(String host, int port) {
        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMinIdle(100);
        conf.setMaxIdle(200);
        conf.setMaxTotal(200);
        pool = new JedisPool(conf, host, port);
    }

    /**
     * @description 获得某个队列的最大自增id号
     * @param qname 队列名称
     * @return 长整形id
     */
    public long getMaxLogId(String qname) {
        Jedis jdc = pool.getResource();
        long id = jdc.incr(String.format("q:%s:gid", qname));
        pool.returnResource(jdc);
        return id;
    }

    public TaskLog buildTaskLog(long id, long groupId, String username, String content) {
        TaskLog tl = new TaskLog();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tl.setProducedTime(new Date());
        tl.setId(id);
        tl.setUsername(username);
        tl.setDateTag(sdf.format(tl.getProducedTime()));
        tl.setContent(content);
        tl.setGroupId(groupId);
        tl.setStatus(TaskStatus.WAITING.toString());
        return tl;
    }

    /**
     * @description 将TaskLog存储到表中,同时还会建立对于此任务的查询索引
     * @param qname 任务队列名称
     * @param log 任务对象
     */
    public void appendBean(String qname, TaskLog log) {
        Jedis jdc = pool.getResource();
        String idStr = log.getId() + "";
        jdc.hset(getLogBeanKey(qname), idStr, JSONMapper.getJSONString(log));
        jdc.sadd(getUserDateSetKey(qname, log), log.getDateTag()); // user ->
                                                                   // date 的索引
        jdc.sadd(getUserDateIdSetKey(qname, log), idStr); // user-date -> id 的索引
        jdc.sadd(getUserStatusKey(qname, log), idStr); // user-status -> id 的索引
        jdc.sadd(getGroupIdSetKey(qname, log.getGroupId()), idStr); // groupid
                                                                    // -> id 的索引
        pool.returnResource(jdc);
    }

    /**
     * @description 更新任务的状态
     * @param qname 队列名称
     * @param id 任务id号
     * @param status 要更新的状态
     * @param message 错误信息
     */
    public void updateBeanStatus(String qname, long id, TaskStatus status, String message) {
        Jedis jdc = pool.getResource();
        String idStr = id + "";
        TaskLog tmpLog = this.queryById(qname, id);
        if (TaskStatus.PROCESSING == status) {
            tmpLog.setTakenTime(new Date());
        } else if (TaskStatus.FAILED == status || TaskStatus.SUCCESS == status) {
            tmpLog.setConsumedTime(new Date());
        }
        tmpLog.setRspnMsg(message);
        jdc.srem(getUserStatusKey(qname, tmpLog.getUsername(), tmpLog.getStatus()), idStr);// 删除status索引
        jdc.sadd(getUserStatusKey(qname, tmpLog.getUsername(), status.toString()), idStr); // 更新status索引
        tmpLog.setStatus(status.toString());
        jdc.hset(getLogBeanKey(qname), idStr, JSONMapper.getJSONString(tmpLog));
        pool.returnResource(jdc);
    }

    public List<TaskLog> query(String qname, String username, Date beginDate, Date endDate, String status) {
        Jedis jdc = pool.getResource();
        Set<String> dateTags = jdc.smembers(getUserDateSetKey(qname, username)); // 过滤日期
        Set<String> filterDates = filterDate(dateTags, beginDate, endDate);
        String[] keys = new String[filterDates.size()];
        filterDates.toArray(keys);

        Set<String> matchDateIds = jdc.sunion(keys); // 符合日期条件的id集合
        Set<String> matchStatusIds = jdc.sunion(getUserStatusKey(qname, username, status)); // 符合状态条件的id集合
        Set<String> idSet = intersect(matchDateIds, matchStatusIds); // 取交集
        List<TaskLog> logResults = findTaskLogList(qname, idSet, jdc);
        pool.returnResource(jdc);
        return logResults;
    }

    /**
     * @description 按照id号查询任务
     * @param qname 队列名称
     * @param logId 任务id号
     * @return 任务对象
     */
    public TaskLog queryById(String qname, long logId) {
        Jedis jdc = pool.getResource();
        String idStr = logId + "";
        String beanStr = jdc.hget(getLogBeanKey(qname), idStr);
        TaskLog oldBean = null;
        if (beanStr != null && !beanStr.isEmpty()) {
            oldBean = JSONMapper.getBean(beanStr, TaskLog.class);
        }
        pool.returnResource(jdc);
        return oldBean;
    }

    private List<TaskLog> findTaskLogList(String qname, Set<String> idSet, Jedis jdc) {
        String[] idArray = new String[idSet.size()];
        idSet.toArray(idArray);
        List<TaskLog> logResults = new ArrayList<TaskLog>();
        try {
            List<String> list = jdc.hmget(getLogBeanKey(qname), idArray);
            for (String s : list) {
                logResults.add(JSONMapper.getBean(s, TaskLog.class));
            }
        } catch (JedisDataException e) {
            LOG.warn("hmget failed: "+getLogBeanKey(qname)+" "+idSet);
        }
        return logResults;
    }

    /**
     * @description 按组信息查询任务队列
     * @param qname 队列名称
     * @param groupId 组ID号
     * @return 包含多个任务的List
     */
    public List<TaskLog> queryByGroupId(String qname, long groupId) {
        Jedis jdc = pool.getResource();
        String key = this.getGroupIdSetKey(qname, groupId);
        Set<String> idSet = jdc.smembers(key);
        List<TaskLog> logResults = findTaskLogList(qname, idSet, jdc);
        pool.returnResource(jdc);
        return logResults;
    }

    private Set<String> intersect(Set<String> matchDateIds, Set<String> matchStatusIds) {
        Set<String> idSet = new HashSet<String>();
        for (String id : matchDateIds) {
            if (matchStatusIds.contains(id)) {
                idSet.add(id);
            }
        }
        return idSet;
    }

    private Set<String> filterDate(Set<String> dateTags, Date beginDate, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Set<String> dates = new HashSet<String>();
        for (String dtag : dateTags) {
            try {
                Date tmp = sdf.parse(dtag);
                if (tmp.after(beginDate) && tmp.before(endDate)) {
                    dates.add(dtag);
                }
            } catch (ParseException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return dates;
    }

    private String getUserStatusKey(String qname, String username, String status) {
        return String.format("q:%s:user:%s:status:%s:id", qname, username, status);
    }

    private String getUserStatusKey(String qname, TaskLog log) {
        return String.format("q:%s:user:%s:status:%s:id", qname, log.getUsername(), log.getStatus());
    }

    private String getLogBeanKey(String qname) {
        return String.format("q:%s:tlogs", qname);
    }

    private String getUserDateSetKey(String qname, String username) {
        return String.format("q:%s:user:%s:date", qname, username);
    }

    private String getUserDateSetKey(String qname, TaskLog log) {
        return String.format("q:%s:user:%s:date", qname, log.getUsername());
    }

    private String getUserDateIdSetKey(String qname, TaskLog log) {
        return String.format("q:%s:user:%s:date:%s:id", qname, log.getUsername(), log.getDateTag());
    }

    private String getGroupIdSetKey(String qname, long groupId) {
        return String.format("q:%s:group:%d:id", qname, groupId);
    }

    public void close() {
        pool.destroy();
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

    /*
     * q:${qname}:exist-task:key -> {key1,key2,key3}
     */
    public boolean isDuplicateTask(String qname, String taskKey) {
        Jedis jdc = pool.getResource();
        boolean result = jdc.sismember(getDedepulicatTaskIndexKey(qname), taskKey);
        pool.returnResource(jdc);
        return result;
    }

    private String getDedepulicatTaskIndexKey(String qname) {
        return String.format("q:%s:exist-task:key", qname);
    }

    /**
     * @description 将某个任务加入到去重队列中
     * @param qname 队列名称
     * @param taskKey 任务唯一ID
     * @return 操作是否成功
     */
    public boolean appendDuplicateTask(String qname, String taskKey) {
        Jedis jdc = pool.getResource();
        Long r = jdc.sadd(getDedepulicatTaskIndexKey(qname), taskKey);
        pool.returnResource(jdc);
        return r > 0;
    }

    /**
     * @description 将任务从去重队列中去除
     * @param qname 队列名称
     * @param taskKey 任务唯一ID
     * @return 操作是否成功
     */
    public boolean removeDuplicateTask(String qname, String taskKey) {
        Jedis jdc = pool.getResource();
        Long r = jdc.srem(getDedepulicatTaskIndexKey(qname), taskKey);
        pool.returnResource(jdc);
        return r > 0;
    }
    
    
    /**
     * @description 回滚任务
     * @param qname
     * @param taskKey
     * @return
     */
    public boolean rollbackTask(String qname, String taskKey){
        return false;
    }

}
