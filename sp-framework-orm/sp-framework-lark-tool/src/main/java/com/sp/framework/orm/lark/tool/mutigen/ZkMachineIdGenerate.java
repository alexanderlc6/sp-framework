package com.sp.framework.orm.lark.tool.mutigen;

import java.util.Date;
import java.util.List;

import com.sp.framework.utilities.PathUtil;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ZkMachineIdGenerate {
	
	private static final Logger log = LoggerFactory.getLogger(ZkMachineIdGenerate.class);

	@Autowired
	private ZkClient zkClient;
	
	/**
	 * 机器标识的zk根路径，如：
	 * /machineid/bizhub/dev/
	 * bizhub为项目
	 * dev为环境
	 */
	private String midRootPath;
	
	/**
	 * 机器id的待选列表
	 * 
	 */
	private List<String> midList=null;
	
	/**
	 * 抢到的机器标识
	 */
	private String mid;
	
	
	

	public String getMid() {
		return mid;
	}


	public void setMid(String mid) {
		this.mid = mid;
	}


	public ZkClient getZkClient() {
		return zkClient;
	}


	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}


	public String getMidRootPath() {
		return midRootPath;
	}


	public void setMidRootPath(String midRootPath) {
		this.midRootPath = midRootPath;
	}


	public List<String> getMidList() {
		return midList;
	}


	public void setMidList(List<String> midList) {
		this.midList = midList;
	}

	/**
	 * 1.检查machine id对应的zk 结点是否存在，不存在则创建
	 * 2.争抢zk结点资源，抢到则完成初始化
	 * 3.60秒还未抢到资源，则报错
	 */
	public void init(){
		
		try{
			//检查mid标识结点是否存在，否则创建结点
			for(String mid:midList){
				
				String path= PathUtil.processFolder(midRootPath)+mid;
				
				if(!zkClient.exists(path)){
					zkClient.createPersistent(path, new Date().getTime());
				}
				
			}
		
		}
		catch(Exception e){
			
			log.error(e.getMessage());
			
			throw new RuntimeException(" machine id root path no exists,please create it [by justin]");
		}
		
		int index=0;
		
		long startTime=new Date().getTime();
		
		while(1==1){
			
			String mid=midList.get(index);
			
			String path=PathUtil.processFolder(midRootPath)+mid;
			
			List<String> children=zkClient.getChildren(path);
			
			//如果没子结点,才需要争抢资源
			if(children.size()==0){
				
				//创建一个序列子结点
				String child=zkClient.createEphemeralSequential(path+"/", new Date().getTime());
				
				String lastLevel=PathUtil.findLastLevelFolder(child);
				
				List<String> children2=zkClient.getChildren(path);
				
				//只有当序列子结点为第一个时，才表示已抢到资源，抢到则继续
				if(lastLevel.equals(children2.get(0))){
					this.mid=mid;
					log.info("use machine id:"+mid+" [by justin]");
					return ;
				}
				else{
					zkClient.delete(child);
				}
				
			}
			
			//如果60秒还未抢到资源，则报错
			if(new Date().getTime()-startTime>60000){
				throw new RuntimeException("need enough machine id resource,please create more[by justin]");
			}
			try{
				
				Thread.sleep(1000l);
				log.info("sleep ..1 s");
				
			}
			catch(Exception e){
				
				log.error(e.getMessage());
				
			}
			index++;
			//进行循环检索，30秒的时间
			if(index>=midList.size()) index=0;
		}
		
	}

}
