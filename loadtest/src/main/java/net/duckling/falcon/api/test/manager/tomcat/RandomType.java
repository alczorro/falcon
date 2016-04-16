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
package net.duckling.falcon.api.test.manager.tomcat;

import java.util.List;

import net.duckling.falcon.api.test.manager.list.ITomcatListManager;

/**
 * @title: ISendMessage.java
 * @package net.duckling.falcon.api.test.manager.tomcat;
 * @description: 用来启动和关闭  随机开启/关闭 模式的类
 * @author xuxin
 * @date 2012-10-25 下午2:57:04
 */

public class RandomType {
	@SuppressWarnings("unused")
    private ITomcatOption tomcatOption;
	private ITomcatListManager listManager;
	private OpenTomcatThread oThread = null;
	private CloseTomcatThread cThread = null;

	public RandomType(ITomcatOption tomcatOption, ITomcatListManager listManager) {
		this.tomcatOption = tomcatOption;
		this.listManager = listManager;

	}

	 /**
	 * @description 结束开启/关闭 模式
	 * @param 
	 * @return 返回true
	 */
	public boolean closeRandomType() {

		oThread.isOver = true;
		cThread.isOver = true;

		return true;
	}

	/**
	 * @description 开启 开启/关闭 模式
	 * @param 
	 * @return 返回true
	 */
	public boolean openRandomType() {
		List<String> openList = listManager.getOpenUrls();
		List<String> closeList = listManager.getCloseUrls();
		int max = openList.size() + closeList.size();
		oThread = new OpenTomcatThread(openList, closeList, max);
		cThread = new CloseTomcatThread(openList, closeList, max);

		new Thread(oThread).start();
		new Thread(cThread).start();

		return true;
	}
}
