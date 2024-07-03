package com.ktvme.songcopyright.exception;

/**
 * <p>Description: 业务异常</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月03日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
public class BusinessException extends BasicException{

    private static final long serialVersionUID = 1L;

    public BusinessException(int code, String message) {
        super(code, message);
    }
}