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
package net.duckling.common.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @date 2011-3-15
 * @author Clive Lee
 */
public final class JSONHelper {
	private JSONHelper(){}
	private static final Logger LOG = Logger.getLogger(JSONHelper.class);
	/**
	 * 将对象写进response，以json格式，
	 * @param object 一般是JSonArray或者JSONObject实例，这样toString才有意义
	 * 
	 * */
	public static void writeJSONObject(HttpServletResponse response,Object object) {
		PrintWriter writer = null;
		try {
			//为了兼容IE系浏览器，特意设置成text/html格式
			response.setContentType("text/html");
			writer = response.getWriter();
			writer.write(object.toString());
		} catch (IOException e) {
			LOG.error("JSONHelper write json object IOException:"+e.getMessage());
			LOG.debug(e.getStackTrace());
		}finally {
			if (writer!=null){
				writer.flush();
				writer.close();
			}
		}
	}
}