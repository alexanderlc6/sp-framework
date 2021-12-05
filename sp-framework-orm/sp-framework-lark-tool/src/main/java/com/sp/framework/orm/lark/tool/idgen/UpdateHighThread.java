package com.sp.framework.orm.lark.tool.idgen;

public class UpdateHighThread implements Runnable {

	
	private IdLowNumGenerate idLowNumGenerate;
	
	
	public UpdateHighThread(IdLowNumGenerate ihng){
		this.idLowNumGenerate=ihng;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		idLowNumGenerate.updateHighNum();
	}

}
