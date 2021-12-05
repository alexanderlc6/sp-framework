package com.sp.framework.registry.zk.plus.bean;

import java.util.List;

import org.apache.zookeeper.Watcher.Event.KeeperState;

/**
 * zk的连接状态发生改变后调用此方法
 * @author Alex Lu
 *
 */
public interface ZkStateChangeManager {

	/**
	 * session过期重新创建以后
	 * @throws Exception
	 */
	public void handleNewSession() throws Exception;
	/**
	 * 以下三个状态都会在个方法中出现
	 * 与下方三个方法并行进行,如断开连接时，handlerDisconnected会被触发，同时本方法也会被触发
	 * Disconnected 表示断开
	 * SyncConnected 表示重连上
	 * Expired session过期
	 * @param state
	 * @throws Exception
	 */
	public void handleStateChanged(KeeperState state) throws Exception;
	
	/**
	 * 断开连接 (一般是指在短期的断开连接)
	 * @throws Exception
	 */
	public void handlerDisconnected() throws Exception;
	
	/**
	 * 重新连接上 (过期之前重新连上)
	 * @throws Exception
	 */
	public void handlerSyncConnected() throws Exception;
	
	/**
	 * 过期 (过期了，创建的临时结点会消失)
	 * @throws Exception
	 */
	public void handlerExpired() throws Exception;
	
	
	
}
