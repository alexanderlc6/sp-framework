package com.sp.framework.orm.lark.orm.dao.face.condition;

import com.sp.framework.orm.lark.common.dao.QueryCondition;

/**
 * 通用Mapper接口,通过QueryCondition删除
 * @Company:yonghui
 * @Author:jiuzhou.hu@yonghui.cn
 * @Since:2016年2月17日 下午7:02:16
 * @Copyright:Copyright (c) 2016
 * @Version:1.0
 */
public interface DeleteByConditionMapper<T, PK> {
	
	/**
	 * 根据条件批量删除
	 * @author jiuzhou.hu@yonghui.cn
	 * @date 2016年2月17日 下午7:02:28
	 * @param cond
	 */
//	@SelectProvider(type = ConditionDeleteProvider.class, method = "dynamicSQL")
	int batchDelete(QueryCondition cond);
}
