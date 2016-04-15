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
package net.duckling.falcon.api.serialize;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONMapper {

    public static final String CHAR_SET = "UTF-8";

    private static final Logger SYS_LOG = Logger.getLogger(JSONMapper.class);

    public static String getJSONString(Object bean) {
        return gson.toJson(bean);
    }

    public static <T> T getBean(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    private static final String ENCODE = "UTF-8";
    private static Gson gson = new Gson();

    private static Gson gsonForUnderscore = new GsonBuilder().setFieldNamingPolicy(
            FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    public static <T> T getBeanForUnderscore(String json, Class<T> clazz){
        return gsonForUnderscore.fromJson(json, clazz);
    }
    
    public static String getJSONStringForUnderscore(Object bean){
        return gsonForUnderscore.toJson(bean);
    }
    
    public static byte[] getJSONBytes(Object object) {
        String messageString = gson.toJson(object);
        try {
            return messageString.getBytes(ENCODE);
        } catch (UnsupportedEncodingException e) {
            SYS_LOG.error("unsupported encoding for " + ENCODE, e);
        }
        return null;
    }

    public static <T> T getBean(byte[] bytes, Class<T> clazz) {
        String messageString;
        try {
            messageString = new String(bytes, ENCODE);

            return gson.fromJson(messageString, clazz);
        } catch (UnsupportedEncodingException e) {
            SYS_LOG.error("unsupported encoding for " + ENCODE, e);
        }
        return null;
    }

    public static JsonObject getJsonObject(String str) {
        JsonParser parser = new JsonParser();
        JsonObject json = (JsonObject) parser.parse(str);
        return json;
    }

}
