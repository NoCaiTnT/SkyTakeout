package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
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

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);

    /**
     * 起售 或 停售菜品
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}
