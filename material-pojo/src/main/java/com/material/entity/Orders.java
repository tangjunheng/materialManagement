package com.material.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    /**
     * 订单状态 1待处理 2已接单 3物资准备完毕 4用户使用物资 5用户归还物资 6确认物资归还状况 7已取消
     */
    public static final Integer TO_BE_CONFIRMED = 1;
    public static final Integer CONFIRMED = 2;
    public static final Integer MATERIALS_READY = 3;
    public static final Integer USING_MATERIALS = 4;
    public static final Integer RETURN_MATERIALS = 5;
    public static final Integer RETURNED_MATERIALS_CONFIRMED = 6;
    public static final Integer CANCELLED = 7;


    private static final long serialVersionUID = 1L;

    private Long id;

    // 订单状态 1待处理 2已接单 3物资准备完毕 4用户使用物资 5用户已归还物资 6管理员已确认物资归还状况 7已取消
    private Integer status;

    // 备注
    private String remark;

    // 用户id
    private Long userId;

    // 订单取消原因
    private String cancelReason;

    // 订单拒绝原因
    private String rejectionReason;

    // 订单取消时间
    private LocalDateTime cancelTime;

    // 下单时间
    private LocalDateTime orderTime;

    // 预约使用物资时间
    private LocalDateTime expectedUseTime;

    // 预计归还时间
    private LocalDateTime expectedReturnTime;

    // 物资准备完毕时间
    private LocalDateTime readyTime;

    // 实际归还时间
    private LocalDateTime returnTime;



}
