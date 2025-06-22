package com.cjc.nimbus.exception;

import com.cjc.nimbus.result.ResCode;
import lombok.Getter;

import java.io.Serial;

/**
 * 应用级别异常基类，所有应用级别的异常都应该继承此类
 * @author xiaogang
 */
public class AppException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private final int code;

    private final String message;

    @Getter
    private final Object[] args;


    public AppException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.args = null;
    }

    public AppException(ResCode resCode) {
        super(resCode.getDesc());
        this.code = resCode.getCode();
        this.message = resCode.getDesc();
        this.args = null;
    }

    public AppException(ResCode resCode, Object... args) {
        super(resCode.getDesc());
        this.code = resCode.getCode();
        this.message = resCode.getDesc();
        this.args = args;
    }


    public AppException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.args = null;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
