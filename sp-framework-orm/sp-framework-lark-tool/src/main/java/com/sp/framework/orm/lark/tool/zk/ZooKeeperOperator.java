package com.sp.framework.orm.lark.tool.zk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZooKeeperOperator  {
	private static Log log = LogFactory.getLog(ZooKeeperOperator.class.getName());  
	private static final int SESSION_TIME   = 10000;      

	public static final String LIFECYCLE_NODE="/applife/app";
	
	private static ZooKeeper zk=null;
	
	private String hosts=null;
	
	private Watcher watcher=new MyWatcher(); 
	
	private static ZooKeeperOperator zko=null;
	
	public static ZooKeeperOperator getInstance(){

		return  zko;
	}
	
	public static ZooKeeperOperator getInstance(String host){
		try {
			if(zko==null)	zko=new ZooKeeperOperator(host);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("zk connection error");
			
		}
		return  zko;
	}
	
	/**
	 * WathControl 继承于Watch, 在使用zk.exists("path",true)进行观察时，使用此watch来处理通知
	 * 调用watch中的process方法
	 * @param hosts
	 * @param list
	 * @throws Exception
	 */
	public ZooKeeperOperator(String hosts) throws Exception{
		
		 this.zk=new ZooKeeper(hosts,SESSION_TIME,watcher);
		 lifecycle();
		 
		 this.hosts=hosts;
	}
	
	
	public ZooKeeperOperator(String hosts,Watcher watcher) throws Exception{
		
		 this.zk=new ZooKeeper(hosts,SESSION_TIME,watcher);
		 lifecycle();
		 
		 this.hosts=hosts;
	}
	
	 /**
	  * session 超时后，进行重连
	  */
	 public	void reConnection()
	 {
	 	 try {
	 	
				zk.close();
				
				zk = new ZooKeeper(hosts,SESSION_TIME,watcher);
				
				log.debug("------re create zookeeper connection");
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	  
	 }
	
	public void initWatch(){
	//	wc.initWatch();
	}
 
	/**
	 * 获取上一级结点
	 * @param fullNode
	 * @return
	 */
	private String queryPreLevelNode(String fullNode){
		String result=fullNode;
		
		if(fullNode==null) return null;
		
		int index=fullNode.lastIndexOf("/");
		if(index>0){
			result=result.substring(0,index); 
		}
		
		return result;
		
	}
 
	private void lifecycle(){
		String data=String.valueOf(System.currentTimeMillis());
		try {
			String basePath=queryPreLevelNode(LIFECYCLE_NODE);
			Stat stat=getZk().exists(basePath, false);
			if(stat==null) {
				getZk().create(basePath,data.getBytes() , Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			
			getZk().create(LIFECYCLE_NODE,data.getBytes() , Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			//this.exists(LIFECYCLE_NODE, true);
			
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 通知zkServer的path 发生了数据变改
	 * 一般用于pts通知zk的服务
	 * @return
	 */
	public boolean noticeZkServer(String path){
		
		String data=String.valueOf(System.currentTimeMillis());
		noticeZkServer(path,data);
		
		return true;
	}
	
	/**
	 * 通知zkServer的path 发生了数据变改
	 * 一般用于pts通知zk的服务
	 * @param path 路径
	 * @param data 前后双方约定的数据
	 * @return
	 */
	public boolean noticeZkServer(String path,String data){
	
		try{
			getZk().setData(path, data.getBytes(), -1);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	public void makeNodeIfNotExists(String path){
		
		String[] strs=path.split("/");
		
		String lastNode="";
		try {
			for(int i=1;i<strs.length;i++){
				String str=strs[i];
				lastNode=lastNode+"/"+str;
				Stat stat = getZk().exists(lastNode, false);
					
				if(stat==null){
					getZk().create(lastNode,"config".getBytes() , Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
			}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ZooKeeper getZk() {
		return zk;
	}

	public void setZk(ZooKeeper zk) {
		this.zk = zk;
	}
	
	

}
