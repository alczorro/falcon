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

import static org.junit.Assert.assertFalse;

import java.beans.PropertyVetoException;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class BootstrapServiceTest {
    private static final String MYSQL_CONN_URL = "jdbc:mysql://%s?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
    private static ComboPooledDataSource makeMysqlPool(String host, String user, String password) throws PropertyVetoException {
	ComboPooledDataSource comboDataSource = new ComboPooledDataSource();
	comboDataSource.setDriverClass("com.mysql.jdbc.Driver");
	comboDataSource.setJdbcUrl(String.format(MYSQL_CONN_URL, host));
	comboDataSource.setUser(user);
	comboDataSource.setPassword(password);
	comboDataSource.setMinPoolSize(10);
	return comboDataSource;
    }

    @Test
    public void testBootstrap() throws PropertyVetoException {
	String host = System.getenv("JUNIT_ORM_DB_HOST");
	String user = System.getenv("JUNIT_ORM_DB_USER");
	String pass = System.getenv("JUNIT_ORM_DB_PASS");
	if (host!=null && user!=null && pass!=null) {
	    ComboPooledDataSource pool = makeMysqlPool(host, user, pass);
	    BootstrapService bootstrapService = new BootstrapService(pool, "probe1","product", "classpath:probe.sql");
	    bootstrapService.dropDatabase();
	    assertFalse(bootstrapService.isDatabaseExists());
	    bootstrapService.bootstrap();
	    return;
	} else {
	    System.out.println("Please set env for JUNIT_ORM_DB_HOST, JUNIT_ORM_DB_USER and JUNIT_ORM_DB_PASS. Otherwise this test will be skipped.");
	    System.exit(0);
	}
    }
}
