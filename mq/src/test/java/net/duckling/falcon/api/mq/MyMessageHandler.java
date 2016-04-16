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

import junit.framework.Assert;

public class MyMessageHandler implements IDFMessageHandler {

    private String name;
    private MessageBody[] body;

    public MyMessageHandler(String name, MessageBody[] body) {
        this.name = name;
        this.body = body;
    }

    private int index = 0;

    @Override
    public void handle(Object messageBody, String routingKey) {
        MessageBody cl = (MessageBody) messageBody;
        System.out.println("recieve message from " + name + ":" + cl.getBody() + "," + cl.getMaps());
        Assert.assertEquals(cl.getType(), body[index].getType());
        Assert.assertEquals(cl.getId(), body[index].getId());
        int count = cl.getList().size();
        for (int i = 0; i < count; i++) {
            assertMessageBodyEqual(cl.getList().get(i), body[index].getList().get(i));
        }
        for (String key : cl.getMaps().keySet()) {
            assertMessageBodyEqual(cl.getMaps().get(key), body[index].getMaps().get(key));
        }
        index++;
    }

    private void assertMessageBodyEqual(CreateLog m1, CreateLog m2) {
        Assert.assertEquals(m1.getId(), m2.getId());
        Assert.assertEquals(m1.getName(), m2.getName());
        Assert.assertEquals(m1.getPoint().getX(), m2.getPoint().getX());
        Assert.assertEquals(m1.getPoint().getY(), m2.getPoint().getY());
        Assert.assertEquals(m1.getTime(), m2.getTime());
    }

}
