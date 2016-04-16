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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SimpleStringMatchTest {

    
    private SimpleStringMatch ssm = new SimpleStringMatch();
    
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testConvertToRex() {
        Assert.assertEquals(null, ssm.convertToRex(null));
        Assert.assertEquals("hello", ssm.convertToRex("hello"));
        Assert.assertEquals("hello\\.\\w+", ssm.convertToRex("hello.*"));
        Assert.assertEquals("\\w+\\.hello", ssm.convertToRex("*.hello"));
        Assert.assertEquals("\\w+\\.hello\\.\\w+", ssm.convertToRex("*.hello.*"));
        Assert.assertEquals("hello\\.\\w+\\.world", ssm.convertToRex("hello.*.world"));
        Assert.assertEquals("\\w+\\.\\w+\\.hello", ssm.convertToRex("*.*.hello"));
        Assert.assertEquals("(\\w+\\.)*hello", ssm.convertToRex("#.hello"));
        Assert.assertEquals("hello(\\.\\w+)*", ssm.convertToRex("hello.#"));
        Assert.assertEquals("(\\w+\\.)*hello(\\.\\w+)*", ssm.convertToRex("#.hello.#"));
        Assert.assertEquals("hello\\.(\\w+\\.)*world", ssm.convertToRex("hello.#.world"));
        Assert.assertEquals("\\w+\\.(\\w+\\.)*hello", ssm.convertToRex("*.#.hello"));
        Assert.assertEquals("(\\w+\\.)*\\w+\\.hello", ssm.convertToRex("#.*.hello"));
    }
    
    @Test
    public void testFindMatchedReg(){
        String[] array = new String[]{"hello1","hello2.*","*.hello3","#.hello4","hello5.#","#.hello6.#","hello7.#.world","*.#.hello8","#.*.hello9"};
        List<String> alist = new ArrayList<String>();
        for(String src:array){
            alist.add(src);
        }
        Map<String,String> map = ssm.getRegMap(alist);
        Assert.assertEquals("hello2.*", ssm.routingMatch(map, "hello2.clive"));
        Assert.assertEquals(null, ssm.routingMatch(map, "hello2.clive.h"));
        Assert.assertEquals("*.hello3", ssm.routingMatch(map, "clive.hello3"));
        Assert.assertEquals(null, ssm.routingMatch(map, "a.clive.hello3"));
        Assert.assertEquals("#.hello4", ssm.routingMatch(map, "hello4"));
        Assert.assertEquals("#.hello4", ssm.routingMatch(map, "hello.hello4"));
        Assert.assertEquals("#.hello4", ssm.routingMatch(map, "hell.o.hello4"));
        Assert.assertEquals("hello5.#", ssm.routingMatch(map, "hello5"));
        Assert.assertEquals("hello5.#", ssm.routingMatch(map, "hello5.hello"));
        Assert.assertEquals("hello5.#", ssm.routingMatch(map, "hello5.he.llo"));
        Assert.assertEquals("#.hello6.#", ssm.routingMatch(map, "hello6"));
        Assert.assertEquals("#.hello6.#", ssm.routingMatch(map, "what.hello6"));
        Assert.assertEquals("#.hello6.#", ssm.routingMatch(map, "wh.at.hello6.he.llo"));
        Assert.assertEquals("#.hello6.#", ssm.routingMatch(map, "hello6.he.llo"));
        Assert.assertEquals("hello7.#.world", ssm.routingMatch(map, "hello7.world"));
        Assert.assertEquals("hello7.#.world", ssm.routingMatch(map, "hello7.a.world"));
        Assert.assertEquals("hello7.#.world", ssm.routingMatch(map, "hello7.a.b.world"));
        Assert.assertEquals("*.#.hello8", ssm.routingMatch(map, "what.hello8"));
        Assert.assertEquals("*.#.hello8", ssm.routingMatch(map, "what.a.hello8"));
        Assert.assertEquals("*.#.hello8", ssm.routingMatch(map, "what.ab.cd.hello8"));
        Assert.assertEquals("#.*.hello9", ssm.routingMatch(map, "what.hello9"));
        Assert.assertEquals("#.*.hello9", ssm.routingMatch(map, "what.e.e.e.e.hello9"));
    }

}
