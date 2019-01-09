package com.productcatalogue.listing;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.koyakal.common.security.ArmourException;
import com.koyakal.common.security.SecurityUtil;

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
			
			Map<String, Object> param= new HashMap<String, Object>();
			String query = env.getProperty("productlist");
			String sortQuery=query.substring(0, query.length()-1);
			if(filterMap.containsKey("category")) {
				//get category id from names
				String categoryName=filterMap.get("category");
				String[] categoryNameArr=categoryName.split(",\\s*");
				List<String> categoryNameList=Arrays.asList(categoryNameArr);
				String categoryIdsQuery = env.getProperty("categoryids");
				Map<String, Object> categoryIdsQueryParam = new HashMap<String, Object>();
				categoryIdsQueryParam.put("categoryNames",categoryNameList);
				List<Map<String,Object>> categoryIds=productTemplate.queryForList(categoryIdsQuery, categoryIdsQueryParam);
				
				//add category id list as param to sortQuery
				List<Integer> categoryIdList=new ArrayList<Integer>();
				for(Map<String, Object> row:categoryIds) {
					categoryIdList.add(Integer.parseInt(row.get("CategoryId").toString()));
				}
				sortQuery+=" WHERE productcategorysubcategorymap.CategoryId IN (:categoryIds) ";
				param.put("categoryIds", categoryIdList);
			}
			
			sortQuery+=" order by ";
			
			if(filterMap.containsKey("columnName")) {
				sortQuery=table.containsKey(filterMap.get("columnName"))?sortQuery+table.get(filterMap.get("columnName")):sortQuery+table.get("ProductCreatedTime");
				
			}
			else {
				sortQuery+=table.get("ProductCreatedTime");
				
			}
			
			if(filterMap.containsKey("order")) {
				sortQuery+=" "+filterMap.get("order");
			}else if(!filterMap.containsKey("columnName")||filterMap.get("columnName").equals(table.get("ProductCreatedTime"))) {
				sortQuery+=" desc";
			}
			
			int noOfProductsPerPage=Integer.parseInt(key_map);
			int fromIndex=page*noOfProductsPerPage;
			int endIndex=fromIndex+noOfProductsPerPage;
			sortQuery+=" limit "+fromIndex+", "+endIndex+";";
			
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

	public List<Map<String, Object>> getShortProductList() {
		try {
			String query = env.getProperty("shortproductlist");
			
			Map<String, Object> param= new HashMap<String, Object>();
			return productTemplate.queryForList(query, param);
		}catch(Exception e) {
			log.error("Error in fetching short products list -->", e);
		}
		return null;
		
	}

	public List<Map<String, Object>> getCategories() {
		try {
			String query = env.getProperty("categorieslist");
			
			Map<String, Object> param= new HashMap<String, Object>();
			return productTemplate.queryForList(query, param);
		}catch(Exception e) {
			log.error("Error in fetching categories list -->", e);
		}
		return null;
	}

	public List<Map<String, Object>> getRootCategories() {
		try {
			String query = env.getProperty("rootcategorieslist");
			
			Map<String, Object> param= new HashMap<String, Object>();
			return productTemplate.queryForList(query, param);
		}catch(Exception e) {
			log.error("Error in fetching root categories list -->", e);
		}
		return null;
	}
	
	public List<Map<String, Object>> getOrphanCategories() {
		try {
			String query = env.getProperty("orphancategorieslist");
			
			Map<String, Object> param= new HashMap<String, Object>();
			return productTemplate.queryForList(query, param);
		}catch(Exception e) {
			log.error("Error in fetching orphan categories list -->", e);
		}
		return null;
	}
	public Map<String, Object> getCategoryMap() {
		List<Map<String, Object>> categoriesList=this.getCategories();
		List<Map<String, Object>> rootCategoriesList=this.getRootCategories();
		List<Map<String, Object>> orphanCategoriesList=this.getOrphanCategories();
		ArrayList<Integer> rootCategoryId=new ArrayList<Integer>();
		for(Map<String, Object> rootCategoryRow:rootCategoriesList) {
			rootCategoryId.add((Integer)rootCategoryRow.get("CategoryId"));
		}
		ArrayList<Integer> orphanCategoryId=new ArrayList<Integer>();
		for(Map<String, Object> orphanCategoryRow:orphanCategoriesList) {
			orphanCategoryId.add((Integer)orphanCategoryRow.get("CategoryId"));
		}
		HashMap<String,ArrayList<Integer>> categoryMap=new HashMap<String,ArrayList<Integer>>();
		categoryMap.put("rootcategories", rootCategoryId);
		categoryMap.put("orphancategories", orphanCategoryId);
		for(Map<String, Object> categoryRow:categoriesList) {
			if(categoryMap.containsKey(((Integer)categoryRow.get("CategoryId")).toString())) {
				categoryMap.get(((Integer)categoryRow.get("CategoryId")).toString()).add((Integer)(categoryRow.get("SubCategoryId")));
			}else {
				categoryMap.put(((Integer)categoryRow.get("CategoryId")).toString(),new ArrayList<Integer>());
				categoryMap.get(((Integer)categoryRow.get("CategoryId")).toString()).add((Integer)(categoryRow.get("SubCategoryId")));
			}
		}
		HashMap<String,Object> categoryDetails=new HashMap<String,Object>();
		for(Map<String, Object> categoryRow:categoriesList) {
			categoryDetails.put(((Integer)categoryRow.get("CategoryId")).toString(), categoryRow.get("CategoryName").toString());
		}
		HashMap<String,Object> categoryTree=new HashMap<String,Object>();
		categoryTree.put("categoryMap", categoryMap);
		categoryTree.put("categoryDetails",categoryDetails);
		return categoryTree;
	}
	
}
