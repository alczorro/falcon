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
        m = new DFMQManager("guest", "guest", "10.10.2.6");
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
