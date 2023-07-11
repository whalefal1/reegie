package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.dto.DishDto;
import com.example.reggie.entity.Category;
import com.example.reggie.entity.Dish;
import com.example.reggie.service.CategoryService;
import com.example.reggie.service.DishFlavorService;
import com.example.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
        @Autowired
        private DishService dishService;
        @Autowired
        private DishFlavorService dishFlavorService;

        @Autowired
        private CategoryService categoryService;

        @PostMapping
        public R<String> save(@RequestBody DishDto dishDto){
                log.info(dishDto.toString());
                dishService.saveWithFlavor(dishDto);
                return R.success("新增菜品成功");
        }

        @GetMapping("/page")
        public  R<Page> page(int page,int pageSize,String name){
               Page<Dish> pageInfo = new Page<>(page,pageSize) ;
               Page<DishDto> dishDtoPage = new Page<>();
                LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(name!=null,Dish::getName,name);
                queryWrapper.orderByDesc(Dish::getUpdateTime);
                dishService.page(pageInfo,queryWrapper);
                //注意此时查询到的数据中菜品的分类是Long型的id，需要进行转化，采用对象拷贝的方法
                BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
                List<Dish> records = pageInfo.getRecords();
                List<DishDto> list =  records.stream().map((item)->{
                    DishDto dishDto = new DishDto();

                        Long id = item.getCategoryId();
                        //根据id查询分类对象
                        Category byId = categoryService.getById(id);
                        if(byId != null){
                            String categoryName = byId.getName();
                            dishDto.setCategoryName(categoryName);
                        }
                        BeanUtils.copyProperties(item,dishDto);
                        return dishDto;
                }).collect(Collectors.toList());
                dishDtoPage.setRecords(list);
                return R.success(dishDtoPage);
        }

    /**
     * Description 根据条件查询对应菜品数据
     *
     * @param dish 菜
     * @return {@link R }<{@link List }<{@link Dish }>>
     * @author 李朋逊
     * @date 2023/07/10
     */
    @GetMapping("/list")
        public R<List<Dish>> listDish(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
        }
}
