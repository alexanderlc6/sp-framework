package com.sp.framework.orm.lark.orm.aop;

import com.sp.framework.orm.lark.common.annotation.CommonQuery;
import com.sp.framework.orm.lark.common.annotation.QueryPage;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

/**
 * 查询方法切面类
 */
public class QueryAspect implements Ordered, InitializingBean {

	@Autowired(required=false)
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
//	private NamedQueryHandler namedQueryHandler;
//	private QueryHandler queryHandler;
//	private DynamicQueryHandler dynamicQueryHandler;
//	private NativeQueryHandler nativeQueryHandler;

	@Override
	public void afterPropertiesSet() throws Exception {
//		namedQueryHandler = new NamedQueryHandler(daoService);
//		queryHandler = new QueryHandler(daoService);
//		dynamicQueryHandler = new DynamicQueryHandler(daoService, templateService, dnqProvider);
//		nativeQueryHandler = new NativeQueryHandler(daoService, templateService, dnqProvider);
	}

	@Override
	public int getOrder() {
		return 20;
	}

	@Around("this(com.yh.common.lark.orm.dao.supports.BaseDao)")
	public Object doQuery(ProceedingJoinPoint pjp) throws Throwable{
		MethodSignature ms = (MethodSignature)pjp.getSignature();
		CommonQuery commonQuery=ms.getMethod().getAnnotation(CommonQuery.class);
		QueryPage queryPageCount=ms.getMethod().getAnnotation(QueryPage.class);
		QueryProcess queryProcess=null;
		
		if(commonQuery!=null){
			return new CommonQueryProcess(sqlSessionTemplate).process(pjp);
		} else if(queryPageCount!=null){
			return new CustomPageProcess(sqlSessionTemplate).process(pjp); 
		}

		return pjp.proceed(pjp.getArgs());
	}
}
