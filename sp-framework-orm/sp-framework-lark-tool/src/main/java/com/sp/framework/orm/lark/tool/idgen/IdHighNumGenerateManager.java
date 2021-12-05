package com.sp.framework.orm.lark.tool.idgen;

/**
 * 高位序列获取接口
 * @author alexlu
 *
 */
public interface IdHighNumGenerateManager {

	/**
	 * 获取高位长度
	 * @return
	 */
	int queryLength();
	
	/**
	 * 获取高位数值
	 * @return
	 */
	int queryHighNum();
}
