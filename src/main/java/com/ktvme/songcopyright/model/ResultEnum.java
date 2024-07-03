package com.ktvme.songcopyright.model;

import lombok.Getter;

/**
 * <p>Description: 响应状态码</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月03日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Getter
public enum ResultEnum {

    /**
     * code, msg
     */
    SUCCESS(200, "接口调用成功"),
    FILE(-1, "接口调用失败"),
    VALIDATE_FAILED(400, "参数校验失败"),
    NO_LOGIN(401, "匿名用户无权限访问！"),
    TOKEN_ERROR(4011, "互踢、Token失效、注销"),
    NO_TOKEN(4012, "匿名用户无权限访问！"),
    NO_AUTH(403, "无权限访问,请联系管理员！"),
    COMMON_FAILED(500, "接口调用失败");

    private final Integer code;

    private final String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}