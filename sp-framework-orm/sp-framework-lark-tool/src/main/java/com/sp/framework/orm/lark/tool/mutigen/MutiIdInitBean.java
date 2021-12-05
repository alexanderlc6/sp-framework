package com.sp.framework.orm.lark.tool.mutigen;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 初始化的处理
 * @author alexlu
 *
 */
public class MutiIdInitBean implements InitializingBean {

	@Autowired
	private ZkMachineIdGenerate zkMachineIdGenerate;
	
	@Autowired
	private MutiIdHighNumGenerateManager mutiIdHighNumGenerateManager;
	
	/**
	 * 1.machine id的初始化，id的争抢
	 * 2.获取high的计数
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
			
		zkMachineIdGenerate.init();
		
		mutiIdHighNumGenerateManager.initLoad();
	}

}
