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
package net.duckling.falcon.api.mq;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DFMQManagerTest {

    private DFMQManager m;
    
    @Before
    public void setUp() throws Exception {
	String username=null, password=null, host=null;
	username = System.getenv("JUNIT_DFMQ_USER");
	if (null != username) {
	    password = System.getenv("JUNIT_DFMQ_PASS");
	    if (null != password) {
		host = System.getenv("JUNIT_DFMQ_HOST");
		if (null != host) {
		    System.out.println("Connect MQ with "+username
				       +":"+password+"@"+host);
		    m = new DFMQManager(username, password, host);
		    return;
		}
	    }
	}
	System.out.println("Please set env for JUNIT_DFMQ_USER, JUNIT_DFMQ_PASS and JUNIT_DFMQ_HOST. Otherwise this test will be skipped.");
	System.exit(0);       
    }

    @Test
    public void testClearQueueData() {
        m.clearQueueData("demo2_fanout_0");
        m.clearQueueData("demo2_fanout_1");
        m.clearQueueData("demo2_fanout_2");
        m.clearQueueData("demo3_topic_0");
        m.clearQueueData("demo3_topic_1");
        m.clearQueueData("demo3_topic_2");
        m.clearQueueData("demo4_topic_0");
        m.clearQueueData("demo4_topic_1");
        m.clearQueueData("demo4_topic_2");
        //demo1_direct_0
        m.clearQueueData("demo1_direct_0");
        m.clearQueueData("demo1_direct_1");
        m.clearQueueData("demo1_direct_2");
    }
    
    @After
    public void tearDown(){
        m.close();
    }

}
