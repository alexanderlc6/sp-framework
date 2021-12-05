package com.sp.framework.orm.lark.orm.aop;

import org.mybatis.spring.SqlSessionTemplate;

public class FindByIdQueryHandle extends AbstractQueryHandle {

	public FindByIdQueryHandle(SqlSessionTemplate sqlSessionTemplate) {
		super(sqlSessionTemplate);
	}

	@Override
	public Object executeDetail(Object[] args) {
		return sqlSessionTemplate.selectOne("com.yh.common.lark.orm.dao.test.TestDao.findById",args[0]);
	}
}
