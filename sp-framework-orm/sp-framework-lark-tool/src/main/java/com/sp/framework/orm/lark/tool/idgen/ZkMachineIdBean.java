package com.sp.framework.orm.lark.tool.idgen;

import java.util.Date;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 生成机器标识
 * @author alexlu
 *
 */
public class ZkMachineIdBean  {

	@Autowired
	private ZkClient zkClient;
	
	/**
	 * 生成机器标识的zk根路径，如：
	 * /machineid/bizhub/dev/
	 * bizhub为项目
	 * dev为环境
	 */
	private String midRootPath;
	
	/**
	 * 机器标识的位数(建议3位)
	 */
	private Integer length; 
	
	
	
	public String getMidRootPath() {
		return midRootPath;
	}


	public void setMidRootPath(String midRootPath) {
		this.midRootPath = midRootPath;
	}


	public Integer getLength() {
		return length;
	}


	public void setLength(Integer length) {
		this.length = length;
	}

	
	/**
	 * 获取mid
	 * @return
	 * @throws Exception
	 */
	public String makeMid() throws Exception {
		// TODO Auto-generated method stub

		String time=""+new Date().getTime();
		
		if(!midRootPath.endsWith("/")){
			throw new Exception("machine id zk path must end of / :"+midRootPath);
		}
		
		String value=zkClient.createEphemeralSequential(midRootPath, time);
		
		String[] array=value.split("/");
		
		String data=array[array.length-1];
		
		if(!data.matches("\\d+")){
			throw new Exception("machine id make node error :"+value);
		}
		
		int num=Integer.parseInt(data);
		
		int base=new Double(Math.pow(10,length )).intValue()-10;
		
		int result=num%(base)+10;
		
		return ""+result;
		
	}

}
