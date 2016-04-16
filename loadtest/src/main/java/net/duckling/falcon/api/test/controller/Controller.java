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
package net.duckling.falcon.api.test.controller;

import java.util.LinkedList;
import java.util.List;

import net.duckling.falcon.api.test.common.PrintMessage;
import net.duckling.falcon.api.test.manager.list.ITomcatListManager;
import net.duckling.falcon.api.test.manager.list.TomcatListManagerImpl;
import net.duckling.falcon.api.test.manager.tomcat.ITomcatOption;
import net.duckling.falcon.api.test.manager.tomcat.TomcatOptionImpl;

/**
 * @title: Controller.java
 * @package net.duckling.falcon.api.test.controller;
 * @description: 用来分发view的请求
 * @author xuxin
 * @date 2012-10-25 下午2:57:04
 */
public class Controller {
	private ITomcatListManager listManager;
	private ITomcatOption tomcatOption;
	private PrintMessage printMessage;

	public Controller() {
		listManager = new TomcatListManagerImpl();
		tomcatOption = new TomcatOptionImpl();
		printMessage = new PrintMessage(Controller.class.getName());
		InitList();
	}

	/**
	 * @description 初始化 注册tomcat的主机列表
	 * @param 
	 * @return 
	 */
	public void InitList() {
		listManager.addOpenUrl("10.10.1.104");
		listManager.addOpenUrl("10.10.1.105");
		listManager.addOpenUrl("10.10.1.106");
	}

	/**
	 * @description 获得所有主机列表
	 * @param 
	 * @return 返回所有主机列表
	 */
	public List<String> getAllClient() {
		return listManager.getAllUrls();
	}

	/**
	 * @description 获得所有开启tomcat主机列表
	 * @param 
	 * @return 返回所有开启tomcat的主机列表
	 */
	public List<String> getOpenClient() {
		return listManager.getOpenUrls();
	}

	/**
	 * @description 获得所有关闭tomcat主机列表
	 * @param 
	 * @return 返回所有关闭tomcat的主机列表
	 */
	public List<String> getCloseClient() {
		return listManager.getCloseUrls();
	}

	/**
	 * @description 开启某主机tomcat
	 * @param url 要开机的主机url
	 * @return 开启成功返回true，否则返回false
	 */
	public boolean openOneTomcat(String url) {
		// 先判断一下要打开的url是否存在于url list中
		if (listManager.isInAllUrls(url) == false) {
			printMessage.println(url + " is not in url list");
			return false;
		}

		// 打开某主机的tomcat
		if (tomcatOption.openOneTomcat(url) == false) {
			printMessage.println(url + " open fail");
			return false;
		}

		// 打开成功之后，犀利的添加到openList中
		if (listManager.addToOpenList(url) == false) {
			printMessage.println(url + " add to open list fail");
			return false;
		}

		return true;
	}

	/**
	 * @description 关闭某主机tomcat
	 * @param url 要关闭的主机url
	 * @return 关闭成功返回true，否则返回false
	 */
	public boolean closeOneTomcat(String url) {
		// 先判断一下要关闭的url是否存在于url list中
		if (listManager.isInAllUrls(url) == false) {
			printMessage.println(url + " is not in url list");
			return false;
		}

		// 关闭某主机的tomcat
		if (tomcatOption.closeOneTomcat(url) == false) {
			printMessage.println(url + " close fail");
			return false;
		}

		// 关闭成功之后，添加到closeList中
		if (listManager.addToCloseList(url) == false) {
			printMessage.println(url + " add to close list fail");
			return false;
		}

		return true;
	}

	/**
	 * @description 开启某主机tomcat，关闭其他主机的tomcat
	 * @param url 要开机的主机url
	 * @return 开启成功返回true，否则返回false
	 */
	public boolean openOnlyOneTomcat(String url) {
		// 先判断一下要关闭的url是否存在于url list中
		if (listManager.isInAllUrls(url) == false) {
			printMessage.println(url + " is not in url list");
			return false;
		}

		// 需要一定的手段获得 需要关闭的list 列表
		List<String> closeList = new LinkedList<String>();
		List<String> openList = listManager.getOpenUrls();
		for (String u : openList) {
			if (u.equals(url) == true)
				continue;
			else
				closeList.add(u);
		}

		// 只开启某一台服务器
		if (tomcatOption.openOnlyOneTomcat(url, closeList) == false) {
			printMessage.println(url + " open only one tomcat fail");
			return false;
		}

		// 更新list列表
		for (String u : closeList)
			listManager.addToCloseList(u);
		listManager.addToOpenList(url);
		return true;
	}

	/**
	 * @description 开启 随机模式
	 * @param 
	 * @return 返回true
	 */
	public boolean startRandomType() {
		return tomcatOption.randomStartShutTomcat(listManager);
	}

	/**
	 * @description 关闭  随机模式
	 * @param 
	 * @return 返回true
	 */
	public boolean closeRandomType() {
		return tomcatOption.closeRandomType();
	}

}
