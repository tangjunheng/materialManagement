package com.material.controller.user;

import com.material.dto.user.ShoppingCartDTO;
import com.material.entity.ShoppingCart;
import com.material.result.Result;
import com.material.service.user.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Tag(name = "C端-套餐浏览接口")
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 添加物资或套餐进购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @Operation(
            description = "添加物资或套餐进购物车",
            summary = "添加物资或套餐进购物车"
    )
    public Result<String> add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车：{}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    @Operation(
            description = "查看购物车",
            summary = "查看购物车"
    )
    public Result<List<ShoppingCart>> list(){
        return Result.success(shoppingCartService.showShoppingCart());
    }


    /**
     * 清空购物车商品
     * @return
     */
    @DeleteMapping("/clean")
    @Operation(
            description = "清空购物车商品",
            summary = "清空购物车商品"
    )
    public Result<String> clean(){
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }
}

