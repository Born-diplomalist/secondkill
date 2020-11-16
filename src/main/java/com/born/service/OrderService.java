package com.born.service;

import com.born.domain.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author born
 * @since 2020-10-08
 */
public interface OrderService extends IService<Order> {

    //获取所有有效订单
    List<Order> listEffectiveOrder();


    //获取所有未支付订单
    List<Order> listNotPayOrder();

    //
    Order getOrderByUserIdSecGoodsId(Long userId,Long secGoodsId);

    Order createOrder(Long userId,Long secGoodsId);

    /**
     * 如果订单未支付，将其状态更改为失效
     * @param orderId
     * @return
     */
    boolean expireOrder(String orderId);
}
