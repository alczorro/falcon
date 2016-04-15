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
/**
 * 
 */
package net.duckling.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期类工具类
 * 
 * @author lvly
 * @since 2012-9-21
 */
public final class DateUtils {
	private DateUtils() {
	}

	/**
	 * 根据字符串计息出一个Date对象
	 * 
	 * @param dateString
	 *            字符串，必须以 yyyy-mm-dd hh:mm形式
	 * @return Date()
	 */
	public static Date getDate(String dateString) {
		return getDate(dateString, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 根据Date日期时间字符串和格式化字符串，返回一个Date对象
	 * 
	 * @param dateString
	 *            格式化的日期时间字符串
	 * @param formatString
	 *            日期时间格式字符串，例如 yyyy-MM-dd HH:mm:ss
	 * @return Date Date对象或null
	 */
	public static Date getDate(String dateString, String formatString) {
		if (CommonUtils.isNull(dateString)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 根据Date计息出一个String对象
	 * 
	 * @param dateString
	 *            字符串，必须以 yyyy-mm-dd hh:mm形式
	 * @return Date()
	 */
	public static String getDateStr(Date date) {
		return getDateStr(date, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 根据Date和格式化字符串，返回一个String对象
	 * 
	 * @param date
	 *            Date对象
	 * @param formatString
	 *            日期时间格式字符串，例如 yyyy-MM-dd HH:mm:ss
	 * @return String 格式化的日期时间字符串
	 */
	public static String getDateStr(Date date, String formatString) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		return sdf.format(date);
	}

}
