package com.material.service.user.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.material.constant.MessageConstant;
import com.material.context.BaseContext;
import com.material.dto.user.OrdersPageQueryDTO;
import com.material.dto.user.OrdersSubmitDTO;
import com.material.entity.OrderDetail;
import com.material.entity.Orders;
import com.material.entity.ShoppingCart;
import com.material.entity.User;
import com.material.exception.OrderBusinessException;
import com.material.exception.ShoppingCartBusinessException;
import com.material.mapper.user.OrderDetailMapper;
import com.material.mapper.user.OrderMapper;
import com.material.mapper.user.ShoppingCartMapper;
import com.material.mapper.user.UserMapper;
import com.material.result.PageResult;
import com.material.service.user.OrderService;
import com.material.vo.user.OrderSubmitVO;
import com.material.vo.user.OrderVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 订单
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderDetailMapper orderDetailMapper;
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private UserMapper userMapper;



    /**
     * 用户下单（清空购物车）
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);


        // 查询当前用户的购物车数据
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        // 购物车为空异常
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        // TODO 下单前查看仓库是否有充足物资

        //构造订单数据
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(Orders.TO_BE_CONFIRMED);
        order.setUserId(userId);

        // 添加订单，id自动赋值
        orderMapper.insert(order);

        // 订单明细数据
        List<OrderDetail> orderDetailList = new LinkedList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        }

        // 向明细表插入n条数据
        orderDetailMapper.insertBatch(orderDetailList);

        // 清理购物车中的数据
        shoppingCartMapper.deleteByUserId(userId);

        // 封装返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .orderTime(order.getOrderTime())
                .build();

        return orderSubmitVO;
    }

    /**
     * 分页查询历史订单
     *
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageResult pageQuery4User(int pageNum, int pageSize, Integer status) {
        // 设置分页
        PageHelper.startPage(pageNum, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        // 分页条件查询
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList();
        long total = page.getTotal();
        // 查询出订单明细，并封装入OrderVO进行响应
        if (page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();// 订单id

                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), list);
    }

    /**
     * 用户取消订单
     *
     * @param id
     */
    @Override
    public void userCancelById(Long id) throws Exception {
        // 根据id查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //订单状态 1待处理 2已接单 3物资准备完毕 4用户使用物资 5用户归还物资 6确认物资归还状况 7已取消
        if (ordersDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());

        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderVO details(Long id) {
        // 根据id查询订单
        Orders orders = orderMapper.getById(id);

        // 查询该订单对应的物资/套餐明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将该订单及其详情封装到OrderVO并返回
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

}
