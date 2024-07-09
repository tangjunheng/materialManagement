package com.material.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.material.dto.user.OrdersReturnDTO;
import com.material.dto.user.OrdersSubmitDTO;
import com.material.result.PageResult;
import com.material.vo.user.OrderSubmitVO;
import com.material.vo.user.OrderVO;

public interface UserOrderService {
    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) throws JsonProcessingException;

    /**
     * 分页查询历史订单
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQuery4User(int page, int pageSize, Integer status);

    /**
     * 用户取消订单
     * @param id
     */
    void userCancelById(Long id) throws Exception;

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    OrderVO details(Long id);

    /**
     * 用户返还物资
     *
     * @return
     */
    void returnMaterials(OrdersReturnDTO ordersReturnDTO);
}
