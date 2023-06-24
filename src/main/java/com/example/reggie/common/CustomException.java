package com.example.reggie.common;

/**
 * 自定义异常
 *
 * @author 李朋逊
 * @date 2023/06/19
 */
public class CustomException  extends  RuntimeException{
    public CustomException(String exception) {
        super(exception);
    }
}
