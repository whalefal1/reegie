package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.entity.SetmealDish;
import com.example.reggie.mapper.SetMealDishMapper;
import com.example.reggie.service.SetmealDishService;
import org.springframework.stereotype.Service;


@Service
public class SetmealDishImp extends ServiceImpl<SetMealDishMapper, SetmealDish> implements SetmealDishService {
}
