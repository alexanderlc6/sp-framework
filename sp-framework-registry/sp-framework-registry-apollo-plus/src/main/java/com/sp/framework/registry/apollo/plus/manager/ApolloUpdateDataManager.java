package com.sp.framework.registry.apollo.plus.manager;


public interface ApolloUpdateDataManager {

	/**
	 * 对Apollo中的Key-Value进行新建或者编辑，但是不会启用，必须要调用 publishNamespace方法启用
	 * 默认是application下，对其他的 clusterName和namespaceName无法生效
	 * @param key 
	 * @param value
	 * @throws Exception
	 */
	void updateOrCreateItem(String key,String value) throws Exception;
	
	/**
	 * 对Apollo中的Key-Value进行新建或者编辑，但是不会启用，必须要调用 publishNamespace方法启用 可对其他集群 namespaceName进行编辑和修改
	 * @param key
	 * @param value
	 * @param clusterName
	 * @param namespaceName
	 * @throws Exception
	 */
	void updateOrCreateItem(String key,String value,String clusterName,String namespaceName) throws Exception;

	/**
	 * 对Apollo中的Key-Value进行新建或者编辑，并且发布应用生效
	 * 默认是application下，对其他的 clusterName和namespaceName无法生效
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	void updateOrCreateItemAndPush(String key,String value) throws Exception;
	
	/**
	 * 对Apollo中的Key-Value进行新建或者编辑，但是不会启用，必须要调用 publishNamespace方法启用 可对其他集群 namespaceName进行编辑和修改
	 * @param key
	 * @param value
	 * @param clusterName
	 * @param namespaceName
	 * @throws Exception
	 */
	void updateOrCreateItemAndPush(String key,String value,String clusterName,String namespaceName) throws Exception ;

	/**
	 * 手动刷新修改的配置，并且生效
	 * @param releaseTitle   刷新发布的标题
	 * @param clusterName    集群
	 * @param namespaceName  namespaceName
	 */
	void publishNamespace(String releaseTitle,String clusterName,String namespaceName) ;
	
	/**
	 * 手动刷新应用配置，立即生效
	 * @param releaseTitle 刷新发布的主题
	 */
	void publishNamespace(String releaseTitle);
}
