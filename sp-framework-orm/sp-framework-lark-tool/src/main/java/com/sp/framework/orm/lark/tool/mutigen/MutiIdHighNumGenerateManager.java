package com.sp.framework.orm.lark.tool.mutigen;

/**
 * 高位序列获取接口
 * @author alexlu
 *
 */
public interface MutiIdHighNumGenerateManager {

	
	/**
	 * 获取高位数值
	 * key由 path-mid组成
	 *  classPath 一般指哪张表,对应model的full package
	 *  mid 这里指机器标识
	 * @return
	 */
	int queryHighNum(String classPath,String mid);
	
	/**
	 * 将class+mid与code的对应信息初始加载
	 */
	void initLoad();
}
