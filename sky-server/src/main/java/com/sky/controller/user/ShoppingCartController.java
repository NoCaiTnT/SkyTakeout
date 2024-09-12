package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "C端购物车接口")
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @ApiOperation(value = "添加购物车")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车, 商品信息为: {}", shoppingCartDTO);

        shoppingCartService.addShoppingCart(shoppingCartDTO);

        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "查看购物车")
    public Result<List<ShoppingCart>> list() {

        List<ShoppingCart> shoppingCarts = shoppingCartService.showShoppingCart();

        return Result.success(shoppingCarts);
    }
}
