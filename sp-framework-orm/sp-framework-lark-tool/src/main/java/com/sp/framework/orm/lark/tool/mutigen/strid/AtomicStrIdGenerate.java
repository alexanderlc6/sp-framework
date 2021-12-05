package com.sp.framework.orm.lark.tool.mutigen.strid;

import com.sp.framework.orm.lark.tool.mutigen.BaseAtomicIdGenerate;
import com.sp.framework.orm.lark.tool.mutigen.MutiIdHighNumGenerateManager;
import com.sp.framework.orm.lark.tool.util.ThirtySixSystemUtil;

/**
 * 单个原子的ID生成
 * 因为考虑到性能,所以使用24个桶做并发处理，每个桶一次只能处理一个，大大增强了并发处理瓶颈
 * @author alexlu
 *
 */
public class AtomicStrIdGenerate extends BaseAtomicIdGenerate {

	
	
	public AtomicStrIdGenerate(MutiIdHighNumGenerateManager mhnm, Integer iln,
                               Integer mln) {
		super(mhnm, iln, mln);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 生成id的方法,用于在数据库表插入前使用
	 * @return
	 */

	public synchronized String genId(String classPath,String mid){
		
		int lowCount=count(classPath,mid);
		
		StringBuffer sb=new StringBuffer();
		sb.append(ThirtySixSystemUtil.toThirtySixSystem(mid));
		sb.append(ThirtySixSystemUtil.toThirtySixSystem(queryHighNum(classPath,mid)));
		sb.append(ThirtySixSystemUtil.toThirtySixSystem(lowCount));
		
		
		return sb.toString();
	}
	
	
	
}
