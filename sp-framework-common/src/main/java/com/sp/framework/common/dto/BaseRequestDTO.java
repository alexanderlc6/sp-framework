package com.sp.framework.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 请求DTO定义基类
 * 适用于RPC/Feign调用场景Service层请求报文体定义
 *
 * @author alex.lu
 * @date 2021/1/30 12:19
 */
@Data
@Accessors(chain = true)
public class BaseRequestDTO {
    /**
     * SAAS租户号
     */
    private Long saasTenantCode;
}
