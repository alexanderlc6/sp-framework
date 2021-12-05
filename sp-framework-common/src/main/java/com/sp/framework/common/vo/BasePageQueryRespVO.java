package com.sp.framework.common.vo;

import com.yh.common.lark.common.dao.Pagination;
import lombok.Data;

/**
 * @description: 基础分页返回VO类
 * @author: luchao
 * @date: Created in 8/5/21 3:12 PM
 */
@Data
public class BasePageQueryRespVO<T> extends Pagination<T> {
}
