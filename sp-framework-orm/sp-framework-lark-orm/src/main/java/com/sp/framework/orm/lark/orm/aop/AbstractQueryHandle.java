package com.sp.framework.orm.lark.orm.aop;

import org.mybatis.spring.SqlSessionTemplate;

public abstract class AbstractQueryHandle {

	public static final String FIND_LIST_BY_PAGE="findListByPage";
	
	public static final String FIND_BY_ID="findById";
	
	protected SqlSessionTemplate sqlSessionTemplate;
	
	AbstractQueryHandle(SqlSessionTemplate sqlSessionTemplate){
		this.sqlSessionTemplate=sqlSessionTemplate;
	}
	
	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public Object execute(Object[] args){
		return executeDetail(args);
	}
	
	public abstract Object executeDetail(Object[] args);
}
