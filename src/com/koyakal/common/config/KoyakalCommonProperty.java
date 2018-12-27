package com.koyakal.common.config;

import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;


public class KoyakalCommonProperty {

	private static Map<String, String> propMap = null;
	
	private KoyakalCommonProperty() throws Exception
	{
		Context appContext = null;
		try{
			
			appContext = new InitialContext();
			String databaseImplClassName = (String) appContext.lookup("java:comp/env/databaseProperties");
			
			if(databaseImplClassName != null ){
				DatabaseProperties databaseProperties = (DatabaseProperties) Class.forName(databaseImplClassName).newInstance();
				propMap = databaseProperties.loadProperties();
			}
			log.info("Database properties loaded in HmCommonProperty | :: "+ propMap);
		}catch(Exception e){
			log.error("Exception occurred while loading properties in HmCommonProperty :: "+e.getMessage());
			e.printStackTrace();
		}finally{
			if(appContext != null){
				appContext.close();
				appContext = null;
			}
		}
	}
	
	public static String getProperty(String key)
	{
		try {
			/**
			 * Properties to come from database
			 */
			if(propMap == null){
				new KoyakalCommonProperty();
			}
			
			/**
			 * Null check for the key
			 */
			if(key != null && propMap.containsKey(key)){
				return propMap.get(key);
			}else{
				return "";
			}
		}catch(Exception e) {
			log.error("Exception occurred while getting the Property from HmCommonProperty for the key :: " + key +", Exception: " + e.getMessage());
			e.printStackTrace();
			return "";
		}
	}
		
	/***
	 * Method to refresh the already loaded properties
	 * @return
	 */
	public static boolean propRefresh(){
		
		if(propMap != null && !propMap.isEmpty()){
			propMap = null;
			log.info("Properties have been refreshed for this container");
			try {
				new KoyakalCommonProperty();
			} catch (Exception e) {}
			return true;
		}else{
			log.error("Properties have already been refreshed and hence skipping..");
		}
		
		return false;
	}
	
	private static final Logger log = Logger.getLogger(KoyakalCommonProperty.class);
}
