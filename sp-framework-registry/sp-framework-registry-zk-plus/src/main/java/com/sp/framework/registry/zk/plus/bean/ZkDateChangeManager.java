package com.sp.framework.registry.zk.plus.bean;

/**
 * 当某个path的数据发生改变后调用此方法
 * @author Alex Lu
 *
 */
public interface ZkDateChangeManager {

	void changeData(String dataPath,Object data);
	
	void deleteData(String dataPath);
		
}
