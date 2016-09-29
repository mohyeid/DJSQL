package com.jsql.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.jsql.interfaces.JoinStatment;
import com.jsql.interfaces.Query;
import com.jsql.interfaces.QueryStatement;
import com.jsql.interfaces.QueryWhereStatment;
import com.jsql.interfaces.ResultGetter;
import com.jsql.interfaces.Statement;
import com.jsql.interfaces.WhereGroup;
import com.jsql.interfaces.WhereStatement;

public class JQuery implements Query, Statement,  QueryStatement, WhereStatement, JoinStatment, QueryWhereStatment {
	String statment;
	String whereClause;
	Map<String, Object> parameters = new HashMap<String, Object>();
	private ResultGetter getter;
	@Override
	public Statement select(String column, String... args) {
		this.statment = "SELECT " + column; 
		return this;
	}
	
	@Override
	public QueryStatement from(String tableName) {
		String [] values = this.getTableNameAndAlias(tableName);
		if (values.length > 1){
			return this.from(values[0], values[1]);
		}
		else{
			return this.from(values[0], null);
		}
	}
	
	@Override
	public QueryStatement from(String tableName, String alias) {
		this.statment += " FROM " + tableName;
		if (alias != null){
			this.statment += " AS " + alias;
		}
		return this;
	}
	
	@Override
	public JoinStatment join(String tableName) {
		
		String [] values = this.getTableNameAndAlias(tableName);
		if (values.length > 1){
			return this.join(values[0], values[1]);
		}
		else{
			return this.join(values[0], null);
		}
	}
	

	@Override
	public JoinStatment join(String tableName, String alias) {
		this.statment += " INNER JOIN " + tableName;
		if (alias != null){
			this.statment += " AS " + alias;
		}
		return this;
	}

	
	@Override
	public QueryWhereStatment on(String column1, String column2) {
		this.statment += " ON " + column1 + " = " + column2;
		return this;
	}
	
	@Override
	public WhereStatement where(String whereCluse) {
		this.whereClause = " WHERE " + whereCluse;
		return this;
	}
	
	@Override
	public <T> WhereStatement where(String column, T value, String operator) {
		this.whereClause = " WHERE " + this.setParam(column, operator, value);
		return this;
	}
	
	@Override
	public WhereStatement and(String andCondition) {
		if (this.whereClause == null){
			this.statment += " AND " + andCondition;
		}
		else{
			this.whereClause += this.verifyWhere(" AND ") + andCondition;
		}
		
		return this;
	}
	
	
	@Override
	public WhereStatement and(String andCondition, WhereGroup predicate) {
		if (this.whereClause == null){
			this.statment += " AND ";
		}
		else{
			this.whereClause += this.verifyWhere(" AND ");
		}
		
		return groupCondition(andCondition, predicate);
	}

	@Override
	public WhereStatement or(String orCondition) {
		if (this.whereClause == null){
			this.statment += " OR " + orCondition;
		}
		else{
			this.whereClause += this.verifyWhere(" OR ") + orCondition;
		}
		return this;
	}

	@Override
	public WhereStatement or(String orCondition, WhereGroup predicate) {
		if (this.whereClause == null){
			this.statment += " OR ";
		}
		else{
			this.whereClause += this.verifyWhere(" OR ");
		}
		return groupCondition(orCondition, predicate);
	}

	@Override
   public WhereStatement groupCondition(String condition, WhereGroup predicate) {
		if (this.whereClause != null){
			this.whereClause += " ( " + condition;
			predicate.set(this);
			this.whereClause += " ) ";
			
		}
		else{
			this.statment += " ( " + condition;
			predicate.set(this);
			this.statment += " ) ";
		}
		return this;
	}
	
	@Override
	public WhereStatement groupCondition(String column, Object value, String operator, WhereGroup predicate) {
		this.whereClause += " ( " + this.setParam(column, operator, value);
		predicate.set(this);
		this.whereClause += " ) ";
		return this;
	}
	
	@Override
	public String getSQL() {
		return this.statment + (this.whereClause != null? this.whereClause : "");
	}

	@Override
	public QueryWhereStatment on(String column1, String column2, WhereGroup predicate) {
		this.on(column1, column2);
		predicate.set(this);
		return this;
	}
	
	private String [] getTableNameAndAlias(String tableName){
		String [] values = tableName.split("::");
		return values;
	}
	
	private String verifyWhere(String operator){
		if (this.whereClause != null && !this.whereClause.contains("WHERE")){
			return " WHERE ";
		}
		else {
			return operator;
		}
	}

	private <T> String setParam(String paramName, String operator, T value){
		
		String paramCluase = "";
		if(paramName.startsWith(":")){
			paramName = paramName.replace(":", "");
			String enclosedParam;
			if (paramName.contains(".")){
				String[] param = paramName.split("\\.");
				enclosedParam = param[0] + "_" + param[1];
				enclosedParam = this.getUniqueParamName(enclosedParam);
				parameters.put(enclosedParam, value);
			}
			else{
				enclosedParam = paramName;
				parameters.put(enclosedParam, value);
			}
			
			if (operator.equals("=") || operator.equals("!=")){
				paramCluase = paramName + " " + operator + " :" + enclosedParam + " ";
			}
			else if (operator.toLowerCase().equals("in")){
				paramCluase = paramName + " IN " +  "(:" + enclosedParam + ") ";
			}
			else if (operator.toLowerCase().equals("is")){
				paramCluase = paramName + " IS NULL ";
			}
		}
		else{
			paramCluase = paramName + " " + operator + " ";
			if (operator.equals("=") || operator.equals("!=")){
				paramCluase += value;
			}
			else if (operator.toLowerCase().equals("in")){
				// paramCluase += "(:" + enclosedParam + ") "; Not implemented
			}
			else if (operator.toLowerCase().equals("is")){
				paramCluase += " NULL ";
			}
		}
		return paramCluase;
	}
	
	private String getUniqueParamName(String paramName){
		List<String> params = this.parameters.entrySet().stream()
									.filter(x-> x.getKey().startsWith(paramName))
									.map(x-> x.getKey())
									.collect(Collectors.toList());
		
		
		if (params.size() > 0){
			Random rand = new Random();
			Integer x = rand.nextInt(50) + 1;
			return this.getUniqueParamName(paramName + x);
		}
		
		return paramName;
	}
	
	@Override
	public WhereStatement and(String andCondition, boolean apply) {
		if (!apply){
			return this;
		}
		else{
			return this.and(andCondition);
		}
	}

	@Override
	public WhereStatement and(String andCondition, WhereGroup predicate, boolean apply) {
		if (!apply){
			return this;
		}
		else{
			return this.and(andCondition, predicate);
		}
	}

	@Override
	public WhereStatement or(String orCondition, boolean apply) {
		if (!apply){
			return this;
		}
		else{
			return this.or(orCondition);
		}
	}

	@Override
	public WhereStatement or(String orCondition, WhereGroup predicate, boolean apply) {
		if (!apply){
			return this;
		}
		else{
			return this.or(orCondition, predicate);
		}
	}

	@Override
	public WhereStatement where(String whereCluse, boolean apply) {
		if (!apply){
			this.whereClause = "";
			return this;
		}
		else{
			return this.where(whereCluse);
		}
	}

	@Override
	public <T> WhereStatement where(String column, T value, String operator, boolean apply) {
		if (!apply){
			this.whereClause = "";
			return this;
		}
		else{
			return this.where(column, value, operator);
		}
	}

	@Override
	public QueryWhereStatment on(String column1, String column2, boolean apply) {
		if (apply){
			return this.on(column1, column2);
		}
		else{
			return this;
		}
	}

	@Override
	public QueryWhereStatment on(String column1, String column2, WhereGroup predicate, boolean apply) {
		if (apply){
			return this.on(column1, column2, predicate);
		}
		else{
			return this;
		}
	}

	@Override
	public JoinStatment join(String tableName, boolean apply) {
		if (apply){
			return this.join(tableName);
		}
		else{
			return this;
		}
	}

	@Override
	public JoinStatment join(String tableName, String alias, boolean apply) {
		if (apply){
			return this.join(tableName, alias);
		}
		else{
			return this;
		}
	}

	@Override
	public WhereStatement and(String column, Object value, String operator) {
		if (this.whereClause == null){
			this.statment += " AND " + this.setParam(column, operator, value);;
		}
		else{
			this.whereClause += this.verifyWhere(" AND ") + this.setParam(column, operator, value);
		}
		return this;
	}

	@Override
	public WhereStatement and(String column, Object value, String operator, boolean apply) {
		if (apply){
			return this.and(column, value, operator);
		}
		else{
			return this;
		}
	}

	@Override
	public WhereStatement and(String column, Object value, String operator, WhereGroup predicate) {
		if (this.whereClause == null){
			this.statment += " AND ";
		}
		else{
			this.whereClause += this.verifyWhere(" AND ");
		}
		
		return groupCondition(column, value, operator, predicate);
	}

	@Override
	public WhereStatement and(String column, Object value, String operator, WhereGroup predicate, boolean apply) {
		if(apply){
			return this.and(column, value, operator, predicate);
		}
		else{
			return this;
		}
	}

	@Override
	public WhereStatement or(String column, Object value, String operator) {
		if (this.whereClause == null){
			this.statment += " AND " + this.setParam(column, operator, value);;
		}
		else{
			this.whereClause += this.verifyWhere(" AND ") + this.setParam(column, operator, value);
		}
		return this;
	}

	@Override
	public WhereStatement or(String column, Object value, String operator, boolean apply) {
		if (apply){
			return this.or(column, value, operator);
		}
		else{
			return this;
		}
	}

	@Override
	public WhereStatement or(String column, Object value, String operator, WhereGroup predicate) {
		if (this.whereClause == null){
			this.statment += " OR ";
		}
		else{
			this.whereClause += this.verifyWhere(" OR ");
		}
		
		return groupCondition(column, value, operator, predicate);
	}

	@Override
	public WhereStatement or(String column, Object value, String operator, WhereGroup predicate, boolean apply) {
		if(apply){
			return this.or(column, value, operator, predicate);
		}
		else{
			return this;
		}
	}
	
	@Override
	public Collection<Map<String, Object>> get(ResultGetter getter){
		return getter.getResultMap(this.getSQL(), this.parameters);
	}

	@Override
	public void setGetter(ResultGetter getter) {
		this.getter = getter;
	}

	@Override
	public ResultGetter getGetter() {
		return this.getter;
	}

	@Override
	public WhereStatement where(String whereCluse, WhereGroup predicate) {
		this.whereClause = " WHERE ";
		return groupCondition(whereCluse, predicate);
	}

	@Override
	public WhereStatement where(String whereCluse, WhereGroup predicate, boolean apply) {
		if (apply){
			return this.where(whereCluse, predicate);
		}
		else{
			return this;
		}
	}
}
