package com.productcatalogue.listing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service("ProductListing")
public class ProductListingImpl implements ProductListingDao {

	private NamedParameterJdbcTemplate productTemplate;
	
	@Autowired
	private Environment env;
	
	public ProductListingImpl(DataSource productDataSource) {
		this.productTemplate = new NamedParameterJdbcTemplate(productDataSource);
	}
	
	public List<Map<String, Object>> getProductList(){
		try {
			String query = env.getProperty("productlist");
			Map<String, Object> param= new HashMap<String, Object>();
			return productTemplate.queryForList(query, param);
		}catch(Exception e) {
			log.error("Error in fetching products list -->", e);
		}
		return null;
		
	}
	private static final Logger log = Logger.getLogger(ProductListingImpl.class);
	
}
