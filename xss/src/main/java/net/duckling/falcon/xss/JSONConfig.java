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
package net.duckling.falcon.xss;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.safety.Whitelist;

class JSONConfig {
	public static Whitelist parse(String filename) throws IOException, ParseException {
		String jsonString = FileUtils.readFileToString(new File(filename),
				"UTF-8");
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(jsonString);
		if (obj instanceof JSONObject){
			Whitelist whitelist = new Whitelist();
			JSONObject config = (JSONObject)obj;
			addTags(whitelist, config);
			addProtocols(whitelist, config);
			return whitelist;
		}
		return Whitelist.none();
	}

	private static void addProtocols(Whitelist whitelist, JSONObject config) {
		JSONObject protocolsJson = (JSONObject)config.get("protocols");
		for(String key :protocolsJson.keySet()){
			String[] pair=key.split("\\.");
			if (pair.length==2){
				JSONArray protocols = (JSONArray)protocolsJson.get(key);
				for(Object p:protocols){
					whitelist.addProtocols(pair[0], pair[1], (String)p);
				}
			}
		}
	}

	private static void addTags(Whitelist whitelist, JSONObject config) {
		JSONObject whiteListJson = (JSONObject)config.get("whiteList");
		for(String tagname:whiteListJson.keySet()){
			whitelist.addTags( tagname);
			JSONArray attributes = (JSONArray)whiteListJson.get(tagname);
			for(Object attribute:attributes){
				whitelist.addAttributes(tagname, (String)attribute);
			}
		}
	}
}
