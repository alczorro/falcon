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

/**
 * @title: IDFPublisher.java
 * @package net.duckling.falcon.api.mq
 * @description: 消息发布者接口
 * @author clive
 * @date 2013-7-31 上午10:14:53
 */
public interface IDFPublisher {
    /**
     * @description 将任意类型的对象序列化后发送到消息队列
     * @param obj 需要发送的消息对象,必须是可序列化对象
     */
    void send(Object obj,String routingKey);
    
    /**
     * @description 关闭客户端与消息总线的连接
     */
    void close();
}
