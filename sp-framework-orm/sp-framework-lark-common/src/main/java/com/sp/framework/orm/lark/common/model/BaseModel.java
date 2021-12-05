package com.sp.framework.orm.lark.common.model;

import java.io.Serializable;

/**
 * 基本模型定义
 */
public class BaseModel implements Serializable {
	private static final long serialVersionUID = -5253902746677991352L;

	/**
	 * 主键ID
	 */
	protected Long id;
	
	public BaseModel(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
