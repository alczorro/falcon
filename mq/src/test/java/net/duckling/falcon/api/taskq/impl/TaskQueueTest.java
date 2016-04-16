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
package net.duckling.falcon.api.taskq.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TaskQueueTest {

    private TaskProducer p;

    String host = "127.0.0.1";
    int port = 6379;

    @Before
    public void setUp() throws Exception {
        p = new TaskProducer(host, port);
    }

    @After
    public void close() {
        p.close();
    }

    @Test
    public void testProduce() {
        for (int i = 0; i < 2000; i++) {
            BaseTask r = new BaseTask(i, "hello world");
            p.produce("simple", r);
        }
    }

    @Test
    public void testConsume() throws InterruptedException {
        TaskConsumer c2 = new TaskConsumer(host, port);
        MyThread a1 = new MyThread(c2);
        a1.start();
        Thread.sleep(20000);
        a1.setStopFlag(true);
    }

}

class MyThread extends Thread {
    public TaskConsumer consumer;

    public MyThread(TaskConsumer c) {
        System.out.println(this.getId());
        this.consumer = c;
    }

    private boolean stopFlag = false;

    public void setStopFlag(boolean flag) {
        this.stopFlag = flag;
    }

    private IConsumerHandler<BaseTask> handler = new IConsumerHandler<BaseTask>() {
        @Override
        public void handle(BaseTask task) {
            System.out.println(String.format("Eat task {id->%d,content->%s}", task.getId(), task.getContent()));
        }
    };

    public void run() {
        while (!stopFlag) {
            System.out.print(String.format("Thread[%d] - ", this.getId()));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            consumer.consume("simple", BaseTask.class, handler);
        }
    }
}
