package com.productcatalogue.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan(basePackages = { "com.productcatalogue", "com.productcatalogue.products.*" }, 
excludeFilters = { @Filter(type = FilterType.ANNOTATION,
value = EnableWebMvc.class) })
@PropertySources({@PropertySource("classpath:config.properties"),@PropertySource("classpath:productsql.properties")})
public class RootConfig {
	

}
