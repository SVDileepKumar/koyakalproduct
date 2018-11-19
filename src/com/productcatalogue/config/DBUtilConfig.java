package com.productcatalogue.config;

import javax.naming.NamingException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;


@Configuration
@ComponentScan("com.productcatalogue")
public class DBUtilConfig {
	@Bean
	public DataSource productDataSource() {
		DataSource dataSource = null;
		JndiTemplate jndi = new JndiTemplate();
		try {
			dataSource = jndi.lookup("java:/comp/env/jndi/koyakal",
					DataSource.class);
		} catch (NamingException e) {
			log.error("Exception occurred : ",e);
			e.printStackTrace();
		}
		return dataSource;

	}
	private static final Logger log = Logger.getLogger(DBUtilConfig.class);
}
