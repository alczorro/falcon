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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.duckling.falcon.api.mq.CreateLog;
import net.duckling.falcon.api.mq.DFMQFactory;
import net.duckling.falcon.api.mq.DFMQMode;
import net.duckling.falcon.api.mq.IDFPublisher;
import net.duckling.falcon.api.mq.IDFSubscriber;
import net.duckling.falcon.api.mq.MessageBody;
import net.duckling.falcon.api.mq.RecieverRoutingThread;
import net.duckling.falcon.api.mq.RecieverThread;

import org.junit.Before;
import org.junit.Test;

public class DFMQTest {

    private String host = "10.10.2.6";
    private String username = "guest";
    private String password = "guest";

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testFanoutMode() {
        String exchange = "fanout-test-exchange";
        IDFPublisher publisher = DFMQFactory.buildPublisher(username, password, host, exchange, DFMQMode.FANOUT);
        MessageBody[] body = sendMessage(publisher);
        testFanoutReceieve(exchange, "demo2", DFMQMode.FANOUT, body);
    }

    @Test
    public void testDirectMode() {
        String exchange = "direct-test-exchange";
        String routingKey = "user";
        IDFPublisher publisher = DFMQFactory.buildPublisher(username, password, host, exchange, DFMQMode.DIRECT);
        MessageBody[] body = sendMessage(publisher, routingKey);
        testRoutingReceive(exchange, "demo1", routingKey, DFMQMode.DIRECT, body);
    }

    @Test
    public void testTopicMode() {
        String exchange = "topic-test-exchange";
        String routingKey = "user.service";
        IDFPublisher publisher = DFMQFactory.buildPublisher(username, password, host, exchange, DFMQMode.TOPIC);
        MessageBody[] body = sendMessage(publisher, routingKey);
        testRoutingReceive(exchange, "demo3", "user.*", DFMQMode.TOPIC, body);
        testRoutingReceive(exchange, "demo4", "#.service", DFMQMode.TOPIC, body);
    }

    private void testRoutingReceive(String exchange, String queueNamePrefix, String routingKey, DFMQMode mode,
            MessageBody[] body) {
        RecieverRoutingThread[] array = new RecieverRoutingThread[3];
        for (int i = 0; i < array.length; i++) {
            String queueName = queueNamePrefix + "_" + mode + "_" + i;
            IDFSubscriber s = DFMQFactory.buildSubscriber(username, password, host, exchange, queueName, mode);
            System.out.println(queueName);
            array[i] = new RecieverRoutingThread(s, routingKey, body);
            array[i].start();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < array.length; i++) {
            array[i].getReciever().close();
        }
    }

    private void testFanoutReceieve(String exchange, String queueNamePrefix, DFMQMode mode, MessageBody[] body) {
        RecieverThread[] array = new RecieverThread[3];
        for (int i = 0; i < array.length; i++) {
            String queueName = queueNamePrefix + "_" + mode + "_" + i;
            System.out.println(queueName);
            IDFSubscriber s = DFMQFactory.buildSubscriber(username, password, host, exchange, queueName, mode);
            array[i] = new RecieverThread(s, body);
            array[i].start();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < array.length; i++) {
            array[i].getReciever().close();
        }
    }

    private MessageBody[] sendMessage(IDFPublisher publisher) {
        MessageBody[] body = new MessageBody[5];
        for (int i = 0; i < body.length; i++) {
            body[i] = getMessageBody();
            publisher.send(body[i], null);
        }
        publisher.close();
        return body;
    }

    private MessageBody[] sendMessage(IDFPublisher publisher, String routingKey) {
        MessageBody[] body = new MessageBody[5];
        for (int i = 0; i < body.length; i++) {
            body[i] = getMessageBody();
            publisher.send(body[i], routingKey);
        }
        publisher.close();
        return body;
    }

    private MessageBody getMessageBody() {
        Random rand = new Random();
        MessageBody b = new MessageBody();
        List<CreateLog> list = new ArrayList<CreateLog>();
        b.setId(rand.nextInt(100000));
        list.add(new CreateLog(2, "CC", 1, 2));
        list.add(new CreateLog(3, "DD", 1, 3));
        b.setList(list);
        Map<String, CreateLog> map = new HashMap<String, CreateLog>();
        map.put("hello", new CreateLog(4, "EE", 2, 3));
        b.setMaps(map);
        b.setType("create_org");
        b.setTimestamp(new Date());
        b.setBody(new CreateLog(1, "CNIC", 2, 4, new Date()));
        System.out.println(b.getId() + "," + b.getTimestamp());
        return b;
    }
}
