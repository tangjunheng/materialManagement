package com.material.service.user.impl;

import com.material.context.BaseContext;
import com.material.dto.user.ShoppingCartDTO;
import com.material.entity.Material;
import com.material.entity.Setmeal;
import com.material.entity.ShoppingCart;
import com.material.mapper.admin.MaterialMapper;
import com.material.mapper.admin.SetmealMapper;
import com.material.mapper.user.ShoppingCartMapper;
import com.material.service.user.ShoppingCartService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class shoppingCartServiceImpl implements ShoppingCartService {

    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private SetmealMapper setmealMapper;


    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //只能查询自己的购物车数据
        shoppingCart.setUserId(BaseContext.getCurrentId());

        //判断当前物资或者套餐是否在购物车中
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        if (shoppingCartList != null && shoppingCartList.size() == 1) {
            //如果已经存在，就更新数量，将请求的数量加上去
            // TODO 对请求数量进行限定，超过最大值就用最大值
            // 1.获取当前物资/套餐的关联数据
            // 2.如果是物资的话，查看购物车中关联的套餐的数量，关联的套餐数量*关联的copies+购物车中原有的物资数量+请求的物资的数量<=物资的总量
            // 3.如果是套餐的话，查看购物车中关联的物资的数量，关联的物资数量+原有套餐数量*关联的copies+购物车中其它与该物资相关联的套餐*copies+请求的套餐数量*copies<=物资的总量
            shoppingCart = shoppingCartList.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + shoppingCartDTO.getNumber());
            shoppingCartMapper.updateNumberById(shoppingCart);
        } else {
            //如果不存在，插入数据，数量就是请求的数量

            //判断当前添加到购物车的是菜品还是套餐
            Long materialId = shoppingCartDTO.getMaterialId();

            if (materialId != null) {
                //添加到购物车的是物资
                // 将物资id封装成数组以方便请求mapper
                List<Long> materialIds = new ArrayList();
                materialIds.add(materialId);
                // 获取物资并放进购物车里
                List<Material> materials = materialMapper.getByIds(materialIds);
                shoppingCart.setName(materials.get(0).getName());
                shoppingCart.setImage(materials.get(0).getImage());
            } else {
                //添加到购物车的是套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
            }
            shoppingCart.setNumber(shoppingCartDTO.getNumber());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {
        return shoppingCartMapper.list(ShoppingCart.
                                        builder().
                                        userId(BaseContext.getCurrentId()).
                                        build());
    }

    /**
     * 清空购物车商品
     */
    @Override
    public void cleanShoppingCart() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }

}
