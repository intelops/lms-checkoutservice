package com.spring.util;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;

public class LyMySQLDialect extends MySQL5Dialect {
	  public LyMySQLDialect() {
		  super();
	        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
	   
	    }
}
