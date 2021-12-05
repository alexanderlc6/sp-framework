package com.sp.framework.orm.lark.tool.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 调用
 * @author alexlu
 *
 */
public abstract class WatchInvoke {
	
	private Watcher watcher;

	@Autowired
	protected ZooKeeperOperator zooKeeperOperator;
	/**
	 * 确认是否匹配路径及类型
	 * 只有返回TRUE后，才会调用，并且重新注册watch
	 * @param path
	 * @return
	 */
	public abstract boolean isMatch(String path,EventType type);
	
	/**
	 * 是否需要继续监听
	 * @param path
	 * @param type
	 * @param state
	 * @return
	 */
	public abstract boolean needContinueWatch(String path,EventType type,KeeperState state);
	
	
	/**
	 * 是否需要初始监听
	 * @return
	 */
	public abstract String getListenPath();
	

	
	public void initListen(){
		try {
			
			
			Stat stat=zooKeeperOperator.getZk().exists(getListenPath(), false);
			if(stat==null) {
				String data=String.valueOf(System.currentTimeMillis());
				zooKeeperOperator.getZk().create(getListenPath(),data.getBytes() , Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			zooKeeperOperator.getZk().exists(getListenPath(), getWatcher());
			//初始化时进行调用
			invoke(getListenPath());
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void watchAgain(String path,EventType type,KeeperState state){
		
		if(path!=null&&needContinueWatch(path,type,state)){
			try {
				zooKeeperOperator.getZk().exists(path, getWatcher());
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * session超时后，再进行重新注册
	 */
	public void watchAgainByReconn(){
		
		
			try {
				zooKeeperOperator.getZk().exists(getListenPath(), getWatcher());
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public void notice(WatchedEvent event){
		
		if(event.getState()==KeeperState.SyncConnected){  
			invoke(event.getPath());
		}
		
	}
	
	/**
	 * 被通知后调用的实现
	 * @param path
	 */
	public abstract void invoke(String path);

	protected Watcher getWatcher() {
		return watcher;
	}

	protected void setWatcher(Watcher watcher) {
		this.watcher = watcher;
	}
	
	
}
