package com.jsql.interfaces;

public interface Statement {
	QueryStatement from(String tableName);
	QueryStatement from(String tableName, String alias);
}
