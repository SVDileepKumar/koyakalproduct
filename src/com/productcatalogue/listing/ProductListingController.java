package com.productcatalogue.listing;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.koyakal.common.security.ArmourException;
import com.koyakal.common.security.SecurityUtil;
import com.productcatalogue.config.AppConfig;
import com.productcatalogue.constants.KoyakalProducts;




@RestController
public class ProductListingController {
	
	@Autowired
	ProductListingDao ProductListing;
	
	
	@GetMapping(value="/list", produces="application/json")
	public List<Map<String, Object>> productListing(){
		String appKey =  AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_KEY);
		String appid=AppConfig.getConfig(KoyakalProducts.PC_APP_NAME, KoyakalProducts.APP_ID);
		String productId = "Dileep";
		String EncryptedProductId, decryptedProductId;
		try {
			EncryptedProductId = SecurityUtil.encrypt(productId, appKey);
			decryptedProductId = SecurityUtil.decrypt(EncryptedProductId, appKey);
		} catch (ArmourException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ProductListing.getProductList();
	}	
	/*
	 * sorts the products using created time
	 */
	@GetMapping(value="/list/{fromIndex}", produces="application/json")
	public List<Map<String, Object>> sortProductListing(@PathVariable("fromIndex") int fromIndex){
		return ProductListing.getProductListSortedBy("CreatedTime", fromIndex);
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
	@GetMapping(value="/list/products/{productIdsList}", produces="application/json")
	public List<Map<String, Object>> getProducts(@PathVariable int[] productIdsList){
		return ProductListing.getProducts(productIdsList);
	}
}
