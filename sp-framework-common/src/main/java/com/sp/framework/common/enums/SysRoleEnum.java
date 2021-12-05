package com.sp.framework.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 系统角色枚举(1-SuperAdmin,2-Admin,3-NormalUser)
 * Created by luchao on 2017/10/27
 */
public enum SysRoleEnum {
    /**
     * 超级管理员
     */
    SUPER_ADMIN(1,"SuperAdmin","超级管理员"),
    ADMIN(2,"Admin","管理员"),
    SUB_ADMIN(3,"SubAdmin","子管理员");

    private Integer sysRoleType;
    private String sysRoleName;
    private String sysRoleDesc;

    SysRoleEnum(Integer sysRoleType, String sysRoleName, String sysRoleDesc){
        this.sysRoleType = sysRoleType;
        this.sysRoleName = sysRoleName;
        this.sysRoleDesc = sysRoleDesc;
    }

    public Integer getSysRoleType() {
        return sysRoleType;
    }


    public String getSysRoleName() {
        return sysRoleName;
    }


    public String getSysRoleDesc() {
        return sysRoleDesc;
    }


    public static String valueOf(Integer roleType) {
        if (roleType == null) {
            return "";
        } else {
            for (SysRoleEnum ms : SysRoleEnum.values()) {
                if (Objects.equals(ms.getSysRoleType(), roleType)) {
                    return ms.getSysRoleName();
                }
            }

            return "";
        }
    }

    /**
     * 获取低优先级所属的所有可访问角色
     * @param curSysRoleName
     * @return
     */
    public static String [] getAccessRoles(String curSysRoleName){
        if (StringUtils.isEmpty(curSysRoleName)) {
            return null;
        } else {
            Set<String> rolesResult = new HashSet<>();
            for (SysRoleEnum ms : SysRoleEnum.values()) {
                if (Objects.equals(ms.getSysRoleName(), curSysRoleName)) {
                    Integer curSysRoleId = ms.getSysRoleType();
                    for (SysRoleEnum tmp : SysRoleEnum.values()) {
                        if(tmp.getSysRoleType() <= curSysRoleId){
                            rolesResult.add(tmp.getSysRoleName());
                        }
                    }

                    return (String[]) rolesResult.toArray();
                }
            }

            return null;
        }
    }

    /**
     * 获取后台管理系统角色code集合（其他角色不能登录后台管理系统）
     * @return
     */
    public static List<String> getBackendRoles() {
        List<String> roles = new ArrayList<>();
        roles.add(SUPER_ADMIN.getSysRoleName());
        roles.add(ADMIN.getSysRoleName());
        roles.add(SUB_ADMIN.getSysRoleName());
        return roles;
    }
}
