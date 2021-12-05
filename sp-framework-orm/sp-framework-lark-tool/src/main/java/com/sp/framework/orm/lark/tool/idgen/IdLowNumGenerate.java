package com.sp.framework.orm.lark.tool.idgen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 低位序列的初始值以1为开头,保证定长
 * 监界值表示到了多少时需要更新高位序列，可以传多个值,按照从小到大升序存放,监界值之间最好保证上万的间隔，免得更新高位时出错
 * 
 * 
 * @author alexlu
 *
 */
public class IdLowNumGenerate implements InitializingBean{
	
	private static final Logger log = LoggerFactory.getLogger(IdLowNumGenerate.class);

	@Autowired
	private ZkMachineIdBean zkMachineIdBean;
	
	@Autowired
	private IdHighNumGenerateManager idHighNumGenerateManager;
	
	
	/**
	 * 更新高位的临界值
	 */
	private List<Integer> criticalList=new ArrayList<Integer>();
	
	/**
	 * 初始低位序列值 
	 */
	private Integer initLowNum=1000;
	
	
	/**
	 * 机器标识
	 */
	private String mid;
	
	
	/**
	 * 高位的序列值
	 */
	private int highNum=1000;
	
	
	/**
	 * 当前的低位数值
	 */
	private int curLowNum=1000;
	
	/**
	 * 已处理过的临界值
	 */
	private int lastCritical=0;

	

	private synchronized int count(){
		
		//表示超过临界时，进行异步刷新
		if(checkCritical()){
		
			UpdateHighThread uht=new UpdateHighThread(this);
			
			Thread t1=new Thread(uht);
			
			t1.start();
		}
		
		this.curLowNum++;
		
		return this.curLowNum;
	}
	
	/**
	 * 检查是否超过临界值,true：已超过
	 * @return
	 */
	private boolean checkCritical(){
		
		for(Integer c:this.criticalList){
			//lastCritical表示已经处理过的监界值
			if(c>lastCritical&&this.curLowNum>c){
				lastCritical=c;
				return true;
			}
		}
		
		return false;
		
	}
	
	/**
	 * 通过异步方式调用此方法
	 */
	public void updateHighNum(){
		
		Integer high=idHighNumGenerateManager.queryHighNum();
		
		queryForUpdate(high);
		
	}
	
	private synchronized int queryForUpdate(Integer high){
		
		if(high!=null){

			this.highNum=high;
			this.curLowNum=initLowNum;
			this.lastCritical=0;
		}
		
		return this.highNum;
	}
	
	private int queryHighNum(){
		
		
		return queryForUpdate(null);
	}
	
	/**
	 * 生成id的方法,用于在数据库中插入前使用
	 * @return
	 */
	public Long genId(){
		
		StringBuffer sb=new StringBuffer();
		sb.append(mid);
		sb.append(queryHighNum());
		sb.append(count());
		
		
		
		return Long.parseLong(sb.toString());
	}
	

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		this.mid=zkMachineIdBean.makeMid();
		
		this.highNum=idHighNumGenerateManager.queryHighNum();
		
		Collections.sort(this.criticalList);
	}
}
