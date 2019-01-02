package com.productcatalogue.listing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.koyakal.common.security.ArmourException;
import com.koyakal.common.security.SecurityUtil;
import com.productcatalogue.config.AppConfig;
import com.productcatalogue.constants.KoyakalProducts;






@RestController
public class ProductListingController {
	
	@Autowired
	ProductListingDao ProductListing;
	
	private static final Logger log = Logger.getLogger(ProductListingController.class);

	@GetMapping(value="/list", produces="application/json")
	public Map<String, Object> productListing(){
		String appKey =  AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_KEY);
		String appId=AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_ID);
		String encryptedProductId=""; 

		ArrayList<Map<String, Object>> productList=new ArrayList<Map<String,Object>>();
		productList=(ArrayList<Map<String, Object>>)ProductListing.getProductList();
		for(Map<String, Object> row:productList) {
			String productId=row.get("ProductId").toString();
			try {
				encryptedProductId = SecurityUtil.encrypt(productId, appKey);
			} catch (ArmourException e) {
				log.error("Error while encryption of productId");
				
			}
			row.put("ProductID", encryptedProductId);
			
		}
		//adding application details -- appName and encrypted appId
		HashMap<String, Object> productMap=new HashMap<>();
		productMap.put("products", productList);
		productMap.put(KoyakalProducts.APP_NAME, KoyakalProducts.PC_APP_NAME);
		String _AppId="";
		try {
			_AppId = SecurityUtil.encrypt(appId, appKey);
		} catch (ArmourException e) {
			System.out.println("Error while encryption of appId");
		}
		productMap.put(KoyakalProducts.APP_ID, _AppId);
		return productMap;
	}	
	/*
	 * sorts the products using created time
	 */
	@GetMapping(value="/list/{pageIndex}", produces="application/json")
	public Map<String, Object> sortProductListing(@PathVariable("pageIndex") String pageIndex){
		String appKey =  AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_KEY);
		String appId=AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_ID);
		
		String encryptedProductId=""; 
		String regex="^[0-9]*$";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(pageIndex);
		
		ArrayList<Map<String, Object>> productList=new ArrayList<Map<String,Object>>();
		HashMap<String, Object> productMap=new HashMap<>();
		if(matcher.find()&&Integer.parseInt(pageIndex)>=0) {
			HashMap<String, String> filterMap=new HashMap<>();
			filterMap.put("columnName", "ProductCreatedTime");
			productList=(ArrayList<Map<String, Object>>)ProductListing.getProductListSortedBy(filterMap, Integer.parseInt(pageIndex));
			for(Map<String, Object> row:productList) {
				String productId=row.get("ProductId").toString();
				try {
					encryptedProductId = SecurityUtil.encrypt(productId, appKey);
				} catch (ArmourException e) {
					log.error("Error while encryption of productId");
					
				}
				row.put("ProductID", encryptedProductId);
				
			}
			
		}
		//adding application details -- appName and encrypted appId
		
		productMap.put("products", productList);
		productMap.put(KoyakalProducts.APP_NAME, KoyakalProducts.PC_APP_NAME);
		String _AppId="";
		try {
			_AppId = SecurityUtil.encrypt(appId, appKey);
		} catch (ArmourException e) {
			System.out.println("Error while encryption of appId");
		}
		productMap.put(KoyakalProducts.APP_ID, _AppId);
		return productMap;
	}	
	
	/*
	 * sorts the products using given column name
	 */
	@PostMapping(value="/list/{pageIndex}", produces="application/json",consumes="application/json")
	public Map<String, Object> sortProductListingByColumnName(@RequestBody Map<String,String> filterMap,@PathVariable("pageIndex") String pageIndex){
		String appKey =  AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_KEY);
		String appId=AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_ID);
		
		String encryptedProductId=""; 
		String regex="^[0-9]*$";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(pageIndex);
		
		ArrayList<Map<String, Object>> productList=new ArrayList<Map<String,Object>>();
		HashMap<String, Object> productMap=new HashMap<>();
		if(matcher.find()&&Integer.parseInt(pageIndex)>=0&&filterMap!=null) {
			
			productList=(ArrayList<Map<String, Object>>)ProductListing.getProductListSortedBy(filterMap, Integer.parseInt(pageIndex));
			for(Map<String, Object> row:productList) {
				String productId=row.get("ProductId").toString();
				try {
					encryptedProductId = SecurityUtil.encrypt(productId, appKey);
				} catch (ArmourException e) {
					log.error("Error while encryption of productId");
					
				}
				row.put("ProductID", encryptedProductId);
				
			}
			
		}
		//adding application details -- appName and encrypted appId
		
		productMap.put("products", productList);
		productMap.put(KoyakalProducts.APP_NAME, KoyakalProducts.PC_APP_NAME);
		String _AppId="";
		try {
			_AppId = SecurityUtil.encrypt(appId, appKey);
		} catch (ArmourException e) {
			System.out.println("Error while encryption of appId");
		}
		productMap.put(KoyakalProducts.APP_ID, _AppId);
		return productMap;
	}
	
	/*
	 * returns list of products with given ids
	 */
	@PostMapping(value="/list/products", produces="application/json",consumes="application/json")
	public Map<String, Object> getProducts(@RequestBody ArrayList<String> encryptedProductIdsList){
		String appKey =  AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_KEY);
		String appId=AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_ID);
		String encryptedProductId="";
		ArrayList<Integer> productIdsList=new ArrayList<Integer>();
		if(encryptedProductIdsList!=null) {
			for(String encryptedId:encryptedProductIdsList) {
				try {
					String productId = SecurityUtil.decrypt(encryptedId, appKey);
					productIdsList.add(Integer.valueOf(productId));

				} catch (ArmourException e) {
					log.error("Error while decryption of productId");
				}
			}
		}
		
		ArrayList<Map<String, Object>> productList=new ArrayList<>();
		
		if(!productIdsList.isEmpty()) {
			int[] productIdsArr=new int[productIdsList.size()];
			int i=0;
			
			for(int id:productIdsList) {
				productIdsArr[i++]=id;
			}
			
			//Encrypting productID
			
			productList=(ArrayList<Map<String,Object>>)ProductListing.getProducts(productIdsArr);
			for(Map<String, Object> row:productList) {
				String productId=row.get("ProductId").toString();
				try {
					encryptedProductId = SecurityUtil.encrypt(productId, appKey);
				} catch (ArmourException e) {
					log.error("Error while encryption of productId");
				}
				row.put("ProductID", encryptedProductId);

			}
		}
		//adding application details -- appName and encrypted appId
		
		HashMap<String, Object> productMap=new HashMap<>();
		productMap.put("products", productList);
		productMap.put(KoyakalProducts.APP_NAME, KoyakalProducts.PC_APP_NAME);
		String _AppId="";
		try {
			_AppId = SecurityUtil.encrypt(appId, appKey);
		} catch (ArmourException e) {
			System.out.println("Error while encryption of appId");
		}
		productMap.put(KoyakalProducts.APP_ID, _AppId);
		return productMap;
	}
	
	/*
	 * returns no of pages
	 */
	@GetMapping(value="/pagecount",produces="application/json")
	public Map<String,Object> getNoOfPages(){
		Integer NoOfPages=ProductListing.getPageCount();
		HashMap<String, Object> pageCountMap=new HashMap<String, Object>();
		pageCountMap.put("pagecount", NoOfPages);
		return pageCountMap;
	}
}
