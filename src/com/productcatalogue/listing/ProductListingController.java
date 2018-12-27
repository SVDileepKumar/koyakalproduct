package com.productcatalogue.listing;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.koyakal.common.config.KoyakalCommonProperty;

@RestController
public class ProductListingController {
	
	@Autowired
	ProductListingDao ProductListing;
	
	
	@GetMapping(value="/list", produces="application/json")
	public List<Map<String, Object>> productListing(){
		String key_map = KoyakalCommonProperty.getProperty("pagination");
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
}
