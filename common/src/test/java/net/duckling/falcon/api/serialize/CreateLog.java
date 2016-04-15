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

public class CreateLog {

    private String name;
    private int id;
    private Point point;
    private Date time;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public CreateLog(int id, String name, int x, int y) {
        this.name = name;
        this.id = id;
        this.point = new Point(x, y);
    }

    public CreateLog(int id, String name, int x, int y, Date time) {
        this.name = name;
        this.id = id;
        this.point = new Point(x, y);
        this.time = time;
    }

    public String toString() {
        return this.name + "," + this.id + "," + this.point.getX() + "," + this.point.getY() + "," + this.getTime();
    }

    public CreateLog() {
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
