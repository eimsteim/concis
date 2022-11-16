package com.joycoho.concis.sys.modular.role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname Role
 * @Description 角色表
 * @Version 1.0.0
 * @Date 2022/10/20 15:07
 * @Created by Leo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sys_role")
public class Role extends Model<Role> {
    /**
     * ID自增机制
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 序号
     */
    private Integer sn;
    /**
     * 父角色id
     */
    private Integer pid;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 部门名称
     */
    private Integer deptId;
    /**
     * 角色编码
     */
    private String code;
    /**
     * 保留字段(暂时没用）
     */
    private Integer version;
    /**
     * 备注
     */
    private String remark;
    /**
     * 层级
     */
    private Integer levels;
}
