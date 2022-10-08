package com.fgsqw.beans.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.util.Collection;


@Setter
@Getter
@ToString
public class Result<T> {

    private T object;
    private Collection<T> beans;
    private Integer total = 0;
    private String returnMessage;
    private String returnCode;

    private Result(String returnCode, String returnMessage) {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
    }

    private Result(T object, String returnCode, String returnMessage) {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
        this.object = object;
    }

    private Result(Integer total ,T object, String returnCode, String returnMessage) {
        this.total=total;
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
        this.object = object;
    }

    public static final <T> Result<T> ok() {
        return new Result<>("0", "ok");
    }

    public static final <T> Result<T> ok(String returnMessage) {
        return new Result<>("0", returnMessage);
    }

    public static final <T> Result<T> ok(String returnCode, String returnMessage) {
        return new Result<>(returnCode, returnMessage);
    }

    public static final <T> Result<T> ok(@Nullable T object) {
        return new Result<>(object, "0", "ok");
    }

    public static final <T> Result<T> fail() {
        return new Result<>("-1" ,"error");
    }

    public static final <T> Result<T> fail(String returnMessage) {
        return new Result<>("-1", returnMessage);
    }

    public static final <T> Result<T> fail(String returnCode, String returnMessage) {
        return new Result<>(returnCode, returnMessage);
    }

}
