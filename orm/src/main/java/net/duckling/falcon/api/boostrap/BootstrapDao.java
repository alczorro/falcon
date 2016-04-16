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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.util.ResourceUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class BootstrapDao {

	private static final Logger LOG = Logger.getLogger(BootstrapDao.class);
	private String checkTable;
	private ComboPooledDataSource createDataSource;

	private String database;
	private String sqlPath;

	private void closeAll(Connection conn, Statement stmt, ResultSet rs)
			throws SQLException {
		if (rs!=null){
			rs.close();
		}
		if (stmt!=null){
			stmt.close();
		}
		if (conn!=null){
			conn.close();
		}
	}

	private void execute(Connection conn, String sql) {
		Statement statement=null;
		try {
			statement = conn.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			throw new WrongSQLException(e.getMessage());
		} finally{
			if (statement!=null){
				try {
					statement.close();
				} catch (SQLException e) {
					throw new WrongSQLException(e.getMessage());
				}
			}
		}
	}
	private void execute(String sql) {
		Connection conn = getConnection();
		execute(conn, sql);
	}

	private Connection getConnection() {
		try {
			return createDataSource.getConnection();
		} catch (SQLException e) {
			throw new WrongSQLException(e.getMessage());
		}
	}

	/**
	 * 关闭创建数据库连接池
	 */
	public void close() {
		createDataSource.close();
	}

	public void createDatabase() {
		execute("CREATE database IF NOT EXISTS " + database
				+ " CHARACTER SET utf8");
	}

	public void createTables() {
		Connection conn = getConnection();
		execute(conn,"use " + database + ";");
		File file = null;
		SQLReader reader = null;
		try {
			file = ResourceUtils.getFile(sqlPath);
			if (file.exists()) {
				reader = new SQLReader(new FileInputStream(file), "UTF-8");
				String sql;
				while ((sql = reader.next()) != null) {
					execute(conn, sql);
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error("Init sql file is not found", e);
		} catch (UnsupportedEncodingException e) {
			LOG.error("Unsupported encode for UTF-8", e);
		} catch (DataAccessException e) {
			LOG.error("Data access exception", e);
		} catch (WrongSQLException e) {
			LOG.error("Init SQL has problem", e);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	public void dropDatabase() {
		execute("drop database " + database);
	}

	public boolean isDatabaseExisted() {
		Connection conn = getConnection();
		String sql = "show databases";
		Statement stmt=null;
		ResultSet rs=null;
		ArrayList<String> results=new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()){
				results.add(rs.getString(1));
			}
		} catch (SQLException e) {
			throw new WrongSQLException(e.getMessage());
		}finally{
			try {
				closeAll(conn, stmt, rs);
			} catch (SQLException e) {
				throw new WrongSQLException(e.getMessage());
			}
		}
		return results.contains(database);
	}

	public boolean isTableExisted() {
		Connection conn = getConnection();
		execute(conn,"use " + database + ";");
		Statement stmt=null;
		ResultSet rs=null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("show tables like '" + this.checkTable + "'");
			return rs.next();
		} catch (SQLException e) {
			throw new WrongSQLException(e.getMessage());
		}finally{
			try {
				closeAll(conn, stmt, rs);
			} catch (SQLException e) {
				throw new WrongSQLException(e.getMessage());
			}
		}
	}

	public void setCheckTable(String checkTable) {
		this.checkTable = checkTable;
	}

	public void setCreateDataSource(ComboPooledDataSource createDataSource) {
		this.createDataSource = createDataSource;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public void setSqlPath(String sqlPath) {
		this.sqlPath = sqlPath;
	}
}
