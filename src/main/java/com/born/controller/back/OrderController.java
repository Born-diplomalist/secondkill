package com.born.controller.back;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.born.common.Result;
import com.born.common.Status;
import com.born.common.exception.GlobalException;
import com.born.domain.entity.Order;
import com.born.domain.vo.OrderStatusVo;
import com.born.domain.vo.OrderVo;
import com.born.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  订单管理
 * </p>
 *
 * @author born
 * @since 2020-10-08
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

//    订单的增删改查

    @GetMapping("/list")
    public Result listOrder(){
        List<Order> orderList = orderService.list();
        return Result.success(orderList);
    }




    /**
     * 条件查询
     */
    @GetMapping("/list_condition")
    public Result queryOrder(Order order){
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq(StringUtils.isNotEmpty(order.getOrderId().toString()),"order_id",order.getOrderId())
                .eq(null!=order.getOrderSecGoodsId(),"order_sec_goods_id",order.getOrderSecGoodsId())
                .eq(null!=order.getOrderGoodsId(),"order_goods_id",order.getOrderGoodsId())
                .eq(null!=order.getOrderUserId(),"order_user_id",order.getOrderUserId())
                .eq(null!=order.getOrderStatus(),"order_status",order.getOrderStatus())
                .eq(null!=order.getOrderSecGoodsPrice(),"order_sec_goods_price",order.getOrderSecGoodsPrice());
        List<Order> list = orderService.list(orderQueryWrapper);
        return Result.success(list);
    }

    /**
     * 修改订单状态
     */
    @PostMapping("/edit")
    public Result changeStatus(@Validated OrderStatusVo orderStatusVo){
        Order order = orderService.getById(orderStatusVo.getOrderId());
        if (null!=order){
            boolean updateResult = orderService.update(new UpdateWrapper<Order>().set("order_status", orderStatusVo.getOrderStatus()).eq("order_id", orderStatusVo.getOrderId()));
            if (!updateResult){
                throw new GlobalException(Status.ORDER_STATUS_UPDATE_FAIL);
            }
        }
        return new Result(Status.ORDER_STATUS_UPDATE_SUCCESS);
    }



    //禁用订单

}
