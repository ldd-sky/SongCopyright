package com.ktvme.songcopyright.exception;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ktvme.songcopyright.model.Result;
import com.ktvme.songcopyright.model.ResultEnum;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

/**
 * <p>Description: 参数校验全局异常处理器</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月03日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    private static final String REGEX = "\\S+.\\S+:\\s+";

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ConstraintViolationException.class})
    public Result<Object>handleConstraintViolationException(ConstraintViolationException ex){
        String message = ex.getMessage();
        if (message != null){
            message = message.replaceFirst(REGEX, StringUtils.EMPTY);
        }
        return Result.failed(message);
    }

    /**
     * {@code @RequestBody} 参数校验不通过时抛出的异常处理
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        return Result.failed(ResultEnum.VALIDATE_FAILED.getCode(),
                bindingResult.getFieldErrors().get(0).getDefaultMessage());
    }


    /**
     * Get请求，@Valid Bean 对象接收，参数验证失败异常处理
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({BindException.class})
    public Result<Object> handleBindException(BindException ex) {
        final FieldError fieldError = ex.getBindingResult().getFieldError();
        if (Objects.nonNull(fieldError)) {
            if (fieldError.isBindingFailure()) {
                return Result.failed(ResultEnum.VALIDATE_FAILED.getCode(), "参数类型绑定失败：" + fieldError.getField());
            } else {
                return Result.failed(fieldError.getDefaultMessage());
            }
        }
        return Result.failed(ResultEnum.VALIDATE_FAILED);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("捕获异常", ex);
        return Result.failed(ResultEnum.VALIDATE_FAILED.getCode(), "请求参数转换异常");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("捕获异常", ex);
        return Result.failed(ResultEnum.VALIDATE_FAILED.getCode(), "参数类型绑定失败：" + ex.getName());
    }
}