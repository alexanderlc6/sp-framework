package com.sp.framework.common.dto.msg;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by alexlu on 2018/8/21.
 */
@Data
public class SendMessageReqDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空!")
    private Long tenantId;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * (单个/批量)发送的用户信息列表
     */
    private List<MsgReceiverInfoDto> receiveUsersList;

    /**
     * 发送类型:1-即时消息,2-延时消息
     */
    private Integer msgSendType;

    /**
     * 消息发送/推送渠道
     * 1-APP(推送移动端APP),2-SMS(发送短信),3-WECHAT(推送微信公众号),4-MAIL(发送邮件)
     * 若传参则指定某单一渠道发送
     */
    private Integer msgSendChannelId;

    /**
     * 消息场景类别ID
     */
    @NotNull(message = "消息场景类别ID不能为空!")
    private Integer msgSceneCategoryId;

    /**
     * 消息场景编号
     */
    @NotBlank(message = "消息场景编号不能为空!")
    private String msgSceneNo;

    /**
     * 透传标签推送(以逗号分隔多个标签)
     */
    private String pushLabels;

    /**
     * 透传(用户)别名推送(以逗号分隔多个标签)
     */
    private String pushAliases;

    /**
     * 推送APN通知(是否Prod)环境标识
     */
    private Boolean pushApnsIsProd;

    /**
     * 过期时间
     */
    private String expireSendTime;

    /**
     * 通知标题
     */
    private String notificationTitle;

    /**
     * 通知内容
     */
    private String notificationContent;

    /**
     * (具体场景渠道的)通知内容模板参数集合
     * 用于替换模板内容中自定义的变量(例如:#userName,#orderNo)
     */
    private Map<String, String> notifyContentSchemaParams;

    /**
     * 透传-自定义消息标题
     */
    private String passthroughMsgTitle;

    /**
     * 透传-自定义消息内容
     */
    private String passthroughMsgContent;

    /**
     * 透传自定义消息参数列表
     */
    private Map<String, Object> passthroughMsgParams;

    @Override
    public String toString() {
        return "SendMessageReqDto{" +
                "tenantId=" + tenantId +
                ", tenantName='" + tenantName + '\'' +
                ", receiveUsersList=" + receiveUsersList +
                ", msgSendType=" + msgSendType +
                ", msgSendChannelId=" + msgSendChannelId +
                ", msgSceneCategoryId=" + msgSceneCategoryId +
                ", msgSceneNo='" + msgSceneNo + '\'' +
                ", pushLabels='" + pushLabels + '\'' +
                ", pushAliases='" + pushAliases + '\'' +
                ", pushApnsIsProd=" + pushApnsIsProd +
                ", expireSendTime='" + expireSendTime + '\'' +
                ", notificationTitle='" + notificationTitle + '\'' +
                ", notificationContent='" + notificationContent + '\'' +
                ", notifyContentSchemaParams=" + notifyContentSchemaParams +
                ", passthroughMsgTitle='" + passthroughMsgTitle + '\'' +
                ", passthroughMsgContent='" + passthroughMsgContent + '\'' +
                ", passthroughMsgParams=" + passthroughMsgParams +
                '}';
    }
}
