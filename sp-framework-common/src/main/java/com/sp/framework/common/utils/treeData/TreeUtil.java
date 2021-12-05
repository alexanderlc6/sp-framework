package com.sp.framework.common.utils.treeData;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzk
 * @date 2017/11/7
 */
public class TreeUtil {

    /**
     * 根据集合获取树状结构
     * @param treeList
     * @return
     */
    public static JSONArray buildTree(List<? extends ITree> treeList) {
        if(treeList == null || treeList.isEmpty()) {
            return null;
        }

        JSONArray jsonArray = new JSONArray();

        long minParentId = 0;
        if(treeList.get(0).findParentTreeId() != null) {
            minParentId = treeList.get(0).findParentTreeId();
        }
        //获取最顶层的父节点parentId
        for(int i = 1; i < treeList.size(); i++) {
            if(treeList.get(i).findParentTreeId() != null && treeList.get(i).findParentTreeId().longValue() < minParentId) {
                minParentId = treeList.get(i).findParentTreeId().longValue();
            }
        }
        //获取树
        for(ITree tree : treeList) {
            if(tree != null && tree.findParentTreeId() != null && tree.findParentTreeId().equals(Long.valueOf(minParentId))) {
                jsonArray.add(findChildren(tree, treeList));
            }
        }
        return jsonArray;
    }



    /**
     * 递归获取子树列表
     * @param tree
     * @param treeList
     * @return
     */
    public static ITree findChildren(ITree tree, List<? extends ITree> treeList) {
        if(tree == null || treeList == null || treeList.isEmpty()) {
            return tree;
        }

        Long id = tree.findTreeId();
        for(ITree tempTree : treeList) {
            if(tempTree == null || tempTree.findParentTreeId() == null) {
                continue;
            }
            if(tempTree.findParentTreeId().equals(id)) {
                if(tree.getChildList() == null) {
                    tree.setChildList(new ArrayList<>());
                }
                tree.getChildList().add(findChildren(tempTree, treeList));
            }
        }

        return tree;
    }
}
