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
package net.duckling.falcon.api.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import net.duckling.falcon.api.test.common.PrintMessage;
import net.duckling.falcon.api.test.controller.Controller;

public class View {
	private Controller controller;
	private BufferedReader sin;
	private PrintMessage printMessage;

	public View() {
		controller = new Controller();
		sin = new BufferedReader(new InputStreamReader(System.in));
		printMessage = new PrintMessage(View.class.getName());
	}

	private String showMenu() {

		String line = "";
		System.out.println("选择操作:");
		System.out.println("0.退出");
		System.out.println("1.显示所有客户端地址");
		System.out.println("2.显示开启Tomcat的客户端");
		System.out.println("3.显示关闭Tomcat的客户端");
		System.out.println("4.关闭某客户端的Tomcat");
		System.out.println("5.开启某客户端的Tomcat");
		System.out.println("6.只开启某客户端的Tomcat");
		System.out.println("7.随机开启/关闭客户端的Tomcat");
		System.out.println("8.关闭7中打开的线程");
		System.out.println("选择:");
		try {
			line = sin.readLine();
		} catch (IOException e) {
			printMessage.println(e.getMessage());
		}
		return line;
	}

	public void run() {
		String choice = "";
		do {
			choice = this.showMenu();
			try {
				switch (Integer.parseInt(choice)) {
				case 0:// 0.退出
					break;
				case 1: // 显示所有客户端地址
					showAllClinet();
					break;
				case 2: // 显示开启Tomcat的客户端
					showOpenClient();
					break;
				case 3:// 显示关闭Tomcat的客户端
					showCloseClien();
					break;
				case 4: // 关闭某客户端的Tomcat
					shutdownOneTomcat();
					break;
				case 5: // 开启某客户端的Tomcat
					startOneTomcat();
					break;
				case 6:// 只开启某客户端的Tomcat
					onlyOpenOneTomcat();
					break;
				case 7:// 随机开启/关闭客户端的Tomcat
					randomStartShutTomcat();
					break;
				case 8:// 关闭7所开的线程
					closeStartShutType();
					break;
				default:
					System.out.println(choice);
					System.out.println("错误输入");
					break;

				}
			} catch (NumberFormatException e) {
				printMessage.println("输入的不是数字 ，重来遍吧");
			} catch (IOException e) {
				printMessage.println("io 有错误");
			}
			System.out
					.println("--------------------------------------------------");
		} while (choice.equals("0") == false);
	}

	private void closeStartShutType() {
		if (controller.closeRandomType() == false) {
			//printMessage.println("close start/shut type fail");
			System.out.println("建议重启");
		} else {
			System.out.println("关闭start/shut模式，不过可能要延迟几秒钟才会完全关闭线程");
			//printMessage.println("关闭start/shut模式，不过可能要延迟几秒钟才会完全关闭线程");
		}
	}

	private void randomStartShutTomcat() {
		if (controller.startRandomType() == false) {
			printMessage.println("start start/shut type fail");
		} else {
			System.out.println("开启start/shut模式");
			//printMessage.println("开启start/shut模式");
		}
	}

	private void onlyOpenOneTomcat() throws IOException {
		String url = null;
		System.out.println("输入只开启的服务器ip:");
		url = sin.readLine();
		if (controller.openOnlyOneTomcat(url) == false) {
			printMessage.println("open only one tomat fail");
		} else {
			System.out.println("开启成功");
		}
	}

	private void startOneTomcat() throws IOException {
		String url = null;
		System.out.println("输入要开启的服务器ip:");
		url = sin.readLine();
		if (controller.openOneTomcat(url) == false) {
			printMessage.println("open only one tomat fail");
		} else {
			System.out.println("开启成功");
		}
	}

	private void shutdownOneTomcat() throws IOException {
		String url = null;
		System.out.println("输入要关闭的服务器ip:");
		url = sin.readLine();
		if (controller.closeOneTomcat(url) == false) {
			printMessage.println("close only one tomat fail");
		} else {
			System.out.println("成功关闭");
		}
	}

	private void showCloseClien() {
		List<String> closeClient = controller.getCloseClient();
		if (closeClient.size() == 0) {
			System.out.println("没有关闭tomcat的主机");
		} else {
			System.out.println("关闭tomcat的主机列表：");
			for (String u : closeClient)
				System.out.println(u);
		}
	}

	private void showOpenClient() {
		List<String> openClient = controller.getOpenClient();
		System.out.println("开启tomcat的主机列表");
		for (String u : openClient)
			System.out.println(u);
	}

	private void showAllClinet() {
		List<String> allClient = controller.getAllClient();
		System.out.println("所有的主机列表");
		for (String u : allClient)
			System.out.println(u);
	}

	public static void main(String[] args) {
		View viewClass = new View();
		viewClass.run();
	}

}
