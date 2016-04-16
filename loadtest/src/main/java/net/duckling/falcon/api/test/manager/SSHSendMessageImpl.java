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

import net.duckling.falcon.api.test.common.ExecShell;
import net.duckling.falcon.api.test.common.PrintMessage;

/**
 * @title: ISendMessage.java
 * @package net.duckling.falcon.api.test.manager.tomcatmanage;
 * @description: 调用shell脚本，来向url发送数据msg
 * @author xuxin
 * @date 2012-10-25 下午2:57:04
 */
public class SSHSendMessageImpl implements ISendMessage {

	private PrintMessage printMessage;
	private String shellPath;

	public SSHSendMessageImpl() {
		printMessage = new PrintMessage(SSHSendMessageImpl.class.getName());
		shellPath = "/home/xuxin/ctrlTomcat.sh";
	}

	

	@Override
	public boolean sendMessage(String url, String msg) {
		try {
			if (msg.equals(ISendMessage.openCmd)) {
				String cmd = shellPath + " " + url + " " + msg;
				ExecShell.runShell(cmd);
				printMessage.println(url + ":" + "open");
				return true;
			} else if (msg.equals(ISendMessage.closeCmd)) {
				String cmd = shellPath + " " + url + " " + msg;
				ExecShell.runShell(cmd);
				printMessage.println(url + ":" + "close");
				return true;
			} else {
				printMessage.println("cmd is error");
				return false;
			}

		} catch (Exception e) {
			//e.printStackTrace();
			printMessage.println(e.getMessage());
			return false;
		}
	}
	
	 /**
	 * @description 返回shell脚本的位置
	 * @param 
	 * @return 返回调用的shell脚本位置
	 */
	public String getShellPath() {
		return shellPath;
	}

	/**
	 * @description 设置shell脚本的位置
	 * @param shellPath 新的shell脚本位置
	 * @return 
	 */
	public void setShellPath(String shellPath) {
		this.shellPath = shellPath;
	}


}
