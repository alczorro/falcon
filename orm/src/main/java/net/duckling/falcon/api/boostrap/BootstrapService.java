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

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 系统初始化服务，负责在初始化时创建数据库
 * 	<bean id="createDataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource"
		destroy-method="close">
		<property name="driverClass" value="${c3p0.driverClass}" />
		<property name="jdbcUrl" value="${c3p0.create.url}" />
		<property name="user" value="${c3p0.username}" />
		<property name="password" value="${c3p0.password}" />
		<property name="initialPoolSize" value="${c3p0.initialPoolSize}" />
		<property name="maxPoolSize" value="${c3p0.maxPoolSize}" />
		<property name="minPoolSize" value="${c3p0.minPoolSize}" />
		<property name="acquireIncrement" value="${c3p0.acquireIncrement}" />
		<property name="maxStatements" value="${c3p0.maxStatements}" />
		<property name="preferredTestQuery" value="${c3p0.preferredTestQuery}" />
		<property name="maxConnectionAge" value="${c3p0.maxConnectionAge}" />
		<property name="testConnectionOnCheckout" value="${c3p0.testConnectionOnCheckout}" />
	</bean>
	<bean id="boostrapService" class="net.duckling.falcon.api.boostrap.BootstrapService" init-method="bootstrap">
		<constructor-arg><ref bean="createDataSource"/></constructor-arg>
		<constructor-arg><value>${database}</value></constructor-arg>
		<constructor-arg><value>product</value></constructor-arg>
		<constructor-arg><value>classpath:probe.sql</value></constructor-arg>
	</bean>
 * @author xiejj@cnic.cn
 *
 */
public class BootstrapService {
	private BootstrapDao bootstrapDao;
	public BootstrapService(ComboPooledDataSource pool, String database,String checkTable, String sqlFile){
		bootstrapDao = new BootstrapDao();
		bootstrapDao.setCreateDataSource(pool);
		bootstrapDao.setDatabase(database);
		bootstrapDao.setSqlPath(sqlFile);
		bootstrapDao.setCheckTable(checkTable);
	}
	/**
	 * 判断数据库是否存在
	 * @return
	 */
	public boolean isDatabaseExists(){
		return bootstrapDao.isDatabaseExisted();
	}
	/**
	 * 删除数据库
	 */
	public void dropDatabase(){
		bootstrapDao.dropDatabase();
	}
	/**
	 * 检查并创建数据库
	 */
	public void bootstrap() {
		if (!bootstrapDao.isDatabaseExisted()) {
			bootstrapDao.createDatabase();
		}
		if (!bootstrapDao.isTableExisted()) {
			bootstrapDao.createTables();
		}
		bootstrapDao.close();
	}
}
