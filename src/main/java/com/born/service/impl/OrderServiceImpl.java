package com.born.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.born.common.ConstantClass;
import com.born.utils.SnowFlake;
import com.born.config.redis.OrderPrefix;
import com.born.config.redis.SecGoodsPrefix;
import com.born.domain.entity.Order;
import com.born.domain.mapper.OrderMapper;
import com.born.domain.vo.SecGoodsVo;
import com.born.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.born.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author born
 * @since 2020-10-08
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisService redisService;


    private SnowFlake snowFlake = new SnowFlake(2, 3);

    @Override
    public List<Order> listEffectiveOrder() {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("order_status", -1);
        return orderMapper.selectList(queryWrapper);
    }

    @Override
    public List<Order> listNotPayOrder() {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_status", 0);
        return orderMapper.selectList(queryWrapper);
    }

    @Override
    public Order getOrderByUserIdSecGoodsId(Long userId,Long secGoodsId) {
        return redisService.get(OrderPrefix.getOrderByUserIdSecGoodsId, ""+userId+"_"+secGoodsId,Order.class);
    }

    /**
     * 生成订单
     */
    @Override
    public Order createOrder(Long userId, Long secGoodsId) {
        SecGoodsVo secGoodsVo = redisService.get(SecGoodsPrefix.secInfo, secGoodsId.toString(), SecGoodsVo.class);
        Order order = new Order();
        String orderNo = String.valueOf(snowFlake.nextId());
        order.setOrderId(orderNo);
        order.setOrderSecGoodsId(secGoodsId);
        order.setOrderUserId(userId);
        order.setOrderGoodsId(secGoodsVo.getSecGoodsGoodsId());
        order.setOrderSecGoodsPrice(secGoodsVo.getSecGoodsPrice());
        order.setOrderStatus(ConstantClass.ORDER_NOT_PAY.intValue());

        int insert = orderMapper.insert(order);
        if (insert>0){
            //insert 后主键会自动 set 到实体的 ID 字段，所以你只需要 getId() 就好
            return order;
        }
        return null;
    }


    @Override
    public boolean expireOrder(String orderId) {
        Order orderDb = orderMapper.selectById(orderId);
        Assert.notNull(orderDb,"订单编号不存在！");
        if (orderDb.getOrderStatus().toString().equals(ConstantClass.ORDER_NOT_PAY)){
            Order order = new Order();
            order.setOrderId(orderId);
            order.setOrderStatus(-1);
            int updateStatus = orderMapper.update(order, new UpdateWrapper<Order>().eq("order_id", orderId));
            return updateStatus > 0;
        }
        return false;
    }
}
