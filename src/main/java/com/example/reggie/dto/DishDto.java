package com.example.reggie.dto;

import com.example.reggie.entity.Dish;
import com.example.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜品类数据传输对象
 *
 * @author 李朋逊
 * @date 2023/07/08
 */
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();
    private String categoryName;

    private Integer copies;
}
