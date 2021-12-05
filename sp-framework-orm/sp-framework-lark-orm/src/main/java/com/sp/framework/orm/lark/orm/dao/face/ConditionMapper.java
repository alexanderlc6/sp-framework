package com.sp.framework.orm.lark.orm.dao.face;

import com.sp.framework.orm.lark.orm.dao.face.condition.DeleteByConditionMapper;
import com.sp.framework.orm.lark.orm.dao.face.condition.SelectByConditionMapper;
import com.sp.framework.orm.lark.orm.dao.face.condition.UpdateByConditionMapper;

/**
 * 通用Mapper接口,通过QueryCondition操作
 * @Company:yonghui
 * @Author:jiuzhou.hu@yonghui.cn
 * @Since:2016年2月17日 下午7:04:08
 * @Copyright:Copyright (c) 2016
 * @Version:1.0
 */
public interface ConditionMapper<T, PK> 
											extends DeleteByConditionMapper<T, PK>
											, SelectByConditionMapper<T, PK>
											, UpdateByConditionMapper<T, PK>{

	
}
