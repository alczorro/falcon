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
package net.duckling.falcon.api.test.manager;
/**
 * @title: ISendMessage.java
 * @package net.duckling.falcon.api.test.manager.tomcatmanage;
 * @description: 用来向url发送数据msg
 * @author xuxin
 * @date 2012-10-25 下午2:57:04
 */
public interface ISendMessage {
	public static final String openCmd = "open";
	public static final String closeCmd = "close";

	 /**
	 * @description 向url发送数据msg
	 * @param url
	 * @param msg 发送的数据
	 * @return 发送成功返回true，失败返回false
	 */
	public boolean sendMessage(String url, String msg);
}
