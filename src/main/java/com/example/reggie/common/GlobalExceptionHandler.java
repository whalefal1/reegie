package com.example.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 *通过注解识别要进行异常处理的对象
 * @author 李朋逊
 * @date 2023/06/18
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody //返回json数据格式
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Description 异常处理方法
     *
     * @return {@link R }<{@link String }>
     * @author 李朋逊
     * @date 2023/06/18
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());

        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
             String msg  = split[2] + "已存在";
             return  R.error(msg);
        }

        return R.error("未知错误");
    }

}
