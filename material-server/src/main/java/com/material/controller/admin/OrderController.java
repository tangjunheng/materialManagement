package com.material.controller.admin;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.material.dto.admin.OrdersCancelDTO;
import com.material.dto.admin.OrdersConfirmDTO;
import com.material.dto.admin.OrdersExceptionDTO;
import com.material.dto.admin.OrdersRejectionDTO;
import com.material.dto.user.OrdersPageQueryDTO;
import com.material.result.PageResult;
import com.material.result.Result;
import com.material.service.admin.AdminOrderService;
import com.material.vo.admin.OrderStatisticsVO;
import com.material.vo.user.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Tag(name = "订单管理接口")
public class OrderController {

    @Resource
    private AdminOrderService adminOrderService;

    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @Operation(
            description = "订单搜索",
            summary = "订单搜索"
    )
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = adminOrderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    @GetMapping("/statistics")
    @Operation(
            description = "各个状态的订单数量统计",
            summary = "各个状态的订单数量统计"
    )
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO orderStatisticsVO = adminOrderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @Operation(
            description = "查询订单详情",
            summary = "查询订单详情"
    )
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        OrderVO orderVO = adminOrderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 接单（将status改成2）
     *
     * @return
     */
    @PutMapping("/confirm")
    @Operation(
            description = "接单",
            summary = "接单"
    )
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        adminOrderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 拒单
     *
     * @return
     */
    @PutMapping("/rejection")
    @Operation(
            description = "拒单",
            summary = "拒单"
    )
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        adminOrderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 取消订单
     *
     * @return
     */
    @PutMapping("/cancel")
    @Operation(
            description = "取消订单",
            summary = "取消订单"
    )
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        adminOrderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 物资准备完毕
     *
     * @return
     */
    @PutMapping("/delivery/{id}")
    @Operation(
            description = "派送订单",
            summary = "派送订单"
    )
    public Result delivery(@PathVariable("id") Long id) throws JsonProcessingException {
        adminOrderService.delivery(id);
        return Result.success();
    }

    /**
     * 完成订单（确认完物资归还状态，将status改成6）
     *
     * @return
     */
    @PutMapping("/complete/{id}")
    @Operation(
            description = "完成订单",
            summary = "完成订单"
    )
    public Result complete(@PathVariable("id") Long id) {
        adminOrderService.complete(id);
        return Result.success();
    }

    /**
     * 订单出现异常（一般是用户归还出现问题，或者准备的物资出现问题需要人工处理）
     */
    @PutMapping("/exception")
    @Operation(
            description = "订单出现异常",
            summary = "订单出现异常"
    )
    public Result haveException(@RequestBody OrdersExceptionDTO ordersExceptionDTO) {
        adminOrderService.haveException(ordersExceptionDTO);
        return Result.success();
    }
}
