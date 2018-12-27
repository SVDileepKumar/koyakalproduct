package com.product.metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class ProductPVWcountImpl implements ProductPVWcountDao {

	@Autowired
	private Environment env;
	
	private NamedParameterJdbcTemplate productTemplate;
	
	public ProductPVWcountImpl(DataSource productDataSource) {
		this.productTemplate=new NamedParameterJdbcTemplate(productDataSource);
	}
	
	@Override
	public List<HashMap<String, Object>> getPVWcount() {
		
		try {
			String query = env.getProperty("overallPVWcount");
			HashMap<String,String>hmap=new HashMap<String,String>();
			List<Map<String, Object>> resultset = productTemplate.queryForList(query, hmap);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<HashMap<String, Object>> getPVWcount(int fromIndex, int toIndex) {

		return null;
	}

	@Override
	public List<HashMap<String, Object>> getPVWcount(List<String> productIds) {

		return null;
	}

	@Override
	public List<HashMap<String, Object>> getPVWcount(String productId) {

		return null;
	}

}
