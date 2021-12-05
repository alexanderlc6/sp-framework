package com.sp.framework.orm.lark.orm.aop;

import java.lang.reflect.Method;

import com.sp.framework.orm.lark.common.model.BaseModel;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.mybatis.spring.SqlSessionTemplate;
/**
 * 所有加上@CommonQuery 通过此方法来处理
 * 公共方法的查询处理
 * 如saveOrUpdate
 * @author Alex Lu
 *
 */
public class CommonQueryProcess extends QueryProcess {

	public static final String SAVE_OR_UPDATE="saveOrUpdate";
	
	public static final String SAVE_OR_UPDATE_VERSION="saveOrUpdateByVersion";
		
	public CommonQueryProcess(SqlSessionTemplate sqlSessionTemplate){
		this.sqlSessionTemplate=sqlSessionTemplate;
	}
	
	private Object processSaveOrUpdate(Object[] objs,String className){
		BaseModel baseModel=(BaseModel)objs[0];
		
		//update
		if(baseModel.getId()!=null){
			return sqlSessionTemplate.update(className+".update", objs[0]);
		}
		//insert
		else{
			return sqlSessionTemplate.insert(className+".insert", objs[0]);
		}
	}

	/**
	 * 通过version的方式进行修改
	 * @param objs
	 * @param className
	 * @return
	 */
	private Object processSaveOrUpdateByVersion(Object[] objs,String className){
		BaseModel baseModel=(BaseModel)objs[0];
		
		//update
		if(baseModel.getId()!=null){
			return sqlSessionTemplate.update(className+".updateByVersion", objs[0]);
		}
		//insert
		else{
			return sqlSessionTemplate.insert(className+".insert", objs[0]);
		}
	}
	
	@Override
	public Object process(ProceedingJoinPoint pjp) {
		MethodSignature ms = (MethodSignature)pjp.getSignature();
		Method method=ms.getMethod();
		
		if(method.getName().equalsIgnoreCase(SAVE_OR_UPDATE)){
			return processSaveOrUpdate(pjp.getArgs(),ms.getDeclaringType().getName());
		}
		
		else if(method.getName().equalsIgnoreCase(SAVE_OR_UPDATE_VERSION)){
			return processSaveOrUpdateByVersion(pjp.getArgs(),ms.getDeclaringType().getName());
		}
		
		String queryPath=ms.getDeclaringType().getName()+"."+method.getName();
		return sqlSessionTemplate.selectList(queryPath,  pjp.getArgs());
	}
}
