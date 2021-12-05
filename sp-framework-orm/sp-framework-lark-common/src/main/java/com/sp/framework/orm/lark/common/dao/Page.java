package com.sp.framework.orm.lark.common.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("分页查询分页条件对象")
public class Page implements Serializable{
	private static final long serialVersionUID = -6900835810451940829L;

	@ApiModelProperty(value = "分页每页记录数", required = true)
	private int size = 20;

	@ApiModelProperty(value = "分页起始页", required = true)
	private int startPage = 1;

	public Page(){}
	
	public Page(int startPage, int size){
		this.startPage=startPage;
		this.size = size;
	}

	@ApiModelProperty(value = "分页起始记录顺序号", hidden = true)
	public int getStart() {
		return (startPage - 1)*size;
	}
	public void setStart(int start) {
		this.startPage=start/size+1;
	}

	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * startPage的获取.
	 * @return int
	 */
	public int getStartPage() {
		return startPage;
	}

	/**
	 * 设定startPage的值.
	 * @param startPage
	 */
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
}
