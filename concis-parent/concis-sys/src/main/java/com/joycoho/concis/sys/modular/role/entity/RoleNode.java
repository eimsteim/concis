/**
 * Copyright 2022-2025 Leo (https://github.com/eimsteim/concis)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joycoho.concis.sys.modular.role.entity;

import java.util.*;

/**
 * @author Leo
 * @Description 菜单的节点
 * @date 2016年12月6日 上午11:34:17
 */
public class RoleNode implements Comparable {

    /**
     * 节点id
     */
    private Long id;

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 节点名称
     */
    private String name;
    /**
     * 编码
     */
    private String code;

    /**
     * 按钮级别
     */
    private Integer levels;

    /**
     * 按钮的排序
     */
    private Integer sn;

    /**
     * 子节点的集合
     */
    private List<RoleNode> children;

    public RoleNode() {
        super();
    }

    public RoleNode(Long id, Long parentId) {
        super();
        this.id = id;
        this.parentId = parentId;
    }

    public Integer getLevels() {
        return levels;
    }

    public void setLevels(Integer levels) {
        this.levels = levels;
    }

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public static RoleNode createRoot() {
        return new RoleNode(0L, -1L);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<RoleNode> getChildren() {
        return children;
    }

    public void setChildren(List<RoleNode> children) {
        this.children = children;
    }


    @Override
    public String toString() {
        return "MenuNode{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", levels=" + levels +
                ", children=" + children +
                '}';
    }

    /**
     * 重写排序比较接口，首先根据等级排序，然后更具排序字段排序
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        RoleNode roleNode = (RoleNode) o;
        Integer levels = roleNode.getLevels();
        Integer sn     = roleNode.getSn();
        if (sn == null) {
            sn = 0;
        }
        if (levels == null) {
            levels = 0;
        }
        if (this.levels.compareTo(levels) == 0) {
            return this.sn.compareTo(sn);
        } else {
            return this.levels.compareTo(levels);
        }

    }

    /**
     * 构建页面菜单列表
     */
    public static List<RoleNode> buildTitle(List<RoleNode> nodes) {
        if (nodes.size() <= 0) {
            return nodes;
        }
        //对菜单排序，返回列表按菜单等级，序号的排序方式排列
        Collections.sort(nodes);
        return mergeList(nodes, nodes.get(nodes.size() - 1).getLevels(), null);
    }

    /**
     * 递归合并数组为子数组，最后返回第一层
     *
     * @param menuList
     * @param listMap
     * @return
     */
    private static List<RoleNode> mergeList(List<RoleNode> menuList, int rank, Map<Long, List<RoleNode>> listMap) {
        //保存当次调用总共合并了多少元素
        int n;
        //保存当次调用总共合并出来的list
        Map<Long, List<RoleNode>> currentMap = new HashMap<>();
        //由于按等级从小到大排序，需要从后往前排序
        //判断该节点是否属于当前循环的等级,不等于则跳出循环
        for (n = menuList.size() - 1; n >= 0 && menuList.get(n).getLevels() == rank; n--) {
            //判断之前的调用是否有返回以该节点的id为key的map，有则设置为children列表。
            if (listMap != null && listMap.get(menuList.get(n).getId()) != null) {
                menuList.get(n).setChildren(listMap.get(menuList.get(n).getId()));
            }
            if (menuList.get(n).getParentId() != null && menuList.get(n).getParentId() != 0) {
                //判断当前节点所属的pid是否已经创建了以该pid为key的键值对，没有则创建新的链表
                currentMap.computeIfAbsent(menuList.get(n).getParentId(), k -> new LinkedList<>());
                //将该节点插入到对应的list的头部
                currentMap.get(menuList.get(n).getParentId()).add(0, menuList.get(n));
            }
        }
        if (n < 0) {
            return menuList;
        } else {
            return mergeList(menuList.subList(0, n + 1), menuList.get(n).getLevels(), currentMap);
        }
    }

    /**
     * 创建一个虚拟的顶级节点，便于前端组织树结构
     * @return
     */
    public static RoleNode createParent() {
        RoleNode node = new RoleNode();
        node.setId(0L);
        node.setName("顶级");
        node.setParentId(0L);
        node.setSn(1);
        node.setLevels(0);
        return node;
    }
}
