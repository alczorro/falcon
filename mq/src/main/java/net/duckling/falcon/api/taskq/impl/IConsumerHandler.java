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

/**
 * @title: IConsumerHandler.java
 * @package net.duckling.falcon.api.taskq.impl
 * @description: 消费者对于任务实例的处理类
 * @author clive
 * @date 2014-6-22 下午7:44:00
 */
public interface IConsumerHandler<T> {
    /**
     * @description 消耗一个task实例
     * @param task
     */
    public void handle(T task);
}
