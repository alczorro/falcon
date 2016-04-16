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
package net.duckling.falcon.api.boostrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
/**
 * SQL文件读取类
 * @author xiejj@cnic.cn
 *
 */
public class SQLReader {
	private static final String ENCODE_UTF8 = "UTF-8";

	private String delimeter = ";";

	private static final Logger LOG = Logger.getLogger(SQLReader.class);

	private BufferedReader reader;
	/**
	 * 构造函数
	 * @param reader SQL文件的reader对象
	 */
	public SQLReader(BufferedReader reader) {
		this.reader = reader;
	}
	/**
	 * 构造函数
	 * @param in		SQL文件输入流
	 * @param encode	文件流的编码
	 * @throws UnsupportedEncodingException	如果文件流编码不支持抛出该异常
	 */
	public SQLReader(InputStream in, String encode)
			throws UnsupportedEncodingException {
		String charEncode = encode;
		if (charEncode == null) {
			charEncode = ENCODE_UTF8;
		}
		reader = new BufferedReader(new InputStreamReader(in, charEncode));
	}
	/**
	 * 构造函数
	 * @param reader	SQL文件的reader对象
	 */
	public SQLReader(Reader reader) {
		this.reader = new BufferedReader(reader);
	}

	private void changeDelimiter(String line) {
		delimeter = parseDelimiter(line);
		if (StringUtils.isEmpty(delimeter)) {
			LOG.error("Wrong format SQL");
			throw new WrongSQLException("delimiter is empty:" + line);
		}
	}

	private boolean isClosed() {
		return reader == null;
	}

	private boolean isComment(String line) {
		return line.startsWith("/*");
	}

	private boolean isDelimiter(String line) {
		if (line != null) {
			String ignored = line.toLowerCase();
			return ignored.startsWith("delimiter");
		}
		return false;
	}

	private String parseDelimiter(String line) {
		return line.substring("delimiter".length()).trim();
	}
	/**
	 *  关闭文件流
	 */
	public void close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				LOG.error("Exception has happended while reading initialize SQL.");
				LOG.error(e.getMessage());
				LOG.debug("", e);
			}
			reader = null;
		}
	}
	/**
	 * 读取下一条SQL
	 * @return	下一条完整的SQL，如果没有SQL返回null
	 */
	public String next() {
		if (!isClosed()) {
			StringBuilder buffer = new StringBuilder();

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (isDelimiter(line)) {
						changeDelimiter(line);
					} else if (!isComment(line)) {
						if (line.endsWith(delimeter)) {
							line = line.substring(0,
									line.length() - delimeter.length());
							buffer.append(line);
							buffer.append("\n");
							return StringUtils.trimToNull(buffer.toString());
						} else {
							buffer.append(line);
							buffer.append("\n");
						}
					}
				}
			} catch (IOException e) {
				LOG.error(e.getMessage());
				LOG.debug("", e);
				close();
			}
			if (line == null) {
				close();
			}
			return StringUtils.trimToNull(buffer.toString());
		}
		return null;
	}
}
