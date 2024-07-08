package com.material.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.material.constant.MessageConstant;
import com.material.dto.admin.OrdersCancelDTO;
import com.material.dto.admin.OrdersConfirmDTO;
import com.material.dto.admin.OrdersExceptionDTO;
import com.material.dto.admin.OrdersRejectionDTO;
import com.material.dto.user.OrdersPageQueryDTO;
import com.material.entity.OrderDetail;
import com.material.entity.Orders;
import com.material.exception.OrderBusinessException;
import com.material.exception.StatusErrorException;
import com.material.mapper.admin.AdminOrderMapper;
import com.material.mapper.user.OrderDetailMapper;
import com.material.result.PageResult;
import com.material.service.admin.AdminOrderService;
import com.material.vo.admin.OrderStatisticsVO;
import com.material.vo.user.OrderVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminOrderServiceImpl implements AdminOrderService {

    @Resource
    private AdminOrderMapper adminOrderMapper;

    @Resource
    private OrderDetailMapper orderDetailMapper;
    /**
     * 条件搜索订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> page = adminOrderMapper.pageQuery(ordersPageQueryDTO);

        // 部分订单状态，需要额外返回订单物资信息，将Orders转化为OrderVO
        List<OrderVO> orderVOList = getOrderVOList(page);

        return new PageResult(page.getTotal(), orderVOList);
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    @Override
    public OrderStatisticsVO statistics() {
        // 查询出状态
        List<Integer> statusList = adminOrderMapper.getStatus();
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();

        for (Integer status : statusList){
            if (status ==Orders.TO_BE_CONFIRMED){
                orderStatisticsVO.setToBeConfirmed(orderStatisticsVO.getToBeConfirmed()+1);
            }else if (status == Orders.CONFIRMED){
                orderStatisticsVO.setConfirmed(orderStatisticsVO.getConfirmed()+1);
            }else if (status == Orders.MATERIALS_READY){
                orderStatisticsVO.setMaterialsReady(orderStatisticsVO.getMaterialsReady()+1);
            }else if (status == Orders.RETURN_MATERIALS){
                orderStatisticsVO.setReturnMaterials(orderStatisticsVO.getUsingMaterials());
            }else if (status == Orders.RETURNED_MATERIALS_CONFIRMED){
                orderStatisticsVO.setReturnMaterialsConfirmed(orderStatisticsVO.getReturnMaterialsConfirmed()+1);
            }else if (status == Orders.CANCELLED) {
                orderStatisticsVO.setCanceled(orderStatisticsVO.getCanceled()+1);
            }else {
                throw new StatusErrorException(MessageConstant.ORDER_STATUS_ERROR);
            }
        }

        return orderStatisticsVO;
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
        Orders orders = adminOrderMapper.getById(id);

        // 查询该订单对应的菜品/套餐明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将该订单及其详情封装到OrderVO并返回
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * 接单
     *
     * @param ordersConfirmDTO
     */
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        // 根据id查询订单
        Orders ordersDB = adminOrderMapper.getById(ordersConfirmDTO.getId());

        // 订单只有存在且状态为1（待接单）才可以拒单
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();

        adminOrderMapper.update(orders);
    }

    /**
     * 拒单
     *
     * @param ordersRejectionDTO
     */
    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        // 根据id查询订单
        Orders ordersDB = adminOrderMapper.getById(ordersRejectionDTO.getId());

        // 订单只有存在且状态为1（待接单）才可以拒单
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }


        // 根据订单id更新订单状态、拒单原因、取消时间
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());

        adminOrderMapper.update(orders);
    }

    /**
     * 商家取消订单
     *
     * @param ordersCancelDTO
     */
    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception {
        // 根据id查询订单
        Orders ordersDB = adminOrderMapper.getById(ordersCancelDTO.getId());

        // 根据订单id更新订单状态、取消原因、取消时间
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        adminOrderMapper.update(orders);
    }

    /**
     * 派送订单
     *
     * @param id
     */
    @Override
    public void delivery(Long id) {
        // 根据id查询订单
        Orders ordersDB = adminOrderMapper.getById(id);

        // 校验订单是否存在，并且状态为ORDER_STATUS_ERROR
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setReadyTime(LocalDateTime.now());
        // 更新订单状态,状态转为物资准备完毕
        orders.setStatus(Orders.MATERIALS_READY);

        adminOrderMapper.update(orders);
    }

    /**
     * 完成订单
     *
     * @param id
     */
    @Override
    public void complete(Long id) {
        // 根据id查询订单
        Orders ordersDB = adminOrderMapper.getById(id);

        // 校验订单是否存在，并且状态为5
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.RETURN_MATERIALS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为完成
        orders.setStatus(Orders.RETURNED_MATERIALS_CONFIRMED);
        orders.setCompleteTime(LocalDateTime.now());

        adminOrderMapper.update(orders);
    }

    /**
     * 出现异常订单(需要人工处理)
     *
     * @param ordersExceptionDTO
     */
    @Override
    public void haveException(OrdersExceptionDTO ordersExceptionDTO) {
        // 根据id查询订单
        Orders ordersDB = adminOrderMapper.getById(ordersExceptionDTO.getId());

        // 根据订单id更新订单状态、取消原因、取消时间
        Orders orders = new Orders();
        orders.setId(ordersExceptionDTO.getId());
        orders.setStatus(Orders.HAVE_EXCEPTION);
        orders.setExceptionMessage(ordersExceptionDTO.getExceptionMessage());
        adminOrderMapper.update(orders);
    }


    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        // 需要返回订单物资信息，自定义OrderVO响应结果
        List<OrderVO> orderVOList = new ArrayList<>();

        List<Orders> ordersList = page.getResult();
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                // 将共同字段复制到OrderVO
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                String orderMaterials = getOrderMaterialsStr(orders);

                // 将订单物资信息封装到orderVO中，并添加到orderVOList
                orderVO.setOrderMaterials(orderMaterials);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }

    /**
     * 根据订单id获取物资信息字符串
     *
     * @param orders
     * @return
     */
    private String getOrderMaterialsStr(Orders orders) {
        // 查询订单物资详情信息（订单中的物资和数量）
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将每一条订单物资信息拼接为字符串（格式：图片*3；）
        List<String> orderMaterialList = orderDetailList.stream().map(x -> {
            String ordermaterial = x.getName() + "*" + x.getNumber() + ";";
            return ordermaterial;
        }).collect(Collectors.toList());

        // 将该订单对应的所有物资信息拼接在一起
        return String.join("", orderMaterialList);
    }
}
