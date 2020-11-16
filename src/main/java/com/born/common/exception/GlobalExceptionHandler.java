package com.born.common.exception;

import com.born.common.Result;
import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-10-02 22:27:52
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //@ResponseStatus(HttpStatus.UNAUTHORIZED)//401 无权限
    //@ExceptionHandler(value = ShiroException.class)
    //public Result handler(ShiroException e){
    //    log.error("运行时异常:---{}", e);
    //    return Result.fail("401",e.getMessage(),null);
    //}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e){
        log.error("实体校验异常:---{}", e);
        //对于校验出来的多个异常（密码为null、邮箱不匹配等），每次仅抛出第一个异常
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.fail(objectError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e){
        log.error("Assert异常:---{}", e);
        return Result.fail(e.getMessage());
    }


    //将HTTP 状态码返回给前端
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = GlobalException.class)
    public Result handler(GlobalException e){
        log.error("全局异常---{}", e.getMessage());
        return Result.fail(e.getStatus());
    }


    //将HTTP 状态码返回给前端
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e){
        log.error("运行时异常---{}", e.getMessage());
        return Result.fail(e.getMessage());
    }

}
