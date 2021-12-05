package com.sp.framework.orm.lark.common.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel("分页查询返回对象")
public class Pagination<T> implements Serializable{
	private static final long serialVersionUID = 520741651051423364L;

	public Pagination(){}
	public Pagination(List<T> items, long count){
		this.items = items;
		this.count = count;
	}

	public Pagination(List<T> items, long count, int currentPage, int totalPages, int start, int size){
		this.items = items;
		this.count = count;
		this.currentPage = currentPage;
		this.totalPages = totalPages;
		this.start = start;
		this.size = size;
	}

	@ApiModelProperty("分页每页数据集合")
	private List<T> items;

	@ApiModelProperty("查询返回数据总数")
	private long count;

	@ApiModelProperty("查询返回当前页序号")
	private int currentPage;

	@ApiModelProperty("查询返回数据总页数")
	private int totalPages;

	@ApiModelProperty("查询起始记录序号")
	private int start;

	@ApiModelProperty("分页查询当前页数据行数")
	private int size;

	@ApiModelProperty("查询SQL排序语句")
	private String sortStr;
		
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getSortStr() {
		return sortStr;
	}
	public void setSortStr(String sortStr) {
		this.sortStr = sortStr;
	}
	public boolean isFirstPage(){
		return getCurrentPage() == 1;
	}
	public boolean isLastPage(){
		return getCurrentPage() >= getTotalPages();
	}
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
}
