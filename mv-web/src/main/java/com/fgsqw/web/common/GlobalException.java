package com.fgsqw.web.common;

import com.fgsqw.beans.exception.MyRuntimeException;
import com.fgsqw.beans.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SQLException.class)
    public Result mySqlException(SQLException e){
        if (e instanceof SQLIntegrityConstraintViolationException){
            return Result.fail("该数据有关联,操作失败!");
        }
        return Result.fail("数据库操作异常!请查看日志!");
    }

    @ExceptionHandler(MyRuntimeException.class)
    public Result RuntimeException(MyRuntimeException e){
        e.printStackTrace();
        log.error("出现异常!" + "{" + e.getResMessage() + "}");
        return Result.fail("出现异常!" + "{" + e.getResMessage() + "}");
    }
}
