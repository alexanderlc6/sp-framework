package com.sp.framework.registry.zk.plus.listener;


import java.util.List;

import com.sp.framework.registry.zk.plus.bean.ZkChildChangeManager;
import org.I0Itec.zkclient.IZkChildListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 子结点发生改变时，会进行通知
 * 如果你需要它生效:
 * 	1.你必须在项目的spring-zk.xml中配置他的实例
 * 	2.在给ZkClientInitBean中指定对应的 dataChildPaths
 *  3.实现 ZkChildChangeManager接口，并使它成为spring的bean
 * @author Alex Lu
 *
 */
public class IZkChildListenerImpl implements IZkChildListener {
	 
	 @Autowired
	 private ZkChildChangeManager zkChildChangeManager;
	

	@Override
	public void handleChildChange(String parentPath, List<String> currentChilds) {
		zkChildChangeManager.changeData(parentPath, currentChilds);
	}
}
