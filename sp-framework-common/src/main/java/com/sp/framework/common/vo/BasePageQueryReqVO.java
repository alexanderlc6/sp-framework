package com.sp.framework.common.vo;

import com.yh.common.lark.common.dao.Page;
import com.yh.common.lark.common.dao.Sort;
import lombok.Data;

import java.util.Map;


/**
 * 基础查询VO定义
 * @author alex.lu
 *@creattime 2020-11-24
 */
@Data
public class BasePageQueryReqVO {
	public static final int DEFULT_START = 0;
	public static final int DEFULT_SIZE = 10;
	
	private Page page = new Page();

	/**
	 * 排序
	 */
	private Sort[] sorts;

	/**
	 * 查询参数集合
	 */
	private Map<String, Object> params;

	/**
	 * 排序属性设置集合
	 */
	private Map<String,String> sortDirection;

	public Map<String, String> getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(Map<String, String> sortDirection) {
		this.sortDirection = sortDirection;
	}
	public Sort[] getSorts() {
		return sorts;
	}
	public void setSorts(Sort[] sorts) {
		this.sorts = sorts;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	public Page getPage(){
		return page;
	}
	
	public void setPage(Page page){
		this.page = page;
	}
}
