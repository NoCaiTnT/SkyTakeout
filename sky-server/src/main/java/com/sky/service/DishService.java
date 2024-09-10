package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {


    /**
     * 新增菜品和对应的口味
     */
    void addDishAndFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     */
    PageResult searchDish(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除菜品
     */
    void deleteDish(List<Long> ids);

    /**
     * 根据id查询菜品和对应的口味
     */
    DishVO searchDishAndFlavorById(Long id);

    /**
     * 更新菜品和对应的口味
     */
    void updateDishAndFlavor(DishDTO dishDTO);
}
