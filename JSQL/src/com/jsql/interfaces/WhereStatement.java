package com.jsql.interfaces;

import java.util.function.Predicate;

public interface WhereStatement extends GetResult {
	WhereStatement and(String andCondition);
	WhereStatement and(String andCondition, boolean apply);
	
	WhereStatement and(String andCondition, WhereGroup predicate);
	WhereStatement and(String andCondition, WhereGroup predicate, boolean apply);
	
	WhereStatement and(String column, Object value, String operator);
	WhereStatement and(String column, Object value, String operator, boolean apply);
	
	WhereStatement and(String column, Object value, String operator, WhereGroup predicate);
	WhereStatement and(String column, Object value, String operator, WhereGroup predicate, boolean apply);
	
	WhereStatement or(String orCondition);
	WhereStatement or(String orCondition, boolean apply);
	
	WhereStatement or(String column, Object value, String operator);
	WhereStatement or(String column, Object value, String operator, boolean apply);
	
	WhereStatement or(String orCondition, WhereGroup predicate);
	WhereStatement or(String orCondition, WhereGroup predicate, boolean apply);
	
	WhereStatement or(String column, Object value, String operator, WhereGroup predicate);
	WhereStatement or(String column, Object value, String operator, WhereGroup predicate, boolean apply);
	
	WhereStatement groupCondition(String condition, WhereGroup predicate);
	WhereStatement groupCondition(String column, Object value, String operator, WhereGroup predicate);
}
