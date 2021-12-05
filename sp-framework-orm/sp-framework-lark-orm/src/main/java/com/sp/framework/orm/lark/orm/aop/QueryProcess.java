package com.sp.framework.orm.lark.orm.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.mybatis.spring.SqlSessionTemplate;

public abstract class QueryProcess {
	protected SqlSessionTemplate sqlSessionTemplate;
	
	public abstract Object process(ProceedingJoinPoint pjp);
}
