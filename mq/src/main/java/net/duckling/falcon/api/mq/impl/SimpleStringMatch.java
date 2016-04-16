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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class SimpleStringMatch {

    public String routingMatch(Map<String, String> regMap, String value) {
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        if (regMap.keySet().contains(value)) {
            return value;
        }
        String matchedReg = null;
        for (Map.Entry<String, String> entry : regMap.entrySet()) {
            Pattern m = Pattern.compile(entry.getValue());
            Matcher matcher = m.matcher(value);
            while (matcher.find()) {
                if (matcher.start() == 0 && (matcher.end() == value.length())) {
                    matchedReg = entry.getKey();
                    return matchedReg;
                }
            }
        }
        return null;
    }

    public String convertToRex(String src) {
        if (src == null) {
            return null;
        }
        if (!src.contains("#") && !src.contains("*")) {
            return src.replace(".", "\\.");
        }
        if (!src.contains("#")) {
            return src.replace("*", "\\w+").replace(".", "\\.");
        }
        return src.replace("*", "\\w+").replace(".", "\\.").replace("#\\.", "(\\w+\\.)*").replace("\\.#", "(\\.\\w+)*");
    }

    public Map<String, String> getRegMap(List<String> array) {
        Map<String, String> map = new HashMap<String, String>();
        if (array != null) {
            for (String src : array) {
                map.put(src, convertToRex(src));
            }
        }
        return map;
    }

}
