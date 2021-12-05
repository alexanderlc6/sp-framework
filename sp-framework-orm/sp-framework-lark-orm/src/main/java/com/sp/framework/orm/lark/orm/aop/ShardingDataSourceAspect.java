package com.sp.framework.orm.lark.orm.aop;

import java.util.ArrayList;
import java.util.List;

import com.sp.framework.orm.lark.orm.dao.ShardingDataSource;
import com.sp.framework.orm.lark.orm.dao.ShardingStatusHolder;
import com.sp.framework.orm.lark.common.annotation.MergeShardingDB;
import com.sp.framework.orm.lark.common.annotation.ShardingDB;

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

@Aspect
@Order(0)
public class ShardingDataSourceAspect implements Ordered{
	protected static final Logger logger = LoggerFactory.getLogger(ShardingDataSourceAspect.class);
	
	@Autowired
	TransactionAttributeSource transactionAttributeSouce;
	
	@Autowired
	private ShardingDataSource shardingDataSource;
	
	@Autowired(required=false)
	private ShardingKeyRuleHandle shardingKeyRuleHandle;
	
	/**
	 * 计算拆分关键字
	 * 其实主要是交给项目具体实现
	 * @param shardingDB
	 * @param args
	 * @return
	 */
	private String calcShardingKey(ShardingDB shardingDB,Object[] args){
		
		String result=null;
		
		int keyIndex=0;
				
		if(shardingDB.value()<args.length){
			
			keyIndex=shardingDB.value();
		}

		if(shardingKeyRuleHandle==null){
			result=defaultCalcKeyRule(shardingDataSource.getKeyList(), args[keyIndex]);
		}
		else{
			result=shardingKeyRuleHandle.calcKeyRule(shardingDataSource.getKeyList(), args[keyIndex]);
		}
			
		return result;
	}
	
	private String defaultCalcKeyRule(List<Object> keyList,Object currentKeyValue){
		
		Integer ckv=0;
		
		if(currentKeyValue instanceof Integer ){
			ckv=(Integer)currentKeyValue;
		}
		if(currentKeyValue instanceof Long){
			ckv=((Long)currentKeyValue).intValue();
		}
		else{
			ckv=currentKeyValue.hashCode();
		}
		
		int modVal=ckv%keyList.size();
		
		return (String)keyList.get(modVal);
		
	}
	
	/**
	 * 合并多数据源进行返回操作
	 * @param pjp
	 * @return
	 */
	private Object doInnerQuery(ProceedingJoinPoint pjp){
		
		MethodSignature ms = (MethodSignature)pjp.getSignature();
		
		MergeShardingDB mergeShardingDB=ms.getMethod().getAnnotation(MergeShardingDB.class);
		
		List<Object> keyList=null;
		
		//注解中如果指定数据源，则使用指定数据源，否则使用全数据源进行合并
		if(mergeShardingDB.dataSourceKeys()!=null&&mergeShardingDB.dataSourceKeys().length>0){
			
			keyList=new ArrayList<Object>();
			
			for(Object dk:mergeShardingDB.dataSourceKeys()){
				keyList.add(dk);
			}
		}
		else{
			keyList=shardingDataSource.getKeyList();
		}
		
		List<Object> allList=new ArrayList<Object>();
		
		for(Object dsKey:keyList){
			
			String key=(String)dsKey;
			
			
			try {
				
				ShardingStatusHolder.setShardingDataSourceStatus(key);
				
				List list=(List)pjp.proceed(pjp.getArgs());
				allList.addAll(list);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				ShardingStatusHolder.clearShardingDataSourceStatus();
			}
			
			
		}
		
		return allList;
		
	}
	
	@Around("this(lark.common.com.yh.common.lark.orm.dao.ShardingSupport)")
	public Object doQuery(ProceedingJoinPoint pjp) throws Throwable{	
		MethodSignature ms = (MethodSignature)pjp.getSignature();
		
		ShardingDB shardingDB=ms.getMethod().getAnnotation(ShardingDB.class);
		
		MergeShardingDB mergeShardingDB=ms.getMethod().getAnnotation(MergeShardingDB.class);
		
		if(shardingDB!=null&&mergeShardingDB!=null){
			throw new Exception("ShardingDB && MergeShardingDB annotation cant' both exists");
		}
		
		//不允许mergeShardingDB 注解 出现在内嵌方法中,因为切换数据源无效
		if(mergeShardingDB!=null&&ShardingStatusHolder.getShardingDataSourceStatus() == null){
			
			return doInnerQuery(pjp);
		}
		
		TransactionAttribute ta = transactionAttributeSouce.getTransactionAttribute(ms.getMethod(), pjp.getTarget().getClass());
		//如果未启用水平分库 或者(已经设置过数据源，并且不是新挂起事务)直接跳过
		if (shardingDB==null||(ShardingStatusHolder.getShardingDataSourceStatus() != null && ta.getPropagationBehavior() != TransactionDefinition.PROPAGATION_REQUIRES_NEW)){
			return pjp.proceed(pjp.getArgs());
		}
		
	
		logger.debug("determine datasource for query:{}.{}",ms.getDeclaringType().getName(),ms.getMethod().getName());		
		logger.debug("Current operation's transaction status: {}", ta == null ? "null": ta.toString());
		
		String currentStatus = ShardingStatusHolder.getShardingDataSourceStatus() ;
		
		ShardingStatusHolder.setShardingDataSourceStatus(calcShardingKey(shardingDB,pjp.getArgs()));
		
		try {
			Object rtn = pjp.proceed(pjp.getArgs());
			return rtn;
		} catch (Throwable e) {
			throw e;
		} finally {
				//因为本次已经设置数据源，所以在aop拦截方法的内容结束后，清除状态
				if(ta.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW){
					logger.debug("Fallback to previous sharding Status: {}", currentStatus);
					if(currentStatus == null)
						ShardingStatusHolder.clearShardingDataSourceStatus();
					else
						ShardingStatusHolder.setShardingDataSourceStatus(currentStatus);
				}else{
					logger.debug("Clear Read/Write Status");
					ShardingStatusHolder.clearShardingDataSourceStatus();					
				}				
			
		}
				
				

	}

	public int getOrder() {
		return 0;
	}
}
