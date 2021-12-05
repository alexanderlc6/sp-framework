package com.sp.framework.orm.lark.orm.dao.face.condition;

import com.sp.framework.orm.lark.common.dao.QueryCondition;

/**
 * 通用Mapper接口,通过QueryCondition修改
 * @Company:yonghui
 * @Author:jiuzhou.hu@yonghui.cn
 * @Since:2016年2月17日 下午7:03:48
 * @Copyright:Copyright (c) 2016
 * @Version:1.0
 */
public interface UpdateByConditionMapper<T, PK> {
	/**
	 * 根据条件批量修改
	 * @author jiuzhou.hu@yonghui.cn
	 * @date 2016年2月17日 下午7:03:57
	 * @param cond
	 */
//	@SelectProvider(type = ConditionUpdateProvider.class, method = "dynamicSQL")
	int batchUpdate(QueryCondition cond);
}
