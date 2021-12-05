package com.sp.framework.common.vo;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserRolePermissionVO {
    /**
     * 用户对应的角色名称列表
     */
    List<String> roles = new ArrayList();

    /**
     * 用户对应的权限编码列表
     */
    List<String> permissionns = new ArrayList();
}
