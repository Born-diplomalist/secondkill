package com.born.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-10-08 09:11:34
 */
@Data
public class LoginVo{

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "密码不能为空")
    private String userPassword;

}
