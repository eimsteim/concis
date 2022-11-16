package com.joycoho.concis.sys.modular.user.entity.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SetAuthorityForm
 * @Description 用于批量授权的请求表单
 * @Version 1.0.0
 * @Date 2022/11/7 00:10
 * @Created by Leo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetAuthorityForm {

    private Integer userId;

    private List<Integer> roleIds;
}
