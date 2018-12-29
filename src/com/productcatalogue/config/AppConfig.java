package com.productcatalogue.config;

import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.koyakal.common.config.JsonConfigReader;


public class AppConfig {

	private static Map<String,Object> configMap; 

	private AppConfig(){

		Context appContext = null;
		try{
			appContext = new InitialContext();
			String appConfigFilePath = (String) appContext.lookup("java:comp/env/appConfigFilePath");
			

			if(appConfigFilePath != null ){
				configMap = JsonConfigReader.readFile(appConfigFilePath);
			}
			System.out.println("AppConfig :: "+ configMap);
		}catch(Exception e){
			System.out.println("Exception occurred in AppConfig: "+e.getMessage());
			e.printStackTrace();
		}finally{
			if(appContext != null){
				try {
					appContext.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
				appContext = null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static String getConfig(String appName, String key){

		try{
			String propValue = null;

			if(configMap == null){
				synchronized(AppConfig.class){
					if(configMap == null){
						new AppConfig();
					}
				}
			}

			if(key != null && configMap.containsKey(appName)){
				Map<String, String> eachConfigMap =  (Map<String, String>) configMap.get(appName);
				propValue = eachConfigMap.get(key);
			}

			return propValue;
		}catch(Exception ex){
			return "";
		}
	}


}
