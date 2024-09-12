package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequestMapping("/admin/dish")
@RestController
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping("")
    @ApiOperation(value = "添加菜品")
    public Result addDish(@RequestBody DishDTO dishDTO) {
        log.info("添加菜品：{}", dishDTO);

        dishService.addDishAndFlavor(dishDTO);

        // 构造 key，该菜品所属的类别 id
        String key = "dish_" + dishDTO.getCategoryId();

        // 清理 Redis 缓存
        cleanDishRedisCache("key");

        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "菜品分页查询")
    public Result<PageResult> searchDish(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.searchDish(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping("")
    @ApiOperation(value = "删除菜品")
    public Result deleteDish(@RequestParam List<Long> ids) {      // 也可以使用 String ids 接收字符串，然后再用逗号分隔符分割成数组
        log.info("删除菜品：{}", ids);
        dishService.deleteDish(ids);

        // 为了避免查询操作，将所有 key 全删除
        // 查询出 Redis 中所有的菜品类别 key，dish_开头的
        cleanDishRedisCache("dish_*");

        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询菜品")
    public Result<DishVO> searchDishAndFlavorById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}", id);
        DishVO dishVO = dishService.searchDishAndFlavorById(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping("")
    @ApiOperation(value = "修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.updateDishAndFlavor(dishDTO);

        // 如果修改分类，则需要清理两个 Redis 缓存
        // 如果修改其他数据，则只需要将该菜品所属的类别的缓存清理
        // 为了简单，直接全删
        // 查询出 Redis 中所有的菜品类别 key，dish_开头的
        cleanDishRedisCache("dish_*");

        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        dishService.startOrStop(status, id);

        // 为了方便，清理 Redis 缓存
        // 查询出 Redis 中所有的菜品类别 key，dish_开头的
        cleanDishRedisCache("dish_*");

        return Result.success();
    }

    /**
     * 清理 菜品所有的 Redis 缓存
     * @param pattern
     */
    public void cleanDishRedisCache(String pattern) {
        // 查询出 Redis 中所有的菜品类别 key，dish_开头的
        Set keys = redisTemplate.keys(pattern);
        // 删除这些 keys
        redisTemplate.delete(keys);
    }

}
