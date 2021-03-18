package com.kdyzm.spring.security.auth.center.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.TreeNode;
import com.alibaba.fastjson.JSONObject;
import com.kdyzm.spring.security.auth.center.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author kdyzm
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DBTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        log.info(("----- selectAll method test ------"));
        userMapper.selectList(null);
    }

    @Test
    public void test1() {
        // 构建node列表
        List<TreeNode<String>> nodeList = CollUtil.newArrayList();

        nodeList.add(new TreeNode<>("1", "0", "系统管理", 5));
        nodeList.add(new TreeNode<>("11", "1", "用户管理", 222222));
        nodeList.add(new TreeNode<>("111", "11", "用户添加", 0));
        nodeList.add(new TreeNode<>("2", "0", "店铺管理", 1));
        nodeList.add(new TreeNode<>("21", "2", "商品管理", 44));
        nodeList.add(new TreeNode<>("221", "2", "商品管理2", 2));

        System.out.println(Arrays.toString(nodeList.toArray()));

        List<?> objects = putParent(nodeList, "0");
        System.out.println(JSONObject.toJSONString(objects));


    }

    public List<?> putParent(List<TreeNode<String>> nodeList, String pid) {
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


    @Test
    public void Test2() {
        String password = "$2a$10$Yt/rrImIYXwYqpAQNI/vIe/kwcJmx19MjZ5kipF0xsHrBcUGHZNqi";
    }
}
