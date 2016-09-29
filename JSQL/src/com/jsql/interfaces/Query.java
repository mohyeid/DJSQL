package com.jsql.interfaces;

public interface Query {
	Statement select(String column, String... args);
}
