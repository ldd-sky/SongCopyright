package com.ktvme.songcopyright.exception;

import com.ktvme.songcopyright.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * <p>Description: 全局异常处理器</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月03日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常
     * @param e the e
     * @return R
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Result<Object> exception(Exception e){
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return Result.failed(e.getStackTrace(), e.getMessage());
    }
}