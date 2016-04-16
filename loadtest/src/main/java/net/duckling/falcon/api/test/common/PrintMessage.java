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
package net.duckling.falcon.api.test.common;

import java.util.logging.Logger;

/**
 * @title: PrintMessage.java
 * @package net.duckling.falcon.api.test.common;
 * @description: 输出
 * @author xuxin
 * @date 2012-10-25 下午2:57:04
 */
public class PrintMessage {
	private String className=null;
	public PrintMessage()
	{}
	public PrintMessage(String className)
	{
		this.className = className;
	}
	public void println(String msg) {
		if(className!=null)
		{
			Logger logger=Logger.getLogger(className);
			logger.info(msg);
			
		}
		else
			System.out.println(className+":"+msg);
	}

}
