package com.ysma.ppt.ppt.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定制异常
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomException extends RuntimeException {

    private int errorCode;

    public CustomException(int errorCode, String errorMsg){
        super(errorMsg);
        this.errorCode = errorCode;
    }
}
