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

import net.duckling.falcon.api.mq.impl.DFMQBasePublisherClient;
import net.duckling.falcon.api.mq.impl.DFMQBaseSubscriberClient;

public class DFMQFactory {

    /**
     * @description 构造一个消息队列模式的发布器
     * @param username
     *            用户名
     * @param password
     *            密码          
     * @param host
     *            主机名
     * @param exchangeName
     *            路由名称
     * @param mode
     *            仅接受枚举类型TOPIC,DIRECT,FANOUT
     * @return 返回IDFPublisher对象
     */
    public static IDFPublisher buildPublisher(String username, String password, String host, String exchangeName,
            DFMQMode mode) {
        DFMQBasePublisherClient client = new DFMQBasePublisherClient(username, password, host);
        client.declareExchange(exchangeName, mode, true);
        return client;
    }

    /**
     * @description 构造一个消息队列订阅器
     * @param host
     *            主机名
     * @param exchangeName
     *            路由名称
     * @param queueName
     *            绑定队列名称
     * @return 返回IDFSubscriber对象
     */
    public static IDFSubscriber buildSubscriber(String username, String password, String host, String exchangeName,
            String queueName, DFMQMode mode) {
        DFMQBaseSubscriberClient client = new DFMQBaseSubscriberClient(username, password, host);
        client.declareExchange(exchangeName, mode, true);
        client.declareQueue(queueName);
        return client;
    }

}
