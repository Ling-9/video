package com.fgsqw.beans.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * 全局统一返回结果类
 */
@Data
@ApiModel(value = "全局统一返回结果")
public class Result<T> {

    @ApiModelProperty(value = "返回码")
    private Integer returnCode;

    @ApiModelProperty(value = "返回消息")
    private String resMessage;

    @ApiModelProperty(value = "返回数据")
    private T object;

    @ApiModelProperty(value = "条数")
    private Integer total = 0;

    private Result(Integer returnCode,String resMessage) {
        this.returnCode = returnCode;
        this.resMessage = resMessage;
    }

    private Result(String resMessage,T object) {
        this.resMessage = resMessage;
        this.object = object;
    }

    private Result(ResultCodeEnum resultCodeEnum) {
        this.returnCode = resultCodeEnum.getCode();
        this.resMessage = resultCodeEnum.getMessage();
    }

    private Result(T object, ResultCodeEnum resultCodeEnum) {
        this.returnCode = resultCodeEnum.getCode();
        this.resMessage = resultCodeEnum.getMessage();
        this.object = object;
    }

    private Result(T object,ResultCodeEnum resultCodeEnum,Integer total) {
        this.returnCode = resultCodeEnum.getCode();
        this.resMessage = resultCodeEnum.getMessage();
        this.object = object;
        this.total = total;
    }

    public static<T> Result<T> ok() {
        return new Result<>(ResultCodeEnum.SUCCESS);
    }

    public static<T> Result<T> ok(String resMessage) {
        return new Result<>(200,resMessage);
    }

    public static<T> Result<T> ok(ResultCodeEnum resultCodeEnum) {
        return new Result<>(resultCodeEnum);
    }

    public static<T> Result<T> ok(@Nullable T object) {
        return new Result<>(object,ResultCodeEnum.SUCCESS);
    }

    public static<T> Result<T> ok(String resMessage,@Nullable T object) {
        return new Result<>(resMessage,object);
    }

    public static<T> Result<T> ok(@Nullable T object,Integer total) {
        return new Result<>(object,ResultCodeEnum.SUCCESS,total);
    }

    public static<T> Result<T> fail() {
        return new Result<>(ResultCodeEnum.FAIL);
    }

    public static<T> Result<T> fail(ResultCodeEnum resultCodeEnum) {
        return new Result<>(resultCodeEnum);
    }

    public static<T> Result<T> fail(String resMessage) {
        return new Result<>(-9999,resMessage);
    }
}
