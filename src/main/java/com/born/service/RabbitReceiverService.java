package com.born.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.born.common.ConstantClass;
import com.born.config.redis.OrderPrefix;
import com.born.config.redis.SecGoodsPrefix;
import com.born.domain.OrderDto;
import com.born.domain.entity.Order;
import com.born.domain.entity.SecGoods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ接收消息服务
 * @Author:gyk
 * @Date: 2020/3/21 21:47
 **/
@Service
public class RabbitReceiverService {

    public static final Logger log= LoggerFactory.getLogger(RabbitReceiverService.class);

    @Autowired
    private RabbitSenderService rabbitSenderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SecGoodsService secGoodsService;

    @Autowired
    private RedisService redisService;


    /**
     * 用户秒杀成功后超时未支付-监听者，处理死信队列
     * 对订单进行失效处理
     */
    @RabbitListener(queues = {"${mq.kill.item.success.kill.dead.real.queue}"},containerFactory = "singleListenerContainer")
    public void consumeExpireOrder(OrderDto orderDto){
        try {
            Assert.notNull(orderDto,"死信队列监听者未收到订单信息");
            log.info("用户秒杀成功后超时未支付-监听者-接收消息:{}",orderDto);
            Order order = orderService.getById(orderDto.getOrderId());
            //针对0未支付状态的订单做判断，将其设置为-1失效，1已付款和2已取消是用户行为不做处理
            if (order!=null && order.getOrderStatus().toString().equals(ConstantClass.ORDER_NOT_PAY)){
                if (!orderService.expireOrder(order.getOrderId())){
                    log.error("更改订单状态为已失效失败");
                    return;
                }
                //订单失效后，应该回补mysql和redis的库存
                SecGoods secGoods = secGoodsService.getById(orderDto.getUserId());
                secGoodsService.update(new UpdateWrapper<SecGoods>().set("sec_goods_stock", secGoods.getSecGoodsStock()+1));
                redisService.incr(SecGoodsPrefix.secStock, orderDto.getKillId().toString());
            }
        }catch (Exception e){
            log.error("用户秒杀成功后超时未支付-监听者--死信队列-发生异常：",e.fillInStackTrace());
        }
    }

    /**
     * 异步操作数据库
     *  扣减秒杀商品库存、生成订单、订单号存入Redis、订单号存入订单失效队列
     * @param orderDto
     */
    @RabbitListener(queues = {"simple.kill.item.dboperations.queue"},containerFactory = "singleListenerContainer")
    //@RabbitListener(queues = {"${mq.kill.item.dbOperations.queue}"},containerFactory = "singleListenerContainer")
    public void dbOperations(OrderDto orderDto){
        try {
            Assert.notNull(orderDto,"死信队列监听者未收到数据库操作相关信息");
            log.info("用户开始修改数据库-监听者-接收消息:{}",orderDto);
            Long secGoodsId=orderDto.getKillId();
            Long userId=orderDto.getUserId();
            SecGoods secGoods = secGoodsService.getById(secGoodsId);
            if (secGoods.getSecGoodsStock()<=0){
                log.error("ID为{}的商品库存不足",orderDto.getKillId());
                throw new RuntimeException("数据库对应秒杀商品库存不足，秒杀失败");
            }
            log.info("库存扣减完毕,秒杀商品ID:{}",orderDto.getKillId());
            boolean updateStock = secGoodsService.update(new UpdateWrapper<SecGoods>().set("sec_goods_stock", secGoods.getSecGoodsStock() - 1).gt("sec_goods_stock", 0));
            if(updateStock){
                Order order = orderService.createOrder(userId, secGoodsId);
                log.info("订单创建成功，订单号：{}", order.getOrderId());
                //缓存订单编号，防止重复秒杀
                redisService.set(OrderPrefix.getOrderByUserIdSecGoodsId,""+userId+"_"+secGoodsId,order);
                //订单编号存入订单失效队列
                rabbitSenderService.sendKillSuccessOrderExpireMsg(orderDto);
            }else {
                log.error("扣减数据库ID为{}的商品库存失败",orderDto.getKillId());

            }
        }catch (Exception e){
            log.error("用户修改数据库-监听者-发生异常：",e.fillInStackTrace());
        }
    }

}












