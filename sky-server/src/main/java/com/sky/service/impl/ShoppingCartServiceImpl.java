package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO 购物车信息
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 通过 ThreadLocal 获取用户 id
        Long userId = BaseContext.getCurrentId();

        // 构造 shoppingCart
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .dishId(shoppingCartDTO.getDishId())
                .dishFlavor(shoppingCartDTO.getDishFlavor())
                .setmealId(shoppingCartDTO.getSetmealId())
                .userId(userId)
                .build();

        // 根据菜品/套餐查询购物车信息
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);

        if (shoppingCarts != null && shoppingCarts.size() > 0) {
            // 如果商品在购物车，更新商品数量
            ShoppingCart cart = shoppingCarts.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        } else {
            // 如果商品不在购物车，添加商品到购物车
            // 判断本次添加的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                // 菜品
                // 查询该菜品的信息，m,名称、图片、价格等
                Dish dish = dishMapper.searchDishById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // 套餐
                // 查询该套餐的信息
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            // 插入数据
            shoppingCartMapper.insert(shoppingCart);

        }




    }
}
