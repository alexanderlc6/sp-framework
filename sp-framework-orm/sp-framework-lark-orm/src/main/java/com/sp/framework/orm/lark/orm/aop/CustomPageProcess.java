package com.sp.framework.orm.lark.orm.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sp.framework.orm.lark.common.annotation.QueryPage;
import com.sp.framework.orm.lark.common.dao.Page;
import com.sp.framework.orm.lark.common.dao.PageSort;
import com.sp.framework.orm.lark.common.dao.Pagination;
import com.sp.framework.orm.lark.common.dao.Sort;
import com.sp.framework.orm.lark.orm.exception.ParameterException;
import com.sp.framework.orm.lark.orm.util.PageBeanUtilsBean;

import org.apache.ibatis.session.RowBounds;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * 自定义分页处理器
 * @author Alex Lu
 *
 */
public class CustomPageProcess extends QueryProcess {
	public CustomPageProcess(SqlSessionTemplate sqlSessionTemplate){
		this.sqlSessionTemplate=sqlSessionTemplate;
	}
	
	private Number queryCount(String path, Object filters){
		return sqlSessionTemplate.selectOne(path, filters);
	}
	
	private Map<String, Object> querySearchFilter(Object o){
		if(o instanceof Map){
			return (Map<String, Object>)o;
		}
		
		Map<String, Object> param = null;
	    try {
	      param = new PageBeanUtilsBean().describe(o);
	    } catch (IllegalAccessException e) {
	      e.printStackTrace();
	    } catch (InvocationTargetException e) {
	      e.printStackTrace();
	    } catch (NoSuchMethodException e) {
	      e.printStackTrace();
	    }
	    
	    return param;
	}
	
	/**
	 * 获取平行参数并封装
	 * @param args
	 * @return
	 */
	private PageSort queryPageSort(Object[] args){
		Map<String,Object> argMap=new HashMap<String,Object>();
		
		for(Object obj:args){
			if(obj instanceof Page) {
				argMap.put(Page.class.getName(), obj);
			} else if(obj instanceof Sort[]) {
				argMap.put(Sort[].class.getName(), obj);
			}

			argMap.put("filter", obj);
		}
		
		Map<String,Object> param=querySearchFilter(argMap.get("filter"));
		return new PageSort((Page)argMap.get(Page.class.getName()),(Sort[])argMap.get(Sort[].class.getName()),param);
	}
	
	
	@Override
	public Object process(ProceedingJoinPoint pjp){
		MethodSignature ms = (MethodSignature)pjp.getSignature();
		Method method=ms.getMethod();
		String queryListPath=ms.getDeclaringType().getName()+"."+method.getName();
		QueryPage queryPageCount=method.getAnnotation(QueryPage.class);
		
		if(queryPageCount==null) {
			throw new ParameterException("if method has Page parameter,need set annotation @QueryPageCount");
		}
		
		String queryCountPath = ms.getDeclaringType().getName()+"."+queryPageCount.value();
		PageSort pageSort = queryPageSort(pjp.getArgs());
		Page page=pageSort.getPage();

		//如果有分页
		Number count = 0;
		if(page!=null) {
			 count = queryCount(queryCountPath,pageSort.getSearchFilter());
			
			if ((count == null) || (count.intValue() <= 0)) {
			      return new Pagination(new ArrayList(), count.longValue());
			 }
		} else{
			page = new Page(RowBounds.NO_ROW_OFFSET,RowBounds.NO_ROW_LIMIT);
		}

		RowBounds rowBounds = new RowBounds(page.getStart(), page.getSize());
		List list = sqlSessionTemplate.selectList(queryListPath,  pageSort, rowBounds);

		if(count.equals(0)) {
			count = list.size();
		}

		int pageCount=(count.intValue() + page.getSize() - 1)/page.getSize();
		return new Pagination(list, count.longValue(), page.getStartPage(), pageCount, page.getStart(), page.getSize());
	}
}
