package com.material.mapper.admin;

import com.github.pagehelper.Page;
import com.material.dto.user.OrdersPageQueryDTO;
import com.material.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminOrderMapper {
    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);


    /**
     * 获取整张表Status
     * @return
     */
    @Select("select status from orders")
    List<Integer> getStatus();

    /**
     * 根据id查询订单
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);
}
