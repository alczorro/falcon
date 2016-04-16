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
package net.duckling.falcon.api.mq.impl;

import java.io.IOException;

import net.duckling.falcon.api.mq.DFMQMode;
import net.duckling.falcon.api.mq.IDFPublisher;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * @title: DFMQBaseClient.java
 * @package net.duckling.falcon.api.mq.impl
 * @description: 消息队列客户端基础类,为消除冗余抽象出来的
 * @author clive
 * @date 2013-7-31 上午10:02:30
 */
public class DFMQBasePublisherClient implements IDFPublisher {

    protected String host;
    protected String mode;

    protected String exchangeName;
    protected String queueName;
    protected String routingKey;

    protected Connection connection;
    protected Channel channel = null;
    private String password;
    private String username;

    private static Logger LOG = Logger.getLogger(DFMQBasePublisherClient.class);

    @Override
    public void send(Object obj, String routingKey) {
        if(StringUtils.isEmpty(routingKey)){
            publishMessage(obj, "");
        }else{
            publishMessage(obj, routingKey);
        }
    }

    public DFMQBasePublisherClient(String username, String password, String host) {
        this.host = host;
        this.password = password;
        this.username = username;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPassword(password);
        factory.setUsername(username);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void declareExchange(String exchangeName, DFMQMode mode, boolean isDurable) {
        try {
            this.mode = mode.toString();
            this.exchangeName = exchangeName;
            channel.exchangeDeclare(exchangeName, mode.toString(), isDurable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void publishMessage(Object obj, String routingKey) {
        try {
            if (obj != null) {
                channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN,
                        DFMessageSerializer.serialize(obj));
            } else {
                LOG.info("The object to send as a message should not be null.");
            }
        } catch (IOException e) {
            LOG.error("IO exception for send message.", e);
        }
    }

    public void close() {
        try {
            this.channel.close();
            this.connection.close();
        } catch (IOException e) {
            LOG.error("Close connection error.", e);
        }
    }

    public String getParams() {
        StringBuilder sb = new StringBuilder();
        sb.append("host:" + host + "\n");
        sb.append("mode:" + mode + "\n");
        sb.append("exchange:" + exchangeName + "\n");
        sb.append("queue:" + queueName + "\n");
        sb.append("user:" + username + "\n");
        sb.append("password:" + password + "\n");
        return sb.toString();
    }

}
