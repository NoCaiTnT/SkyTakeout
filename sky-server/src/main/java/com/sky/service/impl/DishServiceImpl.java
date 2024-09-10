package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品和对应口味
     * @param dishDTO
     */
    @Transactional
    public void addDishAndFlavor(DishDTO dishDTO) {
        // 转换为实体类
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 向菜品表插入 1 条数据
        // 这里通过在 DishMapper.xml 中的设置，可以获得自动生成的 id，并将其赋值给 dish 实体
        dishMapper.addDish(dish);

        // 获取插入的菜品自动生成的 id
        Long dishId = dish.getId();

        // 获取口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();

        // 向口味表插入 n 条数据
        if (flavors != null && flavors.size() > 0) {
            // 使用 lambda 表达式，循环赋值 dishId
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });

            // 批量插入
            dishFlavorMapper.addBatchFlavor(flavors);
        }


    }

}
