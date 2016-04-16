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
import java.util.LinkedList;
import java.util.Vector;




public class TomcatListManagerImpl implements ITomcatListManager {

	List<String> openList = new LinkedList<String>();// 保存所有打开的url
	List<String> closeList = new LinkedList<String>(); // 保存所有关闭的url

	@Override
	public boolean addOpenUrl(String url) {
		openList.add(url);
		return true;
	}

	@Override
	public boolean addToCloseList(String url) {
		// 先从openList中寻找，如果没有找到，就说明存在与close中，返回
		int index = -1;
		for (int i = 0; i < openList.size(); i++) {
			if (url.equals(openList.get(i)) == true) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return true;

		synchronized (closeList) {
			synchronized (openList) {
				openList.remove(index);
			}
			closeList.add(url);
		}

		return true;
	}

	@Override
	public boolean addToOpenList(String url) {
		// 先从closeList中寻找，如果没有找到，就说明存在与open中，返回
		int index = -1;
		for (int i = 0; i < closeList.size(); i++) {
			if (url.equals(closeList.get(i)) == true) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return true;

		synchronized (openList) {
			synchronized (closeList) {
				closeList.remove(index);
			}
			openList.add(url);
		}
		return true;
	}

	@Override
	public List<String> getAllUrls() {
		List<String> openCol = this.getOpenUrls();
		List<String> closeCol = this.getCloseUrls();
		List<String> allCol = new Vector<String>();
		for (String url : openCol)
			allCol.add(url);
		for (String url : closeCol)
			allCol.add(url);
		return allCol;
	}

	@Override
	public List<String> getCloseUrls() {
		return closeList;
	}

	@Override
	public List<String> getOpenUrls() {
		return openList;
	}

	@Override
	public boolean isInAllUrls(String url) {
		List<String> allUrls = this.getAllUrls();
		return allUrls.contains(url);
	}

}
