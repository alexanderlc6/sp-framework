package com.sp.framework.common.enums;

/**
 * @description: 业务枚举接口
 * @author: luchao
 * @date: Created in 6/9/21 5:47 PM
 */
public interface BaseBizEnum {
    /**
     * 业务返回码
     * @return
     */
    Integer getCode();

    /**
     * 业务返回文本
     * @return
     */
    String getMessage();
}
