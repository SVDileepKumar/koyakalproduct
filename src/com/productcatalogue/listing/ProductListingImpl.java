package com.productcatalogue.listing;

import java.util.ArrayList;
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

import com.koyakal.common.config.KoyakalCommonProperty;

@Service("ProductListing")
public class ProductListingImpl implements ProductListingDao {

	private NamedParameterJdbcTemplate productTemplate;
	//pagination
	private String key_map = KoyakalCommonProperty.getProperty("pagination");
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

	public List<Map<String, Object>> getProductListSortedBy(Map<String,String> filterMap, int page) {
		try {
			
			
			Map<String, String> table= new HashMap<String, String>();
			table.put("ProductCreatedTime", "products.CreatedTime");
			table.put("ProductCategoryId", "productcategorysubcategorymap.CategoryId");
			table.put("ProductSubCategoryId", "productcategorysubcategorymap.SubCategoryId");
			table.put("ProductDefaultPrice", "productpricing.DefaultPrice");
			table.put("ProductDiscountPercentage", "productpricing.DiscountPercentage");
			table.put("ProductPurchasedCount", "productpurchasewishcount.PurchasedCount");
			table.put("ProductViewCount", "productpurchasewishcount.ViewCount");
			
			String query = env.getProperty("productlist");
			String sortQuery=query.substring(0, query.length()-1)+" order by ";
			sortQuery=table.containsKey(filterMap.get("columnName"))?sortQuery+table.get(filterMap.get("columnName")):sortQuery+"products.CreatedTime";
			Map<String, Object> param= new HashMap<String, Object>();
			int noOfProductsPerPage=Integer.parseInt(key_map);
			int fromIndex=page*noOfProductsPerPage;
			int endIndex=fromIndex+noOfProductsPerPage;
			if(filterMap.get("columnName").equals("ProductCreatedTime")||!table.containsKey(filterMap.get("columnName"))) {
				sortQuery+=" desc "+" limit "+fromIndex+", "+endIndex+";";
			}else {
				sortQuery+=" asc "+" limit "+fromIndex+", "+endIndex+";";
			}
			
			return productTemplate.queryForList(sortQuery, param);
		}catch(Exception e) {
			log.error("Error in fetching sorted products list -->", e);
		}
		return null;
	}

	
	public List<Map<String, Object>> getProducts(int[] productIdsList) {
		try {

			String query = env.getProperty("productlistofgivenids");
			Map<String, Object> param = new HashMap<String, Object>();
			List<Integer> productIds=new ArrayList<>(productIdsList.length);
			for(int i:productIdsList) {
				productIds.add(i);
			}
			param.put("productIds",productIds);
			return productTemplate.queryForList(query, param);
		} catch (Exception e) {
			log.error("Error in fetching products list of given ids -->", e);
		}
	return null;
	}

	public Integer getPageCount() {
		try {
			String query = env.getProperty("productcount");
			
			Map<String, Object> param= new HashMap<String, Object>();
			 List<Map<String, Object>> queryResult=productTemplate.queryForList(query, param);
			 int noOfProducts=Integer.parseInt(queryResult.get(0).get("COUNT(*)").toString());
			 int noOfProductsPerPage=Integer.parseInt(key_map);
			 int pageCount=(int)Math.ceil((double)noOfProducts/noOfProductsPerPage);
			 
			 return Integer.valueOf(pageCount);
		}catch(Exception e) {
			log.error("Error in finding count of pages -->", e);
		}
		return null;
	}
	
}
