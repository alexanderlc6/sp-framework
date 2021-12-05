package com.sp.framework.orm.lark.orm.aop;

import java.lang.reflect.Method;

import com.sp.framework.orm.lark.common.annotation.MoreDB;
import com.sp.framework.orm.lark.orm.dao.MoreStatusHolder;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

/**
 * 多数源切换使用
 * 只需要将当前aop配置到spring
 * 在事务进入的方法加上@ MoreDb
 * 指定一下具体的数据源即可
 * @author Alex Lu
 *
 */
@Aspect
@Order(0)
public class MoreDataSourceAspect implements Ordered{
	protected static final Logger logger = LoggerFactory.getLogger(MoreDataSourceAspect.class);
	
	@Autowired
	TransactionAttributeSource transactionAttributeSource;

	@Around("@annotation(com.yh.common.lark.common.annotation.MoreDB)")
	public Object doQuery(ProceedingJoinPoint pjp) throws Throwable{	
		MethodSignature ms = (MethodSignature)pjp.getSignature();
		Method ImplMethod=pjp.getTarget().getClass().getMethod(
				ms.getMethod().getName(), ms.getMethod().getParameterTypes());
		MoreDB moreDB=ImplMethod.getAnnotation(MoreDB.class);
		TransactionAttribute ta = transactionAttributeSource.getTransactionAttribute(
				ms.getMethod(), pjp.getTarget().getClass());

		//如果未启用多数据源 直接跳过  不再设置数据源
		//如果已经设置过数据源，且当前存在事务，并且不是新挂起事务 直接跳过不再设置数据源
		if (moreDB==null||(MoreStatusHolder.getMoreDataSourceStatus() != null && ta != null
				&& ta.getPropagationBehavior() != TransactionDefinition.PROPAGATION_REQUIRES_NEW)){
			return pjp.proceed(pjp.getArgs());
		}

		logger.debug("determine datasource for query:{}.{}",ms.getDeclaringType().getName(),ms.getMethod().getName());		
		logger.debug("Current operation's transaction status: {}", ta == null ? "null": ta.toString());
		
		String currentStatus = MoreStatusHolder.getMoreDataSourceStatus();
		MoreStatusHolder.setMoreDataSourceStatus(moreDB.value());
		
		try {
			Object rtn = pjp.proceed(pjp.getArgs());
			return rtn;
		} catch (Throwable e) {
			throw e;
		} finally {
				//因为本次已经设置数据源，所以在aop拦截方法的内容结束后，清除状态
			   	//如果当前没有事务，则按照上层方法进行还原
				//如果当前是嵌套事务，则按照上层方法进行还原
				if(ta==null || ta.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW){
					logger.debug("Fallback to previous sharding Status: {}", currentStatus);
					if(currentStatus == null) {
						MoreStatusHolder.clearMoreDataSourceStatus();
					} else {
						MoreStatusHolder.setMoreDataSourceStatus(currentStatus);
					}
				}else{
					logger.debug("Clear more Status");
					MoreStatusHolder.clearMoreDataSourceStatus();					
				}
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
