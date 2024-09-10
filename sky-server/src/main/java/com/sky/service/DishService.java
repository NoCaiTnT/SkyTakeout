package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

public interface DishService {


    /**
     * 新增菜品和对应的口味
     */
    void addDishAndFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     */
    PageResult searchDish(DishPageQueryDTO dishPageQueryDTO);
}
