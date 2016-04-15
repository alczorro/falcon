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

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MessageBody {

    private int id;
    private String type;
    private CreateLog body;
    private Map<String,CreateLog> maps;
    private List<CreateLog> list;
    private Date timestamp;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, CreateLog> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, CreateLog> maps) {
        this.maps = maps;
    }

    public List<CreateLog> getList() {
        return list;
    }

    public void setList(List<CreateLog> list) {
        this.list = list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CreateLog getBody() {
        return body;
    }

    public void setBody(CreateLog body) {
        this.body = body;
    }

}
