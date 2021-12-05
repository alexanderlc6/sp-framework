package com.sp.framework.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 响应DTO定义基类
 * 适用于RPC/Feign调用场景Service层响应报文体定义
 *
 * @author alex.lu
 * @date 2021/1/30 12:19
 */
@Data
@Accessors(chain = true)
public class BaseResponseDTO {
    /**
     * 记录主键ID
     */
    private Long id;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新者ID
     */
    private Long modifierId;

    /**
     * 更新时间
     */
    private Date modifiedAt;
}
