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
package net.duckling.falcon.xss;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.Test;

public class SanitizeServiceTest {
	@Test
	public void test() {
		SanitizeService service = new SanitizeService();
		String[] unsafes = new String[]{
				"<script type='text/javascript'>alert('hello')</script>",
				"<div class='a' href='http://www.sina.com.cn' onload='javascript:alert(\'1\')'>aa</div>",
				"<a href='javascript:alert(1)'>dd</a>"
		};
		for (String unsafe:unsafes){
			String html = service.clean(unsafe);
			assertTrue(html.indexOf("script")==-1);
			System.out.println(html);
		}
	}
	@Test
	public void testConf() throws IOException, ParseException {
		SanitizeService service = new SanitizeService("./src/resource/example.conf");
		String[] unsafes = new String[]{
				"<script type='text/javascript'>alert('hello')</script>",
				"<DIV class='a' href='http://www.sina.com.cn' onload='javascript:alert(\'1\')'>aa</DIV>",
				"<span style='width:100px'>To narrownarrownarrownarrownarrownarrow</span>",
				"<a href='ftp://ftp.cerc.cnic.cn'>dd</a>"
		};
		for (String unsafe:unsafes){
			String html = service.clean(unsafe);
			assertTrue(html.indexOf("script")==-1);
			System.out.println(html);
		}
	}
}
