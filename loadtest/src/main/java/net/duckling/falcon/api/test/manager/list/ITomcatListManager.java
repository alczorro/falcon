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
package net.duckling.falcon.api.test.manager.list;

import java.util.List;

/**
 * @title: IListManager.java
 * @package net.duckling.falcon.api.test.manager.list;
 * @description: 用来管理已注册的主机队列
 * @author xuxin
 * @date 2012-10-25 下午2:57:04
 */
public interface ITomcatListManager {
    /**
     * @description 添加一个新的url
     * @param url
     * @return 成功返回true，失败返回false
     */
	boolean addOpenUrl(String url);

    /**
     * @description 将url放入close队列，close队列保存了已经关闭tomcat主机的url
     * @param url
     * @return 成功返回true，失败返回false
     */
	boolean addToCloseList(String url);
	/**
     * @description 将url放入open队列，open队列保存了已经打开tomcat主机的url
     * @param url
     * @return 成功返回true，失败返回false
     */
	boolean addToOpenList(String url);

	
	/**
     * @description 判断给定的url是否注册
     * @param url
     * @return 已注册返回true，未注册返回false
     */
	boolean isInAllUrls(String url);

	/**
     * @description 获得open队列
     * @param 
     * @return 返回open列队
     */
	List<String> getOpenUrls();

	/**
     * @description 获得close队列
     * @param 
     * @return 返回close列队
     */
	List<String> getCloseUrls();
	/**
     * @description 获得已注册的url队列
     * @param 
     * @return 获得已注册的url队列
     */
	List<String> getAllUrls();
}
