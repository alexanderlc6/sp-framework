package com.sp.framework.orm.lark.tool.mutigen.numid;

import java.util.ArrayList;
import java.util.List;

import com.sp.framework.orm.lark.tool.mutigen.MutiIdHighNumGenerateManager;
import com.sp.framework.orm.lark.tool.mutigen.ZkMachineIdGenerate;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 多表主键生成策略
 * model的class与机器标识来进行定位
 * 为了提高同步的性能，使用了24个桶进行拆分线程同步锁
 * 每个桶会使用一把同步锁，通过class+机器标识的hashcode % 桶数量 来决定用哪个桶
 * @author alexlu
 *
 */
public class MutiIdGenerate {

	private List<AtomicIdGenerate> idgenList=new ArrayList<AtomicIdGenerate>();
	
	
	@Autowired
	private ZkMachineIdGenerate zkMachineIdGenerate;

	public MutiIdGenerate(Integer initLowNum, Integer maxLowNum, MutiIdHighNumGenerateManager mihng){
		
		for(int i=0;i<24;i++){
			AtomicIdGenerate aig=new AtomicIdGenerate(mihng,initLowNum,maxLowNum);
			idgenList.add(aig);
		}
		
	}
	

	
	/**
	 * 生成id的方法,用于在数据表新增前使用
	 * @return
	 */
	public Long genId(Class<?> clazz){
		
		
		return genId(clazz.getName());
	}
	
	public Long genId(String classPath){
		
		String mid=zkMachineIdGenerate.getMid();
		String key=classPath+"-"+mid;
		

		int num=Math.abs(key.hashCode());

		int i=num%idgenList.size();
		
		
		AtomicIdGenerate aig=idgenList.get(i);
		
		return aig.genId(classPath, mid);
	}
	
	

}
