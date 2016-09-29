package com.jsql.interfaces;

import java.util.Collection;
import java.util.Map;

public interface ResultGetter {
	 Collection<Map<String, Object>> getResultMap(String sql_query, Map<String, Object> parameters);
}
