package com.born.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-10-08 19:14:07
 */
@Data
public class OrderStatusVo {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "订单状态不能为空")
    private Integer orderStatus;

}
