package com.koyakal.common.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class DatabasePropertiesImpl extends CommonDataAccess  implements DatabaseProperties {


	@Override
	public Map<String, String> loadProperties() throws Exception {
		Map<String, String> properties = new HashMap<String, String>();

		String qry = "select prop_key, prop_value from koyakalproperties;";
		setQuery(qry);

		List<Map<String, String>> resultList = null;
		try {
			connection = AppDataSource.getConnection();
			resultList = getSimpleResult();
		}catch(Exception ex) {
			log.error("Exception occurred while fetching the properties: ", ex);
		}finally{
			closeOpenConnection(connection);
		}

		if(resultList != null && !resultList.isEmpty()){
			for(Map<String, String> each : resultList){
				properties.put(each.get("prop_key"), each.get("prop_value"));
			}
		}

		return properties;
	}

	@Override
	public String getQuery() {
		return query;
	}

	@Override
	public Map<String, String> getQueryParams() {
		return null;
	}

	private void setQuery(String query) {
		this.query = query;
	}

	private String query;
	
	private static final Logger log = Logger.getLogger(DatabasePropertiesImpl.class);
}

