package com.sp.framework.common.utils.treeData;

import java.util.List;

/**
 * @author lzk
 * @date 2017/11/7
 */
public interface ITree {

    /**
     * 获取节点ID
     * @return
     */
    Long findTreeId();

    /**
     * 获取节点父ID
     * @return
     */
    Long findParentTreeId();

    /**
     * 获取子树列表
     * @return
     */
    List<ITree> getChildList();

    /**
     * 设置子树列表
     * @param childList
     */
    void setChildList(List<ITree> childList);
}
