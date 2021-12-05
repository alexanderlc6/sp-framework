package com.sp.framework.registry.zk.plus.bean;



import java.util.List;

/**
 * 当某个path的数据发生改变后调用此方法
 * @author Alex Lu
 *
 */
public interface ZkChildChangeManager {
	void changeData(String parentPath, List<String> currentChilds);
}
