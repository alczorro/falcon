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
 * @title: OpenTomcatThread.java
 * @package net.duckling.falcon.api.test.manager.tomcat;
 * @description: 随机开启关闭模式下，该线程用来开启tomcat，从closeList中取得要开启的url
 * 				 开启该url，并将该url添加到openList中
 * @author xuxin
 * @date 2012-10-25 下午2:57:04
 */

public class OpenTomcatThread implements Runnable {

	private List<String> openList;
	private List<String> closeList;
	private ISendMessage sendmsg;
	private int maxNum;
	public boolean isOver = false;
	private PrintMessage printMessage;

	public OpenTomcatThread(List<String> openList, List<String> closeList, int maxNum) {

		this.openList = openList;
		this.closeList = closeList;
		this.maxNum = maxNum;
		this.sendmsg = new SSHSendMessageImpl();
		
		printMessage = new PrintMessage(OpenTomcatThread.class.getName());
		
	}

	@Override
	public void run() {
		Random random = new Random();
		int min = 7; // 最小睡眠时间
		int max = 20; // 最大睡眠时间
		int sleepTime = 0;
		while (isOver == false) {
			sleepTime = random.nextInt(max) % (max - min + 1) + min;
			System.out.println("openThread:sleep time = " + sleepTime);
			try {
				Thread.sleep(sleepTime * 1000);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				printMessage.println(e.getMessage());
			}

			String url = getUrlFromCloseList();
			// System.out.println(url+":open");

			sendmsg.sendMessage(url, "open");

			// 延迟几秒中放入open队列中，这几秒钟让tomcat启动一下
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				printMessage.println(e.getMessage());
			}

			putUrlToOpenList(url);
		}

		System.out.println("open thread is closed!!");

	}

	/**
	 * @description 从close队列中取得一个url，close队列保存了所有关闭tomcat的主机url
	 * @param 
	 * @return 返回要开启的url
	 */
	private String getUrlFromCloseList() {
		String url;
		synchronized (closeList) {
			while (closeList.size() == 0) {
				try {
					closeList.wait();
				} catch (InterruptedException e) {
					printMessage.println(e.getMessage());
				}
			}
			// 从close列队中去除一个
			// System.out.println("OpenThread:"+"closeList.size="+closeList.size());

			int size = closeList.size();
			Random random = new Random();
			int removeOne = random.nextInt(size);
			url = ((LinkedList<String>) closeList).remove(removeOne);

			closeList.notify();
		}
		return url;
	}

	 /**
	 * @description 将url放入open队列，open队列保存了所有开启tomcat的主机url
	 * @param url
	 * @return 
	 */
	private void putUrlToOpenList(String url) {
		synchronized (openList) {
			while (openList.size() == this.maxNum) {
				try {
					openList.wait();
				} catch (InterruptedException e) {
					printMessage.println(e.getMessage());
				}
			}
			openList.add(url);
			openList.notify();

		}
	}

}
