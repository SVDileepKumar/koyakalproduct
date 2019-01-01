package com.productcatalogue.listing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.koyakal.common.config.KoyakalCommonProperty;
import com.koyakal.common.security.ArmourException;
import com.koyakal.common.security.SecurityUtil;
import com.productcatalogue.config.AppConfig;
import com.productcatalogue.constants.KoyakalProducts;





@RestController
public class ProductListingController {
	
	@Autowired
	ProductListingDao ProductListing;
	
	

	@GetMapping(value="/list", produces="application/json")
	public Map<String, Object> productListing(){
		String appKey =  AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_KEY);
		String appId=AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_ID);
		String noOfProductsPerPage = KoyakalCommonProperty.getProperty("pagination");
		String encryptedProductId=""; 

		List<Map<String, Object>> productList=new ArrayList<>();
		productList=ProductListing.getProductList();
		for(Map<String, Object> row:productList) {
			String productId=row.get("ProductId").toString();
			try {
				encryptedProductId = SecurityUtil.encrypt(productId, appKey);
			} catch (ArmourException e) {
				System.out.println("Error while encryption of productId");
			}
			row.put("ProductID", encryptedProductId);
			
		}
		//adding application details -- appName and encrypted appId
		Map<String, Object> appDetails = new HashMap<>();
		appDetails.put("AppName", KoyakalProducts.PC_APP_NAME);
		String _AppId="";
		try {
			_AppId = SecurityUtil.encrypt(appId, appKey);
		} catch (ArmourException e) {
			System.out.println("Error while encryption of appId");
		}
		appDetails.put("AppId", _AppId);
		Map<String, Object> productMap=new HashMap<>();
		productMap.put("products", productList);
		productMap.put("appDetails", appDetails);
		return productMap;
	}	
	/*
	 * sorts the products using created time
	 */
	@GetMapping(value="/list/{fromIndex}", produces="application/json")
	public List<Map<String, Object>> sortProductListing(@PathVariable("fromIndex") int fromIndex){
		return ProductListing.getProductListSortedBy("ProductCreatedTime", fromIndex);
	}	
	
	/*
	 * sorts the products using given column name
	 */
	@GetMapping(value="/list/{sortByColumn}/{fromIndex}", produces="application/json")
	public List<Map<String, Object>> sortProductListingByColumnName(@PathVariable("sortByColumn") String sortByColumn,@PathVariable("fromIndex") int fromIndex){
		return ProductListing.getProductListSortedBy(sortByColumn, fromIndex);
	}
	
	/*
	 * returns list of products with given ids
	 */
	@GetMapping(value="/list/products/{encryptedProductIdsList}", produces="application/json")
	public Map<String, Object> getProducts(@PathVariable String[] encryptedProductIdsList){
		String appKey =  AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_KEY);
		String appId=AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_ID);
		String encryptedProductId="";
		List<Integer> productIdsList=new ArrayList<>();
		for(String encryptedId:encryptedProductIdsList) {
			try {
				String productId = SecurityUtil.decrypt(encryptedId, appKey);
				System.out.println(productId);
				productIdsList.add(Integer.valueOf(productId));
				
			} catch (ArmourException e) {
				System.out.println("Error while decryption of productId");
			}
		}
		List<Map<String, Object>> productList=new ArrayList<>();
		
		if(!productIdsList.isEmpty()) {
			int[] productIdsArr=new int[productIdsList.size()];
			int i=0;
			
			for(int id:productIdsList) {
				productIdsArr[i++]=id;
			}
			
			//Encrypting productID
			
			productList=ProductListing.getProducts(productIdsArr);
			for(Map<String, Object> row:productList) {
				String productId=row.get("ProductId").toString();
				try {
					encryptedProductId = SecurityUtil.encrypt(productId, appKey);
				} catch (ArmourException e) {
					System.out.println("Error while encryption of productId");
				}
				row.put("ProductID", encryptedProductId);

			}
		}
		//adding application details -- appName and encrypted appId
		Map<String, Object> appDetails = new HashMap<>();
		appDetails.put("AppName", KoyakalProducts.PC_APP_NAME);
		String _AppId="";
		try {
			_AppId = SecurityUtil.encrypt(appId, appKey);
		} catch (ArmourException e) {
			System.out.println("Error while encryption of appId");
		}
		appDetails.put("AppId", _AppId);
		Map<String, Object> productMap=new HashMap<>();
		productMap.put("products", productList);
		productMap.put("appDetails", appDetails);
		return productMap;
	}
}
