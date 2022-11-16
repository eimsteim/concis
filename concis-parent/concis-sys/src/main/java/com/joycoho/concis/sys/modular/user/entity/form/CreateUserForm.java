package com.joycoho.concis.sys.modular.user.entity.form;

import cn.hutool.core.date.DateUtil;
import com.joycoho.concis.sys.modular.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Classname CreateUserForm
 * @Description 新建用户表单
 * @Version 1.0.0
 * @Date 2022/11/1 01:14
 * @Created by Leo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserForm {

    private String account;

    private String name;

    private String password;

    private String phone;

    private Integer deptId;

    private Integer sex;

    private String birthday;

    private String email;

    public User toUser() {
        User user = User.builder().account(this.account).name(this.name).password(this.password)
                .phone(this.phone).deptId(this.deptId).sex(this.sex).email(this.email).build();
        //解析生日
        if (StringUtils.isNotBlank(birthday)) {
            user.setBirthday(DateUtil.parse(birthday, "yyyy-MM-dd"));
        }
        //密码，默认为123456
        if (StringUtils.isBlank(this.password)) {
            user.setPassword("123456");
        }
        return user;
    }
}
