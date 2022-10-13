package com.fgsqw.beans.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Ling
 * @since 2022-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("mv_user")
@ApiModel(value="MvUser对象", description="")
public class MvUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "sid")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId("sid")
    private Long sid;

    @ApiModelProperty(value = "用户账号(登录名)")
    private String userName;

    @ApiModelProperty(value = "用户密码")
    private String passwd;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "简介")
    private String brief;

    @ApiModelProperty(value = "性别:1男，0女")
    private Boolean sex;

    @ApiModelProperty(value = "生日")
    private String birthday;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "头像")
    private String head;

    @ApiModelProperty(value = "主页背景")
    private String mainBack;

    @ApiModelProperty(value = "注册时间")
    private LocalDate regiTime;

    @ApiModelProperty(value = "是否是VIP 0 不是， 1 是")
    private Boolean isVip;

    @ApiModelProperty(value = "状态：0 正常 1 封禁")
    private Integer status;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "最后登录IP")
    private String lastIp;

    @ApiModelProperty(value = "最后登录时间")
    private String lastLogin;

    @ApiModelProperty(value = "最后密码更改时间")
    private String pwdUpdateTime;

    @ApiModelProperty(value = "查看主页权限: 0 所有人可看，1：仅好友可见 2：仅自己可见")
    private Integer perm;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除：0 未删除， 1 已删除")
    private Boolean delFlag;

    @ApiModelProperty(value = "临时字段")
    private String field;

    public MvUser(){

    }

    public MvUser(MvUser mvUser){
        this.sid = mvUser.getSid();
        this.userName = mvUser.getUserName();
        this.passwd = mvUser.getPasswd();
        this.email = mvUser.getEmail();
        this.phone = mvUser.getPhone();
        this.status = mvUser.getStatus();
    }
}
