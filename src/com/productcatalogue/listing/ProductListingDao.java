package com.productcatalogue.listing;

import java.util.List;
import java.util.Map;

public interface ProductListingDao {

	public List<Map<String, Object>> getProductList();

	public List<Map<String, Object>> getProductListSortedBy(Map<String,String> filterMap, int pageIndex);

	public List<Map<String, Object>> getProducts(int[] productIdsList);

	public Integer getPageCount();

	public List<Map<String, Object>> getShortProductList();

	public Map<String, Object> getCategoryMap();
}
