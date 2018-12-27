package com.productcatalogue.listing;

import java.util.List;
import java.util.Map;

public interface ProductListingDao {

	public List<Map<String, Object>> getProductList();

	public List<Map<String, Object>> getProductListSortedBy(String columnName, int fromIndex);

}
