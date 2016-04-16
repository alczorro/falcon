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

import net.duckling.falcon.api.test.manager.*;
import net.duckling.falcon.api.test.manager.list.ITomcatListManager;


public class TomcatOptionImpl implements ITomcatOption {

	private ISendMessage sendCmd;
	private RandomType randomType;

	public TomcatOptionImpl() {
		sendCmd = new SSHSendMessageImpl();
		randomType = null;
	}

	@Override
	public boolean closeOneTomcat(String url) {
		return sendCmd.sendMessage(url, ISendMessage.closeCmd);
	}

	@Override
	public boolean openOneTomcat(String url) {
		return sendCmd.sendMessage(url, ISendMessage.openCmd);
	}

	@Override
	public boolean openOnlyOneTomcat(String url, List<String> closeUrlList) {
		if (this.openOneTomcat(url) == false)
			return false;
		for (String u : closeUrlList) {
			if (this.closeOneTomcat(u) == false)
				return false;
		}
		return true;
	}

	@Override
	public boolean closeRandomType() {
		if (randomType == null)
			return true;
		randomType.closeRandomType();
		randomType = null;
		return true;
	}

	@Override
	public boolean randomStartShutTomcat(ITomcatListManager listManager) {
		randomType = new RandomType(this, listManager);
		randomType.openRandomType();
		return true;
	}

}
