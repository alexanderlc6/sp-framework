package com.sp.framework.orm.lark.orm.aop;

import org.mybatis.spring.SqlSessionTemplate;

public class FindPageQueryHandle extends AbstractQueryHandle {

	public FindPageQueryHandle(SqlSessionTemplate sqlSessionTemplate) {
		super(sqlSessionTemplate);
	}



	@Override
	public Object executeDetail(Object[] args) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.selectList("findById");
		return null;
	}

}
