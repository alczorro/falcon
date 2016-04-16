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
package net.duckling.falcon.api.mq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class DFMQManager {

    private Connection connection;
    private Channel channel = null;

    public DFMQManager(String userName,String password,String host) {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setUsername(userName);
            factory.setPassword(password);
            factory.setHost(host);
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearQueueData(String queueName) {
        try {
            channel.queuePurge(queueName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteQueue(String queueName) {
        try {
            channel.queueDelete(queueName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteExchange(String exchangeName) {
        try {
            channel.exchangeDelete(exchangeName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void close(){
        try {
            this.channel.close();
            this.connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
