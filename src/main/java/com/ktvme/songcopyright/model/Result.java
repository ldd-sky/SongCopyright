package com.ktvme.songcopyright.model;

import lombok.Data;

/**
 * <p>Description: 全局统一返回结果类</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月03日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;
    private boolean success;

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result() {
    }

    public static <T> Result<T> success(){
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), message, data);
    }

    public static <T> Result<T> failed() {
        return new Result<>(ResultEnum.COMMON_FAILED.getCode(), ResultEnum.COMMON_FAILED.getMessage(), null);
    }

    public static <T> Result<T> failed(String message) {
        return new Result<>(ResultEnum.FILE.getCode(), message, null);
    }

    public static <T> Result<T> failed(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> failed(T data, String message) {
        return new Result<>(ResultEnum.COMMON_FAILED.getCode(), message, data);
    }

    public static <T> Result<T> failed(ResultEnum resultEnum) {
        return new Result<>(resultEnum.getCode(), resultEnum.getMessage(), null);
    }

    public static <T> Result<T> failed(IResult errorResult) {
        return new Result<>(errorResult.getCode(), errorResult.getMessage(), null);
    }

    public static <T> Result<T> instance(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(message);
        result.setData(data);
        return result;
    }

    public boolean isSuccess() {
        return code.equals(ResultEnum.SUCCESS.getCode());
    }
}