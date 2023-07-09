package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.entity.Category;
import com.example.reggie.mapper.CategoryMapper;
import com.example.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
   @Autowired
   private CategoryService categoryService;

    /**
     * Description 新增分类
     *
     * @param category 类别
     * @return {@link R }<{@link String }>
     * @author 李朋逊
     * @date 2023/06/19
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("Category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);
        //分页查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }


    /**
     * Description 根据id删除分类
     *注意：分类可能会关联菜品或其他套餐，所以不能直接调用remove方法直接删除
     * @param id id
     * @return {@link R }<{@link String }>
     * @author 李朋逊
     * @date 2023/06/19
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("id为{}",ids);
        categoryService.removeCategoryById(ids);
        return R.success("分类信息删除成功");
    }


    @PutMapping
    public R<String>  update(@RequestBody Category category){
        log.info("修改分类信息，{}",category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }


    /**
     * Description 根据条件查询分类数据
     *
     * @param category 类别
     * @return {@link R }<{@link List }<{@link Category }>>
     * @author 李朋逊
     * @date 2023/07/08
     */
    @GetMapping("/list")
    public R<List<Category>>  list(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
            return R.success(list);
    }
}
