package com.jsql.interfaces;

public interface JoinStatment {
	QueryWhereStatment on(String column1, String column2);
	QueryWhereStatment on(String column1, String column2, boolean apply);
	
	QueryWhereStatment on(String column1, String column2, WhereGroup predicate);
	QueryWhereStatment on(String column1, String column2, WhereGroup predicate, boolean apply);
}
