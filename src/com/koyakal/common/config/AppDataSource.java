package com.koyakal.common.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AppDataSource {


	private static DataSource connectionPool;
	static Context initContext = null;

	public static void initDataSource() {

		if(connectionPool != null){
			return ;
		}
		synchronized (AppDataSource.class) {
			try {
				initContext = new InitialContext();
				connectionPool = (DataSource) initContext.lookup("java:/comp/env/jndi/appdb");
			} catch (NamingException e) {
				System.out.print("Exception while creating the datasource connection pool"+e.toString());
			}
		}
	}

	public static Connection getConnection() throws Exception {
		initDataSource();
		return connectionPool.getConnection();
	}

	public static void closeContext(){
		try {
			initContext.close();
		} catch (NamingException e) {
			System.out.print("Exception while closing the context"+e.toString());
		}
	}

	public static void closeConnection(Connection connection){
		StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[1];
		try {
			if(connection != null){
				connection.close();
				connection = null;
			}
		} catch (Exception ex){
			System.out.print("Exception while closing the connection"+ex.toString());
		}
	}

	public static void closeAll(Connection conn, PreparedStatement ps, ResultSet res){

		try{
			if(res != null) {
				res.close();
				res = null;
			}

		}catch(Exception e){
			res = null;
			System.out.println("Exception occurred while closing Resultset :: | :: " + e.getMessage() );
		}
		try{
			if(ps != null) {
				ps.close();
				ps = null;
			}
		}catch(Exception e){
			ps = null;
			System.out.println("Exception occurred while closing PreparedStatement :: | :: " + e.getMessage() );
		}
		try{
			if(conn != null) {
				conn.close();
				conn = null;
			}

		}catch(Exception e){
			conn = null;
			System.out.println("Exception occurred while closing Connection :: | :: " + e.getMessage() );
		}

	}

}

