package com.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie.dto.SetmealDto;
import com.example.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * Description 新增套餐同时新增套餐与菜品的关联数据
     *
     * @param setmealDto setmeal dto
     * @author 李朋逊
     * @date 2023/07/11
     */
    public void saveWithDish(SetmealDto setmealDto);


    /**
     * Description 删除套餐同时删除套餐与菜品的关联数据
     *
     * @param ids id
     * @author 李朋逊
     * @date 2023/07/11
     */
    public void removeWithDish(List<Long> ids);
}
