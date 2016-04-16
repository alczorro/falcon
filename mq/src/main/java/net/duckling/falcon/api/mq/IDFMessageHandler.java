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
 * @title: IDFMessageHandler.java
 * @package net.duckling.falcon.api.mq
 * @description: 订阅者消息处理接口
 * @author clive
 * @date 2013-7-31 上午10:13:01
 */
public interface IDFMessageHandler {
	/**
	 * @description 解析并处理消息的接口,需要客户端自主实现
	 * @param obj 已反序列化好的对象,可直接强制类型转换 
	 */
	void handle(Object obj,String routingKey);
}
