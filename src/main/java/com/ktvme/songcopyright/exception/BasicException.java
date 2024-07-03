package com.ktvme.songcopyright.exception;

import lombok.Getter;

/**
 * <p>Description: 自定义异常</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月03日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Getter
public class BasicException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private final int code;

    public BasicException(int code, String message){
        super(message);
        this.code = code;
    }

}