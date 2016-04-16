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
 * @title: ITomcatOption.java
 * @package net.duckling.falcon.api.test.manager.tomcat;
 * @description: 管理tomcat
 * @author xuxin
 * @date 2012-10-25 下午2:57:04
 */
public interface ITomcatOption {
	 /**
	 * @description 打开指定url主机上的tomcat
	 * @param url
	 * @return 打开成功则返回true，失败返回false
	 */
	boolean openOneTomcat(String url);

	 /**
	 * @description 关闭指定url主机上的tomcat
	 * @param url
	 * @return 关闭成功则返回true，失败返回false
	 */
	boolean closeOneTomcat(String url);

	/**
	 * @description 打开指定url的tomcat，关闭closeUrlList中所有url的主机
	 * @param url
	 * @param closeUrlList保存了所有待关闭tomcat主机的url
	 * @return 成功返回true，失败返回false
	 */
	boolean openOnlyOneTomcat(String url, List<String> closeUrlList);

	/**
	 * @description 开启随机开启/关闭 tomcat模式
	 * @param listManager 用来管理注册的url的类
	 * @return 成功返回true，失败返回false
	 */
	boolean randomStartShutTomcat(ITomcatListManager listManager);

	/**
	 * @description 关闭随机开启/关闭 tomcat模式
	 * @param 
	 * @return 关闭成功返回true，失败返回false
	 */
	boolean closeRandomType();
}
