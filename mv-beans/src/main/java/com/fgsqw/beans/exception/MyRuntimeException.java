package com.fgsqw.beans.exception;

import com.fgsqw.beans.result.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyRuntimeException extends RuntimeException{

    private String resMessage;

    public MyRuntimeException(ResultCodeEnum resultCodeEnum){
        this.resMessage = resultCodeEnum.getMessage();
    }
}
