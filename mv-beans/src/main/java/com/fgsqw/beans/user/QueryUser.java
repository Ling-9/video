package com.fgsqw.beans.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="条件查询对象", description="")
public class QueryUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户账号(登录名)")
    private String userName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "状态：0 正常 1 封禁")
    private String status;

    @ApiModelProperty(value = "页数")
    private Integer page;

    @ApiModelProperty(value = "显示条数")
    private Integer limit;
}
