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

import java.util.*;

import net.duckling.falcon.api.test.common.PrintMessage;
import net.duckling.falcon.api.test.manager.ISendMessage;
import net.duckling.falcon.api.test.manager.SSHSendMessageImpl;


/**
 * @title: CloseTomcatThread.java
 * @package net.duckling.falcon.api.test.manager.tomcat;
 * @description: 随机开启关闭模式下，该线程用来关闭url，从openList中取得要关闭的url
 * 				 关闭该url，并将该url添加到closeList中
 * @author xuxin
 * @date 2012-10-25 下午2:57:04
 */

public class CloseTomcatThread implements Runnable {

	List<String> openList;
	List<String> closeList;
	private ISendMessage sendmsg;
	private int maxNum;
	public boolean isOver = false;
	private PrintMessage printMessage;

	public CloseTomcatThread(List<String> openList, List<String> closeList, int maxNum) {

		this.openList = openList;
		this.closeList = closeList;
		this.maxNum = maxNum;
		this.sendmsg = new SSHSendMessageImpl();
		printMessage = new PrintMessage(OpenTomcatThread.class.getName());
	}

	@Override
	public void run() {
		Random random = new Random();
		int min = 20; // 最小睡眠时间
		int max = 25; // 最大睡眠时间
		int sleepTime = 0;
		while (isOver == false) {
			sleepTime = random.nextInt(max) % (max - min + 1) + min;
			System.out.println("closeThread:sleep time = " + sleepTime);
			try {
				Thread.sleep(sleepTime * 1000);
			} catch (InterruptedException e) {
				printMessage.println(e.getMessage());
			}
			String url = getUrlFromOpenList();
			// System.out.println(url+":close");

			sendmsg.sendMessage(url, "close");

			// 延迟几秒中让tomcat 关闭一下
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				printMessage.println(e.getMessage());
			}

			putUrlToCloseList(url);
		}
		System.out.println("close thread is closed!!");
	}

	 /**
	 * @description 将url放入close队列，close队列保存了所有关闭tomcat的主机url
	 * @param url
	 * @return 
	 */
	private void putUrlToCloseList(String url) {
		synchronized (closeList) {
			while (closeList.size() == maxNum) {
				try {
					closeList.wait();
				} catch (InterruptedException e) {
					printMessage.println(e.getMessage());
				}
			}
			closeList.add(url);
			closeList.notify();
		}
	}

	/**
	 * @description 从open队列中取得一个url，open队列保存了所有开启tomcat的主机url
	 * @param 
	 * @return 返回要关闭的url 
	 */
	private String getUrlFromOpenList() {
		String url;
		synchronized (openList) {
			while (openList.size() == 1) {
				try {
					openList.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// System.out.println("CloseThread:"+"openList.size="+openList.size());
			// 从openList列队中去除一个
			int size = openList.size();
			Random random = new Random();
			int removeOne = random.nextInt(size);
			url = ((LinkedList<String>) openList).remove(removeOne);
			openList.notify();
		}
		return url;
	}

}
