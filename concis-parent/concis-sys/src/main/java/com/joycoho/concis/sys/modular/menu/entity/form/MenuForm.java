package com.joycoho.concis.sys.modular.menu.entity.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname MenuForm
 * @Description 新增或修改Menu对象时的表单
 * @Version 1.0.0
 * @Date 2022/11/11 01:29
 * @Created by Leo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuForm {

    private Integer id;

    private Integer parentId;

    private String code;

    private String icon;

    private Integer ismenu;

    private String name;

    private Integer num;

    private String path;

    private String appCode;
}
