package com.sp.framework.common.enums;

import java.util.Objects;

/**
 * 消息发送渠道枚举
 * Created by luchao on 2017/9/21
 */
public enum MsgSendChannelEnum {
    /**
     * 消息发送渠道
     */
    APP(1,"APP通知推送"),
    SMS(2,"短信发送"),
    WECHAT(3,"微信公众号推送"),
    MAIL(4,"邮件发送"),
    INNER_SYS_MSG(5,"系统消息广播");

    /**
     * 消息发送渠道ID
     */
    private Integer msgSendChannelId;

    /**
     * 消息发送渠道名称
     */
    private String msgSendChannelName;

    MsgSendChannelEnum(Integer msgSendChannelId, String msgSendChannelName){
        this.msgSendChannelId = msgSendChannelId;
        this.msgSendChannelName = msgSendChannelName;
    }

    public Integer getMsgSendChannelId() {
        return msgSendChannelId;
    }

    public void setMsgSendChannelId(Integer msgSendChannelId) {
        this.msgSendChannelId = msgSendChannelId;
    }

    public String getMsgSendChannelName() {
        return msgSendChannelName;
    }

    public void setMsgSendChannelName(String msgSendChannelName) {
        this.msgSendChannelName = msgSendChannelName;
    }

    public static String valueOf(Integer value) {
        if (value == null) {
            return "";
        } else {
            for (MsgSendChannelEnum ms : MsgSendChannelEnum.values()) {
                if (Objects.equals(ms.getMsgSendChannelId(), value)) {
                    return ms.getMsgSendChannelName();
                }
            }

            return "";
        }
    }

    /**
     * 根据消息发送渠道ID获取枚举对象
     * @param msgSendChannelId
     * @return
     */
    public static MsgSendChannelEnum getValueByCode(Integer msgSendChannelId){
        for(MsgSendChannelEnum communityMemberStatusEnum : MsgSendChannelEnum.values()){
            if(communityMemberStatusEnum.getMsgSendChannelId().equals(msgSendChannelId)){
                return communityMemberStatusEnum;
            }
        }

        return null;
    }
}
