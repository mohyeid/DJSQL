package com.jsql.interfaces;

import java.util.Collection;
import java.util.Map;

public interface GetResult {
	String getSQL();
	Collection<Map<String, Object>> get(ResultGetter getter);
	default Collection<Map<String, Object>> get(){
		return this.get(this.getGetter());
	}
	void setGetter(ResultGetter getter);
	ResultGetter getGetter();
}
