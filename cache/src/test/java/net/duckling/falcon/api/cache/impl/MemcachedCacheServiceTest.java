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
package net.duckling.falcon.api.cache.impl;

import junit.framework.Assert;
import net.duckling.falcon.api.cache.ICacheService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MemcachedCacheServiceTest {

    private static ICacheService cache = null;

    @BeforeClass
    public static void setup() {
        MemcachedCacheService cacheService = new MemcachedCacheService();
        // cacheService.setMemcachedURL("10.10.2.7:11211");
        cacheService.setMemcachedURL("Null");
        cacheService.doInit();
        cache = cacheService;
    }

    @AfterClass
    public static void tearDown() {
        cache.doDestroy();
    }

    @Test
    public void testGetSet() {
        cache.set("some", "key");
        cache.set("obj1", new User("abc", "what"));
        System.out.println(cache.get("some"));
        Assert.assertEquals("key", cache.get("some"));
        User user = (User) cache.get("obj1");
        System.out.println(user.getUserName() + "," + user.getPassword());
        Assert.assertEquals("abc", user.getUserName());
        Assert.assertEquals("what", user.getPassword());
    }

    // @Test
    // public void testWhenMemcachedFailed() {
    // long tmp = System.currentTimeMillis();
    // String key = "hello" + tmp;
    // User u = new User("abc", "what");
    // cache.set(key, u);
    // cache.doDestroy();
    // Object o = cache.get(key);
    // Assert.assertNull(o);
    // cache.set(key, u);
    // User o2 = (User) cache.get(key);
    // Assert.assertNotNull(o2);
    // Assert.assertEquals(o2.getUserName(), "abc");
    // }

    @Test
    public void testReconnect() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            double num = Math.random();
            String key = "just-test";
            cache.set(key, num);
            Object obj = cache.get(key);
            if (obj != null) {
                double expect = (double) cache.get(key);
                System.out.println(expect);
                Assert.assertEquals(num, expect);
            }
            Thread.sleep(500);
        }
    }
}
