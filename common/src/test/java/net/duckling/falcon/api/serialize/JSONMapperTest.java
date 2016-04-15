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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class JSONMapperTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetNestBean() {
        MessageBody b = new MessageBody();
        List<CreateLog> list = new ArrayList<CreateLog>();
        list.add(new CreateLog(2, "CC", 1, 2));
        list.add(new CreateLog(3, "DD", 1, 3));
        b.setList(list);
        Map<String, CreateLog> map = new HashMap<String, CreateLog>();
        map.put("hello", new CreateLog(4, "EE", 2, 3));
        b.setMaps(map);
        b.setType("create_org");
        b.setTimestamp(new Date());
        b.setBody(new CreateLog(1, "CNIC", 2, 4));
        String json = JSONMapper.getJSONString(b);
        System.out.println(json);
        MessageBody b1 = JSONMapper.getBean(json, MessageBody.class);
        testHello(b1.getClass());
        System.out.println(b1.getTimestamp());
        UnderscoreDemo ud = new UnderscoreDemo();
        ud.setIdStr("10");
        ud.setIntVal(22);
        ud.setLongValue(10000);

        json = JSONMapper.getJSONStringForUnderscore(ud);
        System.out.println(json);
        UnderscoreDemo ud1 = JSONMapper.getBeanForUnderscore(json, UnderscoreDemo.class);
        Assert.assertEquals(ud.getIdStr(), ud1.getIdStr());
    }

    private void testHello(Class<?> clazz) {
        Field[] fs = clazz.getDeclaredFields(); // 得到所有的fields
        for (Field f : fs) {
            Class<?> fieldClazz = f.getType(); // 得到field的class及类型全路径
            System.out.println(fieldClazz + "," + f.getName());
            if (fieldClazz.isPrimitive()) // 【1】 //判断是否为基本类型
                continue;
            if (fieldClazz.getName().startsWith("java.lang")) // getName()返回field的类型全路径；
                continue;
            if (fieldClazz.isAssignableFrom(List.class)) { // 【2】关键的地方，如果是List类型，得到其Generic的类型
                Type fc = f.getGenericType();
                if (fc == null)
                    continue;
                if (fc instanceof ParameterizedType) { // 【3】如果是泛型参数的类型
                    ParameterizedType pt = (ParameterizedType) fc;
                    Class<?> z = (Class<?>) pt.getActualTypeArguments()[0]; // 【4】得到泛型里的class类型对象。
                    System.out.println(z);
                }
            }
        }
    }

}
