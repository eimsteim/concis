package com.joycoho.concis.sys.modular.menu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Classname Relation
 * @Description 角色和菜单关联表
 * @Version 1.0.0
 * @Date 2022/10/20 15:03
 * @Created by Leo
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sys_relation")
public class Relation extends Model<Relation> {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 菜单id
     */
    private Integer menuId;
    /**
     * 角色id
     */
    private Integer roleId;
}
