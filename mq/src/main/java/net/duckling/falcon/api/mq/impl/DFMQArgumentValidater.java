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

import net.duckling.falcon.api.mq.DFMQMode;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DFMQArgumentValidater {

    private static Logger LOG = Logger.getLogger(DFMQArgumentValidater.class);

    public static void argumentValidate(String[] routingKey, DFMQMode mode) {
        if (routingKey == null) {
            throw new IllegalArgumentException("Error arguments number, " + mode
                    + " mode only accept one routingKey argument.");
        } else if (routingKey.length > 1) {
            LOG.warn("Arguments[1] to Arguments[" + (routingKey.length - 1) + "] will be ignored.");
        }
    }

    public static void fanoutValidate(String[] routingKey) {
        if (routingKey == null || routingKey.length == 0) {
            return;
        }
        if (routingKey.length == 1 && StringUtils.isEmpty(routingKey[0])) {
            return;
        }
        LOG.warn("In fanout mode, all arguments of routingKey will be ignored.");
    }
}
