package com.sp.framework.orm.lark.common.dao;

import java.util.Map;

public class PageSort {

	private Page page;
	
	private Sort[] sorts;
	
	private Map<String,Object> searchFilter;
	
	public PageSort(Page page,Sort[] sorts,Map<String,Object> searchFilter){
		this.page=page;
		this.sorts=sorts;
		this.searchFilter=searchFilter;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Sort[] getSorts() {
		return sorts;
	}

	public void setSorts(Sort[] sorts) {
		this.sorts = sorts;
	}

	public Map<String, Object> getSearchFilter() {
		return searchFilter;
	}

	public void setSearchFilter(Map<String, Object> searchFilter) {
		this.searchFilter = searchFilter;
	}
}
