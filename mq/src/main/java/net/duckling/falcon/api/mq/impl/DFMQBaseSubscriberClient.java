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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.falcon.api.mq.DFMQMode;
import net.duckling.falcon.api.mq.IDFMessageHandler;
import net.duckling.falcon.api.mq.IDFSubscriber;
import net.duckling.falcon.api.mq.NotFoundHandlerException;

import org.apache.log4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * @title: DFMQBaseClient.java
 * @package net.duckling.falcon.api.mq.impl
 * @description: 消息队列客户端基础类,为消除冗余抽象出来的
 * @author clive
 * @date 2013-7-31 上午10:02:30
 */
public class DFMQBaseSubscriberClient implements IDFSubscriber {

    protected String host;
    protected String mode;
    protected String exchangeName;
    protected String queueName;
    protected String routingKey;
    private String username;
    private String password;
    protected Connection connection;
    protected Channel channel = null;
    protected QueueingConsumer consumer;

    private Map<String, IDFMessageHandler> handlerMap = null;
    private SimpleStringMatch ssm = null;

    private static Logger LOG = Logger.getLogger(DFMQBaseSubscriberClient.class);

    public DFMQBaseSubscriberClient(String username, String password, String host) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.ssm = new SimpleStringMatch();
        this.handlerMap = new HashMap<String, IDFMessageHandler>();
        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setHost(host);
            factory.setUsername(username);
            factory.setPassword(password);
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } catch (ShutdownSignalException e) {
            LOG.error(e.getMessage(), e);
        } catch (ConsumerCancelledException e) {
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

    @Override
    public void receive() throws NotFoundHandlerException {
        this.receiveFromSubscriber();
    }

    public void declareQueue(String queueName) {
        try {
            this.queueName = queueName;
            channel.queueDeclare(queueName, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            this.channel.close();
            this.connection.close();
        } catch (IOException e) {
            LOG.error("Close connection has an error.", e);
        }
    }

    protected void receiveFromSubscriber() throws NotFoundHandlerException {
        try {
            channel.basicQos(1);
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, false, consumer);
            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String routingKey = delivery.getEnvelope().getRoutingKey();
                IDFMessageHandler handler = getMatchedHandler(routingKey);
                Object message = DFMessageSerializer.deserialize(delivery.getBody());
                try {
                    if (handler != null) {
                        handler.handle(message, routingKey);
                    } else {
                        throw new NotFoundHandlerException("Not found handler for [" + routingKey
                                + "], please bind a new handler for this keyword.");
                    }
                } catch (RuntimeException e) {
                    LOG.error(e.getMessage(), e);
                }
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        } catch (IOException e) {
            LOG.error("Recieve message has an error.", e);
        } catch (ShutdownSignalException e) {
            LOG.info("Message reciever accept a shutdown singal.", e);
        } catch (ConsumerCancelledException e) {
            LOG.error("Consumer canceling has an error.", e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    protected IDFMessageHandler getMatchedHandler(String routingKey) {
        if (DFMQMode.FANOUT.toString().equals(this.mode)) {
            return handlerMap.get("*");
        }
        String matchedReg = ssm.routingMatch(regMap, routingKey);
        return handlerMap.get(matchedReg);
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

    private List<String> arrayList = new ArrayList<String>();
    private Map<String, String> regMap;

    @Override
    public void registHandler(String routingKey, IDFMessageHandler handler) {
        try {
            if (DFMQMode.FANOUT.toString().equals(this.mode)) {
                channel.queueBind(queueName, exchangeName, "");
                handlerMap.put("*", handler);
            } else {
                channel.queueBind(queueName, exchangeName, routingKey);
                handlerMap.put(routingKey, handler);
                arrayList.add(routingKey);
                regMap = ssm.getRegMap(arrayList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
