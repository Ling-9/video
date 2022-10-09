package com.fgsqw.beans.result;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"请求成功！"),
    FAIL(-9999,"请求失败！"),

    ;

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
