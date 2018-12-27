package com.product.metadata;

import java.util.HashMap;
import java.util.List;

public interface ProductPVWcountDao {

	public List<HashMap<String, Object>> getPVWcount();
	public List<HashMap<String, Object>> getPVWcount(int fromIndex, int toIndex);
	public List<HashMap<String, Object>> getPVWcount(List<String> productIds);
	public List<HashMap<String,Object>> getPVWcount(String productId);
}
