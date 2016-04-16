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
package net.duckling.falcon.api.mq.impl;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

/**
 * @title: DFMessageSerializer.java
 * @package net.duckling.falcon.api.mq.impl
 * @description: 消息序列化与反序列化类
 * @author clive
 * @date 2013-7-31 上午9:58:03
 */
public class DFMessageSerializer {
    private static final String ENCODE = "UTF-8";
    private static Gson gson = new Gson();

    private static Logger LOG = Logger.getLogger(DFMessageSerializer.class);

    private DFMessageSerializer() {
    };

    /**
     * @description 将对象转换成json的格式的字符数组
     * @param 需要序列化的对象
     * @return 字符数组 返回的格式为 {"className":"jsonString"}
     */
    public static byte[] serialize(Object message) {
        String messageString = message.getClass().getName() + ":" + gson.toJson(message);
        try {
            return messageString.getBytes(ENCODE);
        } catch (UnsupportedEncodingException e) {
            LOG.error("unsupported encoding for " + ENCODE, e);
        }
        return null;
    }

    /**
     * @description 反序列化方法
     * @param body
     *            带反序列化的字符数组 仅接收 {"className":"jsonString"}这种格式的字符
     * @return 返回已序列化好的对象 可直接强制类型转换
     */
    public static Object deserialize(byte[] body) {
        String messageString;
        String className = "";
        try {
            messageString = new String(body, ENCODE);
            int colonIndex = messageString.indexOf(':');
            if( colonIndex > 0 ) {
                className = messageString.substring(0, colonIndex);
                String json = messageString.substring(colonIndex + 1);
                Class<?> clazz = Class.forName(className);
                return gson.fromJson(json, clazz);
            }else{
                return messageString;
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error("unsupported encoding for " + ENCODE, e);
        } catch (ClassNotFoundException e) {
            LOG.error("Class not found :" + className, e);
        }
        return null;
    }
}
