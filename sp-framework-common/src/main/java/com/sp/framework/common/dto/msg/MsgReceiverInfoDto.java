package com.sp.framework.common.dto.msg;

import lombok.Data;

/**
 * 接收消息的用户信息DTO
 * Created by alexlu on 2019/4/26.
 */
@Data
public class MsgReceiverInfoDto {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 微信OpenID
     */
    private String wechatOpenId;

    /**
     * 手机号
     */
    private String mobileNo;

    public MsgReceiverInfoDto() {

    }

    public MsgReceiverInfoDto(Long userId, String userCode, String userName, String wechatOpenId, String mobileNo) {
        this.userId = userId;
        this.userCode = userCode;
        this.userName = userName;
        this.wechatOpenId = wechatOpenId;
        this.mobileNo = mobileNo;
    }
}
