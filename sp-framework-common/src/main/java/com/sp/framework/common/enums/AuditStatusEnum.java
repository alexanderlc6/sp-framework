package com.sp.framework.common.enums;

import java.util.Objects;

/**
 * 账号状态
 * Created by alexlu on 2017/9/21
 */
public enum AuditStatusEnum {
    /**
     * 审核状态
     */
    WAIT_AUDIT(1,"等待审核"),
    AUDIT_PASSED(2,"审核通过"),
    AUDIT_REFUSE(3,"审核拒绝"),

    /**
     * 禁用
     */
    FORBIDDEN(4,"禁用");

    private Integer userStatus;
    private String userStatusDesc;

    AuditStatusEnum(Integer userStatus, String userStatusDesc){
        this.userStatus = userStatus;
        this.userStatusDesc = userStatusDesc;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserStatusDesc() {
        return userStatusDesc;
    }

    public void setUserStatusDesc(String userStatusDesc) {
        this.userStatusDesc = userStatusDesc;
    }

    public static String valueOf(Integer value) {
        if (value == null) {
            return "";
        } else {
            for (AuditStatusEnum ms : AuditStatusEnum.values()) {
                if (Objects.equals(ms.getUserStatus(), value)) {
                    return ms.getUserStatusDesc();
                }
            }

            return "";
        }
    }
}
