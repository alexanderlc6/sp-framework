package com.sp.framework.registry.zk.plus.listener;


import com.sp.framework.registry.zk.plus.bean.ZkDateChangeManager;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 数据结点发生改变时进行通知
 * 如果你需要它生效:
 * 	1.你必须在项目的spring-zk.xml中配置他的实例
 * 	2.在给ZkClientInitBean中指定对应的 dataChangePaths
 *  3.实现 ZkDateChangeManager接口，并使它成为spring的bean
 * @author Alex Lu
 *
 */
public class IZkDataListenerImpl implements IZkDataListener {

	 @Autowired
	 private ZkClient zkClient;
	 
	 @Autowired
	 private ZkDateChangeManager zkDateChangeManager;

	 @Override
	public void handleDataChange(String dataPath, Object data) throws Exception {
		zkDateChangeManager.changeData(dataPath, data);
	}

	@Override
	public void handleDataDeleted(String dataPath) throws Exception {
		zkDateChangeManager.deleteData(dataPath);
	}
}
