package com.productcatalogue.listing;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductListingController {
	
	@Autowired
	ProductListingDao ProductListing;
	
	
	@GetMapping(value="/list", produces="application/json")
	public List<Map<String, Object>> productListing(){
		return ProductListing.getProductList();
	}	
}
