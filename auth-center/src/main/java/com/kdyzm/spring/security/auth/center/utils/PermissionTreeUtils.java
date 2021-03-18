package com.kdyzm.spring.security.auth.center.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.TreeNode;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 将权限处理成树形结构
 *
 * @author tuess
 * @version 1.0
 **/
@Slf4j
public class PermissionTreeUtils {

    public static List<?> putParent(List<TreeNode<String>> nodeList, String pid) {
        List<Object> list = new ArrayList<>();
        for (TreeNode<String> stringTreeNode : nodeList) {
            Map<String, Object> mapParent = new LinkedHashMap<>();
            if (null != pid && stringTreeNode.getParentId().equals(pid)) {
                mapParent.put("id", stringTreeNode.getId());
                mapParent.put("name", stringTreeNode.getName());
                mapParent.put("pid", stringTreeNode.getParentId());
                mapParent.put("childList", putParent(nodeList, stringTreeNode.getId()));
                list.add(mapParent);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        // 构建node列表
        List<TreeNode<String>> nodeList = CollUtil.newArrayList();

        nodeList.add(new TreeNode<>("1", "0", "系统管理", 5));
        nodeList.add(new TreeNode<>("11", "1", "用户管理", 222222));
        nodeList.add(new TreeNode<>("111", "11", "用户添加", 0));
        nodeList.add(new TreeNode<>("2", "0", "店铺管理", 1));
        nodeList.add(new TreeNode<>("21", "2", "商品管理", 44));
        nodeList.add(new TreeNode<>("221", "2", "商品管理2", 2));

        // 0为顶级权限的父权限id
        List<?> objects = putParent(nodeList, "0");
        System.out.println(JSONObject.toJSONString(objects));
    }
}
