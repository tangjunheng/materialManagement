package com.material.service.user.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.material.constant.MessageConstant;
import com.material.context.BaseContext;
import com.material.dto.user.OrdersPageQueryDTO;
import com.material.dto.user.OrdersReturnDTO;
import com.material.dto.user.OrdersSubmitDTO;
import com.material.entity.OrderDetail;
import com.material.entity.Orders;
import com.material.entity.ShoppingCart;
import com.material.exception.OrderBusinessException;
import com.material.exception.ShoppingCartBusinessException;
import com.material.mapper.user.OrderDetailMapper;
import com.material.mapper.user.UserOrderMapper;
import com.material.mapper.user.ShoppingCartMapper;
import com.material.mapper.user.UserMapper;
import com.material.result.PageResult;
import com.material.service.user.UserOrderService;
import com.material.vo.user.OrderSubmitVO;
import com.material.vo.user.OrderVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 订单
 */
@Service
@Slf4j
public class UserOrderServiceImpl implements UserOrderService {
    @Resource
    private UserOrderMapper userOrderMapper;
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
        userOrderMapper.insert(order);

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
        Page<Orders> page = userOrderMapper.pageQuery(ordersPageQueryDTO);

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
        Orders ordersDB = userOrderMapper.getById(id);

        // 校验订单是否存在
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //订单状态 1待处理 2已接单 3物资准备完毕 4用户归还物资 5确认物资归还状况（完成订单） 6已取消  7出现异常
        if (ordersDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());

        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        userOrderMapper.update(orders);
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
        Orders orders = userOrderMapper.getById(id);

        // 查询该订单对应的物资/套餐明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将该订单及其详情封装到OrderVO并返回
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * 用户返还物资
     *
     * @param ordersReturnDTO
     * @return
     */
    @Override
    public void returnMaterials(OrdersReturnDTO ordersReturnDTO) {

        //提前获取数据库中的详细信息
        List<OrderDetail> dbOrderDetails = orderDetailMapper.getByOrderId(ordersReturnDTO.getOrderId());
        // 将数据库中的 OrderDetail 对象放入一个 Map 中，以 id 为键
        Map<Long, OrderDetail> dbOrderDetailMap = new HashMap<>();
        for (OrderDetail dbOrderDetail : dbOrderDetails) {
            dbOrderDetailMap.put(dbOrderDetail.getId(), dbOrderDetail);
        }

        //根据用户id查询订单，做安全验证
        // TODO 可以优化只获取status为物资准备完毕状态的订单
        List<Orders> orders = userOrderMapper.getByUserId(BaseContext.getCurrentId());


        for (int i = 0; i < orders.size(); i++) {
            if (ordersReturnDTO.getOrderId() == orders.get(i).getId()) {
                //判断status是否正常
                //订单状态 1待处理 2已接单 3物资准备完毕 4用户归还物资 5确认物资归还状况（完成订单） 6已取消  7出现异常
                if (orders.get(i).getStatus() != 3) {
                    throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
                }

                //判断订单详情是否对应上
                for (OrderDetail orderDetail : ordersReturnDTO.getOrderDetailList()) {
                    Long id = orderDetail.getId();
                    if (dbOrderDetailMap.containsKey(id)) {
                        //对比返回的数量是否大于数据库中的数量
                        OrderDetail dbOrderDetail = dbOrderDetailMap.get(id);
                        //如果大于，抛出异常
                        if (orderDetail.getNumber() > dbOrderDetail.getNumber()) {
                            throw new OrderBusinessException(MessageConstant.RETURN_MATERIAL_NUMBER_ERROR);
                        }

                        // 移除已匹配的元素
                        dbOrderDetailMap.remove(id);
                    } else {
                        //如果没找到，抛出异常
                        throw new OrderBusinessException(MessageConstant.RETURN_MATERIAL_ERROR);
                    }
                }

                //没有出现异常，则表明全部匹配成功,写入数据库
                orderDetailMapper.returnMaterials(ordersReturnDTO.getOrderDetailList());

                // 更新订单状态
                Orders orders2 = new Orders();
                orders2.setId(orders.get(i).getId());
                orders2.setStatus(Orders.RETURN_MATERIALS);
                orders2.setCancelTime(LocalDateTime.now());
                userOrderMapper.update(orders2);
                return;
            }

        }

        // 如果没有进入if条件return返回，抛出异常
        throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);

    }



}
