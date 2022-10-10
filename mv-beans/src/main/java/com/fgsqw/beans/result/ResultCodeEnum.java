package com.fgsqw.beans.result;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"请求成功！"),
    FAIL(-9999,"请求失败！"),
    ISNULL(-9999,"参数缺失！"),

    VERIFY_CODE_SUCCESS(10000,"验证码发送成功!"),
    VERIFY_CODE_INVALID(10001,"验证码已经失效!"),
    VERIFY_CODE_FAIL(10002,"验证码错误!")

    ;

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
