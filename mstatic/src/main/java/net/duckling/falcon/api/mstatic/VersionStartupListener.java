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
package net.duckling.falcon.api.mstatic;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class VersionStartupListener implements ServletContextListener {
	private static Logger log = Logger.getLogger(VersionStartupListener.class);
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context =  sce.getServletContext();
		String versionFile =context.getInitParameter("version-file");
		String fullpath = context.getRealPath(versionFile);
		try {
			String version = FileUtils.readFileToString(new File(fullpath));
			if (version!=null){
				version = version.trim();
			}else{
				version ="N/A";
			}
			VersionAttributeHelper.setVersion(context, version);
		} catch (IOException e) {
			log.error("Reading version file failed.",e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//do nothing
	}
}