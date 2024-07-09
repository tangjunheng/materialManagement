package com.material.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.material.dto.user.OrdersReturnDTO;
import com.material.dto.user.OrdersSubmitDTO;
import com.material.result.PageResult;
import com.material.result.Result;
import com.material.service.user.UserOrderService;
import com.material.vo.user.OrderSubmitVO;
import com.material.vo.user.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 订单
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Tag(name = "C端-订单接口")
public class OrderController {

    @Resource
    private UserOrderService userOrderService;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @Operation(
            description = "用户下单",
            summary = "用户下单"
    )
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) throws JsonProcessingException {
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = userOrderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @Operation(
            description = "查询订单详情",
            summary = "查询订单详情"
    )
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        OrderVO orderVO = userOrderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 历史订单查询
     * @param page
     * @param pageSize
     * @param status 订单状态 1待处理 2已接单 3物资准备完毕 4用户归还物资 5确认物资归还状况（完成订单） 6已取消  7出现异常
     * @return
     */
    @GetMapping("/historyOrders")
    @Operation(
            description = "历史订单查询",
            summary = "历史订单查询"
    )
    public Result<PageResult> page(int page, int pageSize, Integer status) {
        // TODO 优化可使用OrdersPageQueryDTO添加根据时间分页
        PageResult pageResult = userOrderService.pageQuery4User(page, pageSize, status);
        return Result.success(pageResult);
    }


    /**
     * 用户取消订单
     *
     * @return
     */
    @PutMapping("/cancel/{id}")
    @Operation(
            description = "取消订单",
            summary = "取消订单"
    )
    public Result cancel(@PathVariable("id") Long id) throws Exception {
        log.info("用户取消订单：{}", id);
        userOrderService.userCancelById(id);
        return Result.success();
    }

    /**
     * 用户返还物资
     *
     * @return
     */
    @PutMapping("/return")
    @Operation(
            description = "用户返还物资",
            summary = "用户返还物资"
    )
    public Result returnMaterials(@RequestBody OrdersReturnDTO ordersReturnDTO) throws Exception {
        log.info("用户取消订单：{}", ordersReturnDTO.getOrderId());
        userOrderService.returnMaterials(ordersReturnDTO);
        return Result.success();
    }

    // TODO 用户报告出现异常




}
