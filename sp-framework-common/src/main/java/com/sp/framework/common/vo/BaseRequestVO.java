package com.sp.framework.common.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 请求信息VO基类
 * 适用于与前台界面交互请求属性定义
 *
 * @author alex.lu
 * @date 2021/1/30 12:19
 */
@Data
public class BaseRequestVO {
    /**
     * SaaS租户号
     */
    @NotBlank(message = "SAAS租户编码不能为空!")
    private String saasTenantCode;
}
