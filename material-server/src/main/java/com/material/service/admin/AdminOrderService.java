package com.material.service.admin;

import com.material.dto.admin.OrdersCancelDTO;
import com.material.dto.admin.OrdersConfirmDTO;
import com.material.dto.admin.OrdersExceptionDTO;
import com.material.dto.admin.OrdersRejectionDTO;
import com.material.dto.user.OrdersPageQueryDTO;
import com.material.result.PageResult;
import com.material.vo.admin.OrderStatisticsVO;
import com.material.vo.user.OrderVO;
import org.springframework.stereotype.Service;


public interface AdminOrderService {

    /**
     * 条件搜索订单
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);


    /**
     * 各个状态的订单数量统计
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    OrderVO details(Long id);

    /**
     * 接单
     *
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     *
     * @param ordersRejectionDTO
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 商家取消订单
     *
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * 派送订单
     *
     * @param id
     */
    void delivery(Long id);

    /**
     * 完成订单
     *
     * @param id
     */
    void complete(Long id);

    /**
     * 出现异常订单(需要人工处理)
     *
     * @param ordersExceptionDTO
     */
    void haveException(OrdersExceptionDTO ordersExceptionDTO);
}
