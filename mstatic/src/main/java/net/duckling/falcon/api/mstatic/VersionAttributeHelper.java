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

import javax.servlet.ServletContext;

public class VersionAttributeHelper {
	private static final String ATTRIBUTE_KEY = "falcon.version";
	private static final String TRANS_KEY = "falcon.version.trans";

	public static void setVersion(ServletContext context, String version) {
		if (version == null) {
			version = "";
		}
		String trans = version.replaceAll("[\\. ]", "_");
		context.setAttribute(ATTRIBUTE_KEY, version);
		context.setAttribute(TRANS_KEY, trans);
	}

	public static String readVersionTransformed(ServletContext context) {
		return (String) context.getAttribute(TRANS_KEY);
	}

	public static String readVersion(ServletContext context) {
		return (String) context.getAttribute(ATTRIBUTE_KEY);
	}
}
