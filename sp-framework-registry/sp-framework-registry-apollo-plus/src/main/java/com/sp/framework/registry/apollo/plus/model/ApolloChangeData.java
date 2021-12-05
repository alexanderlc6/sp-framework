package com.sp.framework.registry.apollo.plus.model;

import java.io.Serializable;

/**
 * Apollo配置项定义POJO
 */
public class ApolloChangeData implements Serializable{
	private static final long serialVersionUID = -4300895557431682811L;

	/**
	 * Apollo命名空间
	 */
	private String namespace;

	/**
	 * 配置路径
	 */
	private String path;

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
