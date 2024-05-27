package com.material.mapper.user;

import com.github.pagehelper.Page;
import com.material.entity.Orders;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据，id自动赋值
     * @param orders
     */
    void insert(Orders orders);


}
