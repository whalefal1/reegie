package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.common.CustomException;
import com.example.reggie.entity.Category;
import com.example.reggie.entity.Dish;
import com.example.reggie.entity.Setmeal;
import com.example.reggie.mapper.CategoryMapper;
import com.example.reggie.service.CategoryService;
import com.example.reggie.service.DishService;
import com.example.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImp extends ServiceImpl<CategoryMapper,Category> implements  CategoryService{
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    @Override
    public void removeCategoryById(Long id) {
        //查询当前分类是否关联了菜品，如果已关联，抛出事务异常
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishQueryWrapper);
        if(count1>0){
            throw new CustomException("当前分类下关联菜品，不能删除");
        }
        //查询当前分类是否关联了套餐，如果已关联，抛出事务异常
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealQueryWrapper);
        if(count2>0){
            throw new CustomException("当前分类下关联套餐，不能删除");
        }


        //正常删除分类

        super.removeById(id);
    }
}