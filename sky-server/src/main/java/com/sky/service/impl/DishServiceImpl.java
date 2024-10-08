package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

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

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult searchDish(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.searchDish(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 删除菜品
     * @param ids
     */
    @Transactional
    public void deleteDish(List<Long> ids) {

        // 判断菜品是否能够删除 是否在起售中
        for (Long id : ids) {
            Dish dish = dishMapper.searchDishById(id);
            if (dish.getStatus() == 1) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        // 判断菜品是否能够删除 菜品是否被套餐关联
        List<Long> setmealIds = setmealDishMapper.searchSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        /**
         * 单次删除需要执行多条 sql 语句，需要改为批量删除
         */
        /*
        // 删除菜品表中的菜品数据 以及 该菜品对应的口味数据
        for (Long id : ids) {
            dishMapper.deleteDish(id);
            dishFlavorMapper.deleteDishFlavorByDishId(id);
        }*/

        // 批量删除 菜品数据 以及 该菜品对应的口味数据
        dishMapper.deleteMultiDish(ids);

        dishFlavorMapper.deleteMultiDishFlavorByDishId(ids);

    }

    /**
     * 根据 id 查询菜品和对应口味
     * @param id
     * @return
     */
    public DishVO searchDishAndFlavorById(Long id) {
        // 根据 id 查询菜品信息
        Dish dish = dishMapper.searchDishById(id);

        // 根据菜品 id 查询口味信息
        List<DishFlavor> dishFlavors = dishFlavorMapper.searchDishFlavorByDishId(id);

        // 封装成 DishVO 对象返回
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * 更新菜品和对应口味
     * @param dishDTO
     */
    public void updateDishAndFlavor(DishDTO dishDTO) {
        // 修改菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.updateDish(dish);

        // 删除口味信息
        dishFlavorMapper.deleteDishFlavorByDishId(dishDTO.getId());

        // 重新插入口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();

        // 向口味表插入 n 条数据
        if (flavors != null && flavors.size() > 0) {
            // 使用 lambda 表达式，循环赋值 dishId
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });

            // 批量插入
            dishFlavorMapper.addBatchFlavor(flavors);
        }
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.searchDishFlavorByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    /**
     * 起售 或 停售菜品
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.updateDish(dish);
    }
}
