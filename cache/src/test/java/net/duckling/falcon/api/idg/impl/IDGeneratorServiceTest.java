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
package net.duckling.falcon.api.idg.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import junit.framework.Assert;
import net.duckling.falcon.api.idg.IIDGeneratorService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class IDGeneratorServiceTest {

    private static IIDGeneratorService idg;

    private String appName = "clbtest";
    private String groupName = "docmeta";
    private int val = 10;
    private int length = 10;

    @BeforeClass
    public static void setUp() throws Exception {
        TimeProfilable timeProfilable = new TimeProfilable() {
		@Override
		public Object invoke(String[] args) {
		    String host=null, port=null;
		    host = System.getenv("JUNIT_IDG_HOST");
		    if (null != host) port = System.getenv("JUNIT_IDG_PORT");
		    if (null != port) {
			System.out.println("Host: "+host+" Port: "+port);
			idg = new IDGeneratorService(host, Integer.parseInt(port));
		    } else {
			System.out.println("Please set env JUNIT_IDG_HOST and JUNIT_IDG_PORT for redis services, otherwise this test will be skipped.");
			System.exit(0);
		    }
		    return null;
		}
	    };
        timeProfilable.profile("setUp");
    }

    @Test
    public void testInit() {
        TimeProfilable timeProfilable = new TimeProfilable() {
            @Override
            public Object invoke(String[] args) {
                idg.removeAll(appName,groupName);
                Map<Integer, Integer> map = new HashMap<Integer, Integer>();
                for (int i = 0; i < length; i++) {
                    map.put(i, val);
                }
                idg.init(appName, groupName, map);
                int id = idg.getNextID(appName, groupName, 1);
                Assert.assertEquals(id, val + 1);
                id = idg.getNextID(appName, groupName, 1);
                Assert.assertEquals(id, val + 2);
                return null;
            }

        };
        timeProfilable.profile("testInit");
    }

    @Test
    public void testGetNextID() {
        TimeProfilable timeProfilable = new TimeProfilable() {
            @Override
            public Object invoke(String[] args) {
                Random rand = new Random();
                int gid = rand.nextInt(length);
                int id = idg.getNextID(appName, groupName, gid);
                Assert.assertEquals(val + 1, id);
                for(int i=0; i<10; i++){
                    id = idg.getNextID(appName, groupName, gid);
                }
                Assert.assertEquals(val + 11, id);
                
                
                return null;
            }

        };
        timeProfilable.profile("testGetNextID");
    }
    
    
    @Test
    public void testGetNotExistGroupID() {
        TimeProfilable timeProfilable = new TimeProfilable() {
            @Override
            public Object invoke(String[] args) {
                idg.delete(appName, groupName, 65536);
                int id = idg.getNextID(appName, groupName, 65536);
                Assert.assertEquals(id, 1);
                return null;
            }

        };
        timeProfilable.profile("testGetNotExistGroupID");
    }
    
    
    @Test
    public void testGetMap() {
        TimeProfilable timeProfilable = new TimeProfilable() {
            @Override
            public Object invoke(String[] args) {
                Map<Integer,Integer> map = idg.getGroupMap(appName, groupName);
                for(Map.Entry<Integer, Integer> en:map.entrySet()){
                    System.out.println(en.getKey()+":"+en.getValue());
                }
                return null;
            }

        };
        timeProfilable.profile("testGetMap");
    }
    

    @AfterClass
    public static void tearDown() {
        TimeProfilable timeProfilable = new TimeProfilable() {

            @Override
            public Object invoke(String[] args) {
                idg.close();
                return null;
            }

        };
        timeProfilable.profile("close");
    }

    private static abstract class TimeProfilable {
        public abstract Object invoke(String[] args);

        public Object profile(String methodName, String... args) {
            long start = System.currentTimeMillis();
            Object obj = this.invoke(args);
            long end = System.currentTimeMillis();
            System.out.println("Run method[" + methodName + "] use time " + (end - start) + " ms");
            return obj;
        }
    }

}
