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

public enum TaskStatus {

	WAITING, PROCESSING, SUCCESS, FAILED;

	public String toString() {
		return this.name().toLowerCase();
	}

	public static TaskStatus build(String str) {
		if (str == null)
			return null;
		switch (str.toLowerCase()) {
		case "waiting":
			return WAITING;
		case "processing":
			return PROCESSING;
		case "success":
			return SUCCESS;
		case "failed":
			return FAILED;
		default:
			return null;
		}
	}

}
