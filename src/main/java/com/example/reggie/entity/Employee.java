package com.example.reggie.entity;

import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体类
 *
 * @author 李朋逊
 * @date 2023/06/17
 */


@Data
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
     private String username;
     private String name;
     private String password;
     private Integer sex;
     private String phone;
     // -驼峰命名法，对应与数据库表中的id_number
    //身份证号码
    private String idNumber;
     private Integer status;
     /*
     @TableField()注解：INSERT插入时自动填充字段值 INSERT_UPDATE插入和更新时自动填充字段值
      */
     @TableField(fill = FieldFill.INSERT)
     private LocalDateTime createTime;
     @TableField(fill = FieldFill.INSERT_UPDATE)
     private LocalDateTime updateTime;

     @TableField(fill = FieldFill.INSERT)
    private Long createUser;
     @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
