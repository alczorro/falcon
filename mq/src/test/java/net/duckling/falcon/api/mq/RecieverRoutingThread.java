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


public class RecieverRoutingThread extends Thread {

    private IDFSubscriber r = null;
    private String routingKey ;

    @Override
    public void run() {
        try {
            r.registHandler(routingKey, new MyMessageHandler("subscriber", body));
            r.receive();
        } catch (NotFoundHandlerException e) {
            e.printStackTrace();
        }
    }

    public RecieverRoutingThread(IDFSubscriber r,String routingKey, MessageBody[] body) {
        this.r = r;
        this.body = body;
        this.routingKey = routingKey;
    }

    private MessageBody[] body;

    public IDFSubscriber getReciever() {
        return r;
    }

}
