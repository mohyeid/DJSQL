package com.jsql.interfaces;

public interface QueryStatement extends GetResult {
	JoinStatment join(String tableName, boolean apply);
	JoinStatment join(String tableName);
	
	JoinStatment join(String tableName, String alias);
	JoinStatment join(String tableName, String alias, boolean apply);
	
	WhereStatement where(String whereCluse, boolean apply);
	WhereStatement where(String whereCluse);
	
	WhereStatement where(String whereCluse, WhereGroup predicate);
	WhereStatement where(String whereCluse, WhereGroup predicate, boolean apply);
	
	
	<T> WhereStatement where(String column, T value, String operator);
	<T> WhereStatement where(String column, T value, String operator, boolean apply);
}
