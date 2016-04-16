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
 * @title: IDFSubscriber.java
 * @package net.duckling.falcon.api.mq
 * @description: 订阅器接口
 * @author clive
 * @date 2013-7-31 上午10:16:47
 */
public interface IDFSubscriber {
    /**
     * @description 接收从消息总线中传输的接口
     * @param handler 消息处理器
     */
    void receive() throws NotFoundHandlerException;
    
    void registHandler(String routingKey,IDFMessageHandler handler);
    
    /**
     * @description 关闭订阅器与消息总线的连接
     */
    void close();
}
