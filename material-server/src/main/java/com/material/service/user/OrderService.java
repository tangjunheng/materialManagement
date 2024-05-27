package com.material.service.user;

import com.material.dto.user.OrdersSubmitDTO;
import com.material.vo.user.OrderSubmitVO;

public interface OrderService {
    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
