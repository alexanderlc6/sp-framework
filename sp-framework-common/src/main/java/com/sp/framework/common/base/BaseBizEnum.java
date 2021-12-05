package com.sp.framework.common.base;

/**
 * @description: 基础业务枚举
 * @author: luchao
 * @date: Created in 6/22/21 10:41 PM
 */
public interface BaseBizEnum {
    /**
     * 获取业务返回码
     * @return
     */
    Integer getCode();

    /**
     * 获取业务返回信息
     * @return
     */
    String getMsg();
}
