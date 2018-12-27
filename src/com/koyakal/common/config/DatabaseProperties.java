package com.koyakal.common.config;

import java.util.Map;

public interface DatabaseProperties {
	public Map<String,String> loadProperties() throws Exception;
}
