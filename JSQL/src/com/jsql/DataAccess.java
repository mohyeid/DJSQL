package com.jsql;

import java.util.Collection;
import java.util.Map;

import com.jsql.impl.JQuery;

public class DataAccess {
	public static void main(String[] args) {
        JQuery jquery = new JQuery();
        Collection<Map<String, Object>> result = jquery.select("tbl1::column1", "tbl2::column2") //Select column list
									        		.from("Table1" , "TB1") // Specifiy main table entry, and you can add alias
									        		.join("Table2::tb2") // Provide your join table, and another way to provide alias name
									        		.on("tbl1.key1", "tbl2.key1") // your on statement will be based on the passed 2 values equaliy 
									        		.join("Table3", "tbl3", true) // Join another table with a flag to enable/disable the join (Lazy Joining)
									        		.on("tbl2.key2", "tbl3.key1", (st-> {st.and("tbl3.condition = true"); return st;}))
									        		.where("tbl1.condition", true, "!=") // Start your where statment and it also support enable/disable flags 
									        		.and("tbl2.condition = true", (st-> {st.or("tbl.cond2", 9000, "="); return st;})) // And statment that is grouping an or inside parentheses to group conditions  
									        		.and("tbl3.cond3=5", false) // And statment with a flag to enable/disable the condition 
									        		.get((String sql, Map<String, Object> parameters)-> getData(sql, parameters)); // Passing the hybrid getter. 
        															//You can also assign the getter at the jqueryobject itself by calling setGetter.
    }
	
	private static Collection<Map<String, Object>> getData(String sql, Map<String, Object> parameters){
		
		
		
		return null;
		
	}
}
