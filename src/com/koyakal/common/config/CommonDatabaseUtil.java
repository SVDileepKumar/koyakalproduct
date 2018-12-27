package com.koyakal.common.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class CommonDatabaseUtil {

	private static int activeQryCount;

	private static int failedQryCount;

	/**
	 * Global method for all the Database calls - returns List<Map<String,String>>
	 * @param query
	 * @param conn null defaults to HeyMathDataSource.getConnection()
	 * @return
	 */
	public static List<Map<String,String>> getSimpleResult(String query, Connection conn){

		Map<String, String> resultMap = null;
		List<Map<String,String>> resultList = null;

		//Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try{
			setActiveQueryCount(true);

			if(conn == null){
				LOGGER.error("DatabaseUtil | No Connection provided for the query : < " + query + " >");
				return null;
			}

			ps = conn.prepareStatement(query);
			res = ps.executeQuery();

			if(res != null){
				resultList = new ArrayList<Map<String,String>>();

				//Get ResultSet Column Names
				int colCount = res.getMetaData().getColumnCount();
				String[] colNames = new String[colCount];
				for(int i=0,c=1; i<colCount;i++,c++){
					colNames[i]=res.getMetaData().getColumnLabel(c);
				}

				while(res.next()){
					resultMap = new HashMap<String,String>();
					for(String colName:colNames){
						resultMap.put(colName, res.getString(colName));
					}

					//Add the map to the list
					resultList.add(resultMap);
				}
			}

			setActiveQueryCount(false);

		}catch(Exception ex){
			LOGGER.error("DatabaseUtil | getSimpleResult | **Exception occurred** while fetching data for the query : < " + query + " >");
			ex.printStackTrace();
			setFailedQueryCount();
			//in case of exception
			setActiveQueryCount(false);
		}finally{
			closeAll(conn, ps, res);	
		}

		return resultList;
	}

	/**
	 * Global method for all the Database calls - returns number of rows inserted
	 * THis method is different from doInsert() as this also returns the metadata that got inserted
	 * @param query
	 * @param conn null defaults to HeyMathDataSource.getConnection()
	 * @return
	 */
	public static List<Map<String,Object>> getMetaOnInsertUpdate(String query, Connection conn){

		List<Map<String, Object>> resultMetaList = null;
		Map<String, Object> resultMetaMap = null;

		int numberOfRows = 0;

		//	Connection conn = null;
		PreparedStatement ps = null;
		try{
			setActiveQueryCount(true);

			if(conn == null){
				LOGGER.error("DatabaseUtil | No Connection provided for the query : < " + query + " >");
				return null;
			}
			ps = conn.prepareStatement(query);
			numberOfRows = ps.executeUpdate();
			if(numberOfRows > 0){
				resultMetaList = new ArrayList<Map<String, Object>>();
				ResultSet res = ps.getGeneratedKeys();

				int colCount = res.getMetaData().getColumnCount();
				String[] colNames = new String[colCount];
				for(int i=0,c=1; i<colCount;i++,c++){
					colNames[i]=res.getMetaData().getColumnLabel(c);
				}

				while(res.next()){
					resultMetaMap = new HashMap<String, Object>();

					//Get ResultSet Column Names
					for(String colName:colNames){
						resultMetaMap.put(colName, res.getString(colName));
					}

					//Add the map to the list
					resultMetaList.add(resultMetaMap);
				}
			}

			setActiveQueryCount(false);

		}catch(Exception ex){
			LOGGER.error("DatabaseUtil | getMetaOnInsertUpdate | **Exception occurred** while fetching data for the query : < " + query + " >");
			ex.printStackTrace();
			setFailedQueryCount();
			//in case of exception
			setActiveQueryCount(false);
		}finally{
			closeAll(conn, ps, null);	
		}

		return resultMetaList;
	}

	/**
	 * Global method for all the Database calls - returns number of rows inserted
	 * @param query
	 * @param conn null defaults to HeyMathDataSource.getConnection()
	 * @return
	 */
	public static int doInsert(String query, Connection conn){

		//LOGGER.info("DatabaseUtil | doInsert | New Query for execution... Query-Tag: < " + qTag + " > ");
		int numberOfRows = 0;

		//Connection conn = null;
		PreparedStatement ps = null;
		try{
			setActiveQueryCount(true);

			if(conn == null){
				LOGGER.error("DatabaseUtil | No Connection provided for the query : < " + query + " >");
				return 0;
			}
			ps = conn.prepareStatement(query);
			numberOfRows = ps.executeUpdate();

			setActiveQueryCount(false);

		}catch(Exception ex){
			LOGGER.error("DatabaseUtil | doInsert | **Exception occurred** while fetching data for the query : < " + query + " >");
			ex.printStackTrace();
			setFailedQueryCount();
			//in case of exception
			setActiveQueryCount(false);
		}finally{
			closeAll(conn, ps, null);	
		}

		return numberOfRows;
	}

	/**
	 * Global method for all the Database calls - returns number of rows updated
	 * @param query
	 * @param conn null defaults to HeyMathDataSource.getConnection()
	 * @return
	 */
	public static int doUpdate(String query, Connection conn){

		//LOGGER.info("DatabaseUtil | doUpdate | New Query for execution... Query-Tag: < " + qTag + " > ");
		int numberOfRows = 0;

		//Connection conn = null;
		PreparedStatement ps = null;
		try{
			setActiveQueryCount(true);

			if(conn == null){
				LOGGER.error("DatabaseUtil | No Connection provided for the query : < " + query + " >");
				return 0;
			}
			ps = conn.prepareStatement(query);
			numberOfRows = ps.executeUpdate();

			setActiveQueryCount(false);

		}catch(Exception ex){
			LOGGER.error("DatabaseUtil | doUpdate | **Exception occurred** while fetching data for the query : < " + query + " >");
			ex.printStackTrace();
			setFailedQueryCount();
			//in case of exception
			setActiveQueryCount(false);
		}finally{
			closeAll(conn, ps, null);	
		}

		return numberOfRows;
	}

	/**
	 * Global method for all the Database calls - returns List<Map<String,Object>>
	 * @param query
	 * @param conn null defaults to HeyMathDataSource.getConnection()
	 * @return
	 */
	public static List<Map<String,Object>> getResult(String query, Connection conn){

		//LOGGER.info("DatabaseUtil | getResult | New Query for execution... Query-Tag: < " + qTag + " > ");

		Map<String, Object> resultMap = null;
		List<Map<String,Object>> resultList = null;

		//	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		try{
			setActiveQueryCount(true);

			if(conn == null){
				LOGGER.error("DatabaseUtil | No Connection provided for the query : < " + query + " >");
				return null;
			}
			ps = conn.prepareStatement(query);
			res = ps.executeQuery();

			if(res != null){
				resultList = new ArrayList<Map<String,Object>>();

				//Get ResultSet Column Names
				int colCount = res.getMetaData().getColumnCount();
				String[] colNames = new String[colCount];
				for(int i=0,c=1; i<colCount;i++,c++){
					colNames[i]=res.getMetaData().getColumnLabel(c);
				}

				while(res.next()){
					resultMap = new HashMap<String,Object>();
					for(String colName:colNames){
						resultMap.put(colName, res.getString(colName));
					}

					//Add the map to the list
					resultList.add(resultMap);
				}
			}

			setActiveQueryCount(false);

		}catch(Exception ex){
			LOGGER.error("DatabaseUtil | getResult | **Exception occurred** while fetching data for the Query-Tag: < " + query + " > | and the query : < " + query + " >"); 
			ex.printStackTrace();
			setFailedQueryCount();
			//in case of exception
			setActiveQueryCount(false);
		}finally{
			closeAll(conn, ps, res);	
		}

		return resultList;
	}

	/***
	 * Method to close all the connections
	 * 
	 * @param conn
	 * @param ps
	 * @param res
	 */
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

	/**
	 * Gives the Count of Active Queries
	 * 
	 * @return
	 */
	public static synchronized int getActiveQueryCount(){

		return activeQryCount;
	}

	/**
	 * Gives the Count of Failed Queries
	 * 
	 * @return
	 */
	public static synchronized int getFailedQueryCount(){

		return failedQryCount;
	}

	private static synchronized void setActiveQueryCount(boolean flag){
		if(flag){
			activeQryCount ++;	
		}else{
			activeQryCount --;
		}
	}

	private static synchronized void setFailedQueryCount(){
		failedQryCount ++;
	}

	private static final Logger LOGGER = Logger.getLogger(CommonDatabaseUtil.class);
}
