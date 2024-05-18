package com.material.service.user.impl;

import com.material.constant.MessageConstant;
import com.material.context.BaseContext;
import com.material.dto.user.ShoppingCartDTO;
import com.material.dto.user.ShoppingSetmealMaterialDTO;
import com.material.entity.Material;
import com.material.entity.Setmeal;
import com.material.entity.ShoppingCart;
import com.material.exception.ParametersException;
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
        // 禁止两种数据同时有或同时无，抛出参数错误异常
        if ((shoppingCartDTO.getMaterialId() !=null && shoppingCartDTO.getSetmealId() !=null) || (shoppingCartDTO.getMaterialId() ==null && shoppingCartDTO.getSetmealId() ==null)){
            throw new ParametersException(MessageConstant.PARAMETERS_ERROR);
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //只能查询自己的购物车数据
        shoppingCart.setUserId(BaseContext.getCurrentId());

        //判断当前物资或者套餐是否在购物车中
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        if (shoppingCartList != null && shoppingCartList.size() == 1) {
            //如果已经存在，就更新数量，将请求的数量加上去
            shoppingCart = shoppingCartList.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + shoppingCartDTO.getNumber());
            shoppingCartMapper.updateNumberById(shoppingCart);
            // TODO 对请求数量进行限定，超过能请求的最大值就用最大值（对于这个功能优化，感觉需要重新构建数据库才行）
            // TODO 预设方案（感觉请求太多了）：
            // TODO 1.获取当前请求添加的物资/套餐对应购物车的的数量
            // TODO 2.如果是物资的话，获取购物车中关联的所有套餐（有n个），每个关联的套餐的数量*关联的copies+购物车中原有的物资数量+请求的物资的数量<=物资的总量
            // TODO 3.如果是套餐的话，查看购物车中关联的所有物资（有n个），对于一种每个关联的物资，关联的物资数量+原有套餐数量*关联的copies+购物车中其它与这个物资关联的套餐*copies+请求的套餐数量*copies<=这个物资的总量

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


    /**
     * 删除购物车中一个物资或套餐
     * @param shoppingCartDTO
     */
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //设置查询条件，查询当前登录用户的购物车数据
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        if(list != null && list.size() > 0){
            shoppingCart = list.get(0);

            Integer number = shoppingCart.getNumber();
            if(number == 1){
                //当前物资或套餐在购物车中的份数为1，直接删除当前记录
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }else {
                //当前物资或套餐在购物车中的份数不为1，修改份数即可
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }
    }

    // TODO 根据用户id获取购物车某物资的所有数量（此函数用于前面的增加购物车的优化）
    private int getCurrentMaterialQuantity(ShoppingCartDTO shoppingCartDTO) {
        if (shoppingCartDTO.getSetmealId() != null) {
            // 当是添加的是套餐时，获取购物车中所有该套餐对应的物资id与数量
            shoppingCartMapper.getShoppingSetmealMaterialsById(shoppingCartDTO);
        }
        return 0;
    }

}
