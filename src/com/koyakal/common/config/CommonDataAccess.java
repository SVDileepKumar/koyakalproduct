package com.koyakal.common.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public abstract class CommonDataAccess {

	/**
	 * Expects datasource other than HeyMathDataSource to be returned.
	 * This enables CommonDataAccess to use multi-datasources when required.
	 */
	protected Connection connection; 
	
	/**
	 * Expects RAW query either with / without Substitued parameters.
	 * if Parameters not substituted, QueryParams map needs to be populated with parameters
	 * 
	 * To implemented in child classes 
	 * @return
	 */
	public abstract String getQuery();

	/**
	 * Expects the Parameters to be substituted in the query.
	 * For Example: if the query is select PROP_KEY,PROP_VALUE from hm_app_properties where prop_key='{myvalue}',
	 * the QueryParams Map shall contain key-value pair as {"{myvalue}"=corelocation}.
	 * Same applicable for values and filters. 
	 * 
	 * @return
	 */
	public abstract Map<String,String> getQueryParams();

	/**
	 * ResultList will contain the datatype as Map<String,String>
	 * This method executes the query after substitution of values.
	 * 
	 * @return List<Map<String,String>>
	 */
	protected final List<Map<String,String>> getSimpleResult(){

		List<Map<String,String>> resultList = null;

		String query = getBaseQuery();

		if(StringUtils.isEmpty(query)){
			LOGGER.error("CommonDataAccess | **Possible Error** QUERY CANNOT be null or empty: " + query);
			return resultList; 
		}

		try{
			resultList = CommonDatabaseUtil.getSimpleResult(query, connection);
		}catch(Exception ex){
			LOGGER.error("CommonDataAccess | **Exception Occurred** while executing the query : " + query);
			ex.printStackTrace();
		}finally{
			closeOpenConnection(connection);
		}

		return resultList;
	};
	
	/**
	 * Returns the number of rows inserted
	 * This method executes the query after substitution of values.
	 * 
	 * @return int
	 */
	protected final int doInsert(){

		int numberOfRows = 0;

		String query = getBaseQuery();

		if(StringUtils.isEmpty(query)){
			LOGGER.error("CommonDataAccess | **Possible Error** QUERY CANNOT be null or empty: " + query);
			return numberOfRows; 
		}

		try{
			numberOfRows = CommonDatabaseUtil.doInsert(query, connection);
		}catch(Exception ex){
			LOGGER.error("CommonDataAccess | **Exception Occurred** while executing the query : " + query);
			ex.printStackTrace();
		}finally{
			closeOpenConnection(connection);
		}

		return numberOfRows;
	};
	
		/**
	 * Returns the number of rows inserted
	 * This method executes the query after substitution of values.
	 * 
	 * @return int
	 */
	protected final List<Map<String, Object>> getMetaOnInsertUpdate(){

		List<Map<String, Object>> resultMetaList = null;
		String query = getBaseQuery();

		if(StringUtils.isEmpty(query)){
			LOGGER.error("CommonDataAccess | **Possible Error** QUERY CANNOT be null or empty: " + query);
			return null; 
		}

		try{
			resultMetaList = CommonDatabaseUtil.getMetaOnInsertUpdate(query, connection);
		}catch(Exception ex){
			LOGGER.error("CommonDataAccess | **Exception Occurred** while executing the query : " + query);
			ex.printStackTrace();
		}finally{
			closeOpenConnection(connection);
		}

		return resultMetaList;
	};
	
	
	
	/**
	 * Returns the number of rows updated
	 * This method executes the query after substitution of values.
	 * 
	 * @return int
	 */
	protected final int doUpdate(){

		int numberOfRows = 0;

		String query = getBaseQuery();

		if(StringUtils.isEmpty(query)){
			LOGGER.error("CommonDataAccess | **Possible Error** QUERY CANNOT be null or empty: " + query);
			return numberOfRows; 
		}

		try{
			numberOfRows = CommonDatabaseUtil.doUpdate(query, connection);
		}catch(Exception ex){
			LOGGER.error("CommonDataAccess | **Exception Occurred** while executing the query : " + query);
			ex.printStackTrace();
		}finally{
			closeOpenConnection(connection);
		}

		return numberOfRows;
	};

	/**
	 * ResultList will contain the datatype as Map<String,Object>
	 * This method executes the query after substitution of values.
	 * 
	 * @return List<Map<String,Object>>
	 */
	protected final List<Map<String,Object>> getResult(){

		List<Map<String,Object>> resultList = null;

		String query = getBaseQuery();

		if(StringUtils.isEmpty(query)){
			LOGGER.error("CommonDataAccess | **Possible Error** QUERY CANNOT be null or empty: " + query);
			return resultList;
		}

		try{
			resultList = CommonDatabaseUtil.getResult(query, connection);
		}catch(Exception ex){
			LOGGER.error("CommonDataAccess | **Exception Occurred** while executing the query : " + query);
			ex.printStackTrace();
		}finally{
			closeOpenConnection(connection);
		}

		return resultList;
	};

	/**
	 * In order to find the number of Active Queries
	 * 
	 * @return
	 */
	public int getActiveQryCount(){
		return CommonDatabaseUtil.getActiveQueryCount();
	}

	/**
	 * In order to find the number of Failed Queries
	 * 
	 * @return
	 */
	public int getFailedQryCount(){
		return CommonDatabaseUtil.getFailedQueryCount();
	}

	/**
	 * common method to get the base query
	 * 
	 * @return
	 */
	private String getBaseQuery(){

		return getQueryWithParams(getQuery(), getQueryParams());
	}

	/**
	 * Commom method to get the query with params
	 * @param query
	 * @param paramsMap
	 * @return
	 */
	private String getQueryWithParams(String query, Map<String,String> paramsMap){

		//basic null check to avoid nullpointer
		if(query != null && paramsMap != null){
			for(Map.Entry<String, String> each: paramsMap.entrySet()){
				query = query.replace(each.getKey(), each.getValue());
			}
		}

		return query;
	}
	
	/***
	 * Close the active connection on exception
	 * 
	 * @param connection
	 */
	protected final void closeOpenConnection(Connection connection){
		try {
			if(connection != null && !connection.isClosed()){
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			LOGGER.error("Exception occurred while trying to close the open connection");
			e.printStackTrace();
		}
	}
	
	private static final Logger LOGGER = Logger.getLogger(CommonDataAccess.class);

}

