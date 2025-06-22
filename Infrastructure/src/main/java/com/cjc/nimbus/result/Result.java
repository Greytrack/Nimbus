package com.cjc.nimbus.result;


import com.cjc.nimbus.constant.AirEdgeConstants;
import com.cjc.nimbus.exception.AppException;
import com.cjc.nimbus.utils.MessageUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用响应对象，用于封装接口返回结果
 *
 * @author xiaogang
 */
@Getter
@Setter
public class Result<T> {

    @Schema(name = "code", description = "响应码")
    int code;

    @Schema(name = "message", description = "响应消息")
    String message;

    @Schema(name = "data", description = "响应数据")
    T data;

    public static <T> Result<T> ok(T data) {
        return create(
                GlobalResultCode.SUCCESS.getCode(),
                GlobalResultCode.SUCCESS.getDesc(),
                data);
    }

    public static Result<String> successOk() {
        return create(
                GlobalResultCode.SUCCESS.getCode(),
                GlobalResultCode.SUCCESS.getDesc(),
                AirEdgeConstants.SUCCESS);
    }

    public static <T> Result<T> noDataOk() {
        return create(
                GlobalResultCode.SUCCESS.getCode(),
                GlobalResultCode.SUCCESS.getDesc());
    }

    public static <T> Result<T> create(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        message = MessageUtils.getI18nMessage(message);
        result.setMessage(message + "[" + code + "]");
        return result;
    }

    public static <T> Result<T> create(int code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        message = MessageUtils.getI18nMessage(message);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(ResCode codeEnum) {
        return create(
                codeEnum.getCode(),
                codeEnum.getDesc());
    }

    public static <T> Result<T> error(ResCode codeEnum, String message) {
        return create(codeEnum.getCode(), message);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return create(code, message);
    }

    public static <T> Result<T> error(Integer code, String message, T data) {
        return create(code, message, data);
    }

    public static <T> Result<T> error(AppException exception) {
        return create(exception.getCode(), exception.getMessage());
    }

    public static <T> Result<T> error(AppException exception, String message) {
        return create(exception.getCode(), message);
    }

    public boolean ok() {
        return code == GlobalResultCode.SUCCESS.getCode();
    }

}
