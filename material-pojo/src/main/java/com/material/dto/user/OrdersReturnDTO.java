package com.material.dto.user;


import com.material.entity.OrderDetail;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrdersReturnDTO implements Serializable {

    //订单id
    private Long orderId;

    //返还后对应的订单详情
    private List<OrderDetail> orderDetailList;
}
