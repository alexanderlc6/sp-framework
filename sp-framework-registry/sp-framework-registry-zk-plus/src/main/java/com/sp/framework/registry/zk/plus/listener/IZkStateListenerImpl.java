package com.sp.framework.registry.zk.plus.listener;


import com.sp.framework.registry.zk.plus.bean.ZkStateChangeManager;
import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * zk连接状态发生改变时进行通知
 * 如果你需要它生效:
 * 	1.你必须在项目的spring-zk.xml中配置他的实例
 * 	2.在给ZkClientInitBean中subscribeStateChange 设为true
 *  3.实现 ZkStateChangeManager接口，并使它成为spring的bean
 * @author Alex Lu
 *
 */
public class IZkStateListenerImpl implements IZkStateListener {
	
	
	protected static final Logger logger = LoggerFactory.getLogger(IZkStateListenerImpl.class);
	
	@Autowired
	private ZkStateChangeManager zkStateChangeManager;
	 
	/**
	 * Disconnected 表示断开
	 * SyncConnected 表示重连上
	 * Expired session过期
	 * 
	 */
	@Override
	public void handleStateChanged(KeeperState state) throws Exception {
		if(KeeperState.Disconnected.equals(state)){
			zkStateChangeManager.handlerDisconnected();
		}
		else if(KeeperState.SyncConnected.equals(state)){
			zkStateChangeManager.handlerSyncConnected();
		}
		
		else if(KeeperState.Expired.equals(state)){
			zkStateChangeManager.handlerExpired();
		}
		
		zkStateChangeManager.handleStateChanged(state);
	}

	/**
	 * session过期重新创建以后
	 * 
	 */
	@Override
	public void handleNewSession() throws Exception {
		zkStateChangeManager.handleNewSession();
	}
}
