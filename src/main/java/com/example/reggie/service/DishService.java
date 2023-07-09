package com.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie.dto.DishDto;
import com.example.reggie.entity.Dish;
import org.springframework.beans.factory.annotation.Autowired;

public interface DishService extends IService<Dish> {

    //新增菜品，同时插入对应的口味数据，需要同时操作两张表
    public void  saveWithFlavor(DishDto dishDto);
}
