package com.fgsqw.beans.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="注册对象", description="")
public class RegisterUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户账号(登录名)")
    private String userName;

    @ApiModelProperty(value = "用户密码")
    private String passwd;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "邮箱验证码")
    private String verifyCode;
}
