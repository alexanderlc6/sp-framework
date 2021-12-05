package com.sp.framework.orm.lark.tool.idgen;

import java.util.Date;

/**
 * 生成uuid
 * 分为三个部分
 * 1.2位的机器标识，范围是11-90，通过在jvm中进行配置：-Dcmid=11
 * 2.13位的时间戳，可以用到2285-12-31 23:59:59
 * 3.4位的计数器,1001到9999进行循环编号,只要一台机器不会一毫秒一张表创建8999条记录，就不会有问题
 * @author alexlu
 *
 */
public class UniqueId {
	
	private static UniqueId uid=new UniqueId();
	
	private String customMachineId=null;
	
	private int curCount=1000;
	
	private long curTimestamp=0l;
	
	private UniqueId(){
		this.customMachineId=System.getProperty("cmid");
	}
 
	
	private static String makeMachineCode() {  
      
		String cmid=uid.customMachineId;
		
		if(cmid==null){
			cmid="10";
		}
  
        return cmid;
    }  
	
	private Long makeTimestamp(){
		
		long timestamp=new Date().getTime();
		this.curTimestamp=timestamp;
		return timestamp;
	}
	
	private synchronized int count(){
		this.curCount++;
		
		if(this.curCount>9999) this.curCount=1001;
		
		
		return this.curCount;
	}
	
	private Long gen(){
		StringBuffer sb=new StringBuffer();
		sb.append(makeMachineCode());
		sb.append(makeTimestamp());
		sb.append(count());
		
		
		return Long.parseLong(sb.toString());
	}
	
	public static Long genId(){
		
		return uid.gen();
	}
	
	public static void main(String[] args){
		System.out.println(genId());
	}
}
