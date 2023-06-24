package com.example.reggie.common;

/**
 * 基于ThreadLocal封装的工具类，用于保存和获取当前登录用户的id
 *ThreadLocal类：线程管理类，可以用于线程内数据的共享
 * @author 李朋逊
 * @date 2023/06/18
 */
public class BaseContext {
    private static  ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
