package com.material.handler;


import com.material.constant.MessageConstant;
import com.material.exception.BaseException;
import com.material.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;


import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * @ExceptionHandler相当于controller的@RequestMapping
     * 如果抛出的的是BaseException，则调用该方法
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生业务异常:", requestURI, ex);
        return Result.error(ex.getMessage());
    }

    /**
     * 处理SQL异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(DuplicateKeyException ex){
        // Duplicate entry 'zhangsan' for key 'employee.idx_username'
        // 针对插入已存在的字段的异常处理
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[9];
            String msg = username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }else{
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }


    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常:", requestURI, ex);
        return Result.error(ex.getMessage());
    }





}
