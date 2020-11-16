package com.born.controller.front;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.born.common.ConstantClass;
import com.born.common.Result;
import com.born.common.Status;
import com.born.common.exception.GlobalException;
import com.born.config.redis.OrderPrefix;
import com.born.config.redis.SecGoodsPrefix;
import com.born.config.redis.UserPrefix;
import com.born.domain.OrderDto;
import com.born.domain.entity.Goods;
import com.born.domain.entity.Order;
import com.born.domain.entity.SecGoods;
import com.born.domain.entity.User;
import com.born.domain.vo.SecGoodsVo;
import com.born.service.OrderService;
import com.born.service.RabbitSenderService;
import com.born.service.RedisService;
import com.born.service.SecGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-10-08 21:33:49
 */
@Slf4j
@RestController
@RequestMapping("/front/seckill")
public class FrontSecGoodsController {

    @Autowired
    private SecGoodsService secGoodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RabbitSenderService rabbitSenderService;


    /**
     * 内存标记，标记每个商品的库存状态，如果某商品售空，对应的value就会为true
     * --- 减少对Redis的访问
     */
    private Map<Long,Boolean> localSellOverMap=new HashMap<>();


//    查看秒杀商品详情    秒杀     遍历

    @GetMapping("/list")
    public Result listSecKill(){
        List<SecGoodsVo> secGoodsVoResult=secGoodsService.listWithGoods();
        return Result.success(secGoodsVoResult);
    }


    @GetMapping("/get/{secGoodsId}")
    public Result getSecKillDetail(@PathVariable(name = "secGoodsId") Long secGoodsId){
        SecGoodsVo secGoodsVo=secGoodsService.getWithGoods(secGoodsId);
        return Result.success(secGoodsVo);
    }


    /**
     * 初始化数据库信息到Redis中
     *
     * 该方法应该在容器启动时执行一次
     * 而且不能被执行多次，否则mysql信息会覆盖redis的信息
     */
    public void initSecKillInfoToRedis(){

        //缓存已支付订单信息
        List<Order> orderList = orderService.listEffectiveOrder();
        for (Order order : orderList) {
            redisService.set(OrderPrefix.effectiveOrder,order.getOrderId().toString(), ConstantClass.USER_HAS_SECKILLED);
        }

        //缓存秒杀商品信息
        List<SecGoodsVo> secGoodsVoList = secGoodsService.listWithGoods();
        for (SecGoodsVo secGoodsVo :secGoodsVoList) {
            redisService.set(SecGoodsPrefix.secInfo, secGoodsVo.getSecGoodsId().toString(),secGoodsVo);
            redisService.set(SecGoodsPrefix.secStock, secGoodsVo.getSecGoodsId().toString(),secGoodsVo.getSecGoodsStock());
            redisService.set(SecGoodsPrefix.secStartTime, secGoodsVo.getSecGoodsId().toString(),secGoodsVo.getSecGoodsStartTime());
            redisService.set(SecGoodsPrefix.secEndTime, secGoodsVo.getSecGoodsId().toString(),secGoodsVo.getSecGoodsEndTime());
            localSellOverMap.put(secGoodsVo.getSecGoodsId(),secGoodsVo.getSecGoodsStock()<=0);
        }
    }



    /**
     * 执行秒杀
     */
    @PostMapping("do_seckill")
    public Result doSecKill(HttpServletRequest request, @RequestParam Long secGoodsId){
        User user = getUser(request);
        if (user==null){
            return new Result(Status.USER_INFO_LOSS);
        }
        Long userId=user.getUserId();
        if (secGoodsId==null || secGoodsId<0){
            return new Result(Status.BIND_ERROR);
        }
        //内存标记，减少Redis访问
        if (localSellOverMap.get(secGoodsId)){
            return new Result(Status.SECKILL_SELL_OVER);
        }
        //预减Redis库存并判断剩余库存
        Long decr = redisService.decr(SecGoodsPrefix.secStock, secGoodsId.toString());
        System.out.println("剩余库存：-------------------------------------------"+decr);
        if (decr<0){
            localSellOverMap.put(secGoodsId,true);
            return new Result(Status.SECKILL_SELL_OVER);
        }
        //判断重复秒杀
        Order orderByUserIdSecGoodsId = orderService.getOrderByUserIdSecGoodsId(userId, secGoodsId);
        if (orderByUserIdSecGoodsId!=null){
            return new Result(Status.SECKILL_REPEAT);
        }
        Order order = orderService.createOrder(userId, secGoodsId);
        //TODO: 将创建订单用到的信息添加到消息队列，监听到该消息后异步创建订单

        redisService.set(OrderPrefix.getOrderByUserIdSecGoodsId,""+userId+"_"+secGoodsId,order);

        return new Result(Status.SUCCESS);
    }

    /**
     * 执行秒杀---压测
     */
    @PostMapping("do_seckill_test_nomq")
    public Result doSecKillTestNoMq(@RequestParam Long secGoodsId,@RequestParam Long userId) throws InterruptedException {
        if (secGoodsId==null || secGoodsId<0){
            return new Result(Status.BIND_ERROR);
        }
        //内存标记，减少Redis访问
        if (localSellOverMap.get(secGoodsId)){
            return new Result(Status.SECKILL_SELL_OVER);
        }
        //判断Redis中的库存
        if (redisService.get(SecGoodsPrefix.secStock, secGoodsId.toString(),Long.class)<=0L){
            localSellOverMap.put(secGoodsId,true);
            return new Result(Status.SECKILL_SELL_OVER);
        }
        //预减Redis库存并判断剩余库存
        Long decr = redisService.decr(SecGoodsPrefix.secStock, secGoodsId.toString());
        System.out.println("剩余库存：------------------------------------------||||"+decr);

        //TODO: 由于订单是异步生成，为了避免生成过程中有重复秒杀的情况，可以再设置一个键值对，以用户名+秒杀商品+一个新的前缀为key，在DB操作之前，将其设置为true，表示已有订单，在DB完成后，订单生成之后，再将对应的key删除掉XXXX 该问题分布式锁会将其拦住，无需再设置key


        //判断重复秒杀
        Order orderByUserIdSecGoodsId = orderService.getOrderByUserIdSecGoodsId(userId, secGoodsId);
        if (orderByUserIdSecGoodsId!=null){
            return new Result(Status.SECKILL_REPEAT);
        }

        //StringBuilder线程不安全，String浪费空间
        final String lockKey = new StringBuffer().append(secGoodsId).append(userId).append("-RedissonLock").toString();

        //redisson的分布式锁
        RLock lock = redissonClient.getLock(lockKey);
        try {

            boolean getLock = lock.tryLock(30, 10, TimeUnit.SECONDS);
            if (getLock) {
                OrderDto orderDto = new OrderDto();
                orderDto.setKillId(secGoodsId);
                orderDto.setUserId(userId);
                dbOperations(orderDto);
                return new Result(Status.SECKILL_WAITING);
            }
        } finally {
            lock.unlock();
            //lock.forceUnlock();//强制释放
        }
        return new Result(Status.SECKILL_FAIL);
    }

    public void dbOperations(OrderDto orderDto){
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
            }

    }


    /**
     * 执行秒杀---压测 异步生成订单
     */
    @PostMapping("do_seckill_test")
    public Result doSecKillTest(@RequestParam Long secGoodsId,@RequestParam Long userId) throws InterruptedException {
        if (secGoodsId==null || secGoodsId<0){
            return new Result(Status.BIND_ERROR);
        }
        //内存标记，减少Redis访问
        if (localSellOverMap.get(secGoodsId)){
            return new Result(Status.SECKILL_SELL_OVER);
        }
        //判断Redis中的库存
        if (redisService.get(SecGoodsPrefix.secStock, secGoodsId.toString(),Long.class)<=0L){
            localSellOverMap.put(secGoodsId,true);
            return new Result(Status.SECKILL_SELL_OVER);
        }
        //预减Redis库存并判断剩余库存
        Long decr = redisService.decr(SecGoodsPrefix.secStock, secGoodsId.toString());
        System.out.println("剩余库存：------------------------------------------||||"+decr);

        //TODO: 由于订单是异步生成，为了避免生成过程中有重复秒杀的情况，可以再设置一个键值对，以用户名+秒杀商品+一个新的前缀为key，在DB操作之前，将其设置为true，表示已有订单，在DB完成后，订单生成之后，再将对应的key删除掉XXXX 该问题分布式锁会将其拦住，无需再设置key


        //判断重复秒杀
        Order orderByUserIdSecGoodsId = orderService.getOrderByUserIdSecGoodsId(userId, secGoodsId);
        if (orderByUserIdSecGoodsId!=null){
            return new Result(Status.SECKILL_REPEAT);
        }

        //StringBuilder线程不安全，String浪费空间
        final String lockKey = new StringBuffer().append(secGoodsId).append(userId).append("-RedissonLock").toString();

        //redisson的分布式锁
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean getLock = lock.tryLock(30, 10, TimeUnit.SECONDS);
            if (getLock) {
                OrderDto orderDto = new OrderDto();
                orderDto.setKillId(secGoodsId);
                orderDto.setUserId(userId);
                rabbitSenderService.secKillDbOperations(orderDto);
                return new Result(Status.SECKILL_WAITING);
            }
        } finally {
            lock.unlock();
            //lock.forceUnlock();//强制释放
        }
        return new Result(Status.SECKILL_FAIL);
    }


    /**
     * 设定数据库和Redis中的库存和订单信息，方便压测之后的还原
     * @param secGoodsId
     * @param secGoodsStock
     */
    @PostMapping("/initTest")
    public void setTestEnvironMent(Long secGoodsId,Integer secGoodsStock){
        SecGoods secGoods = new SecGoods();
        secGoodsService.update(new UpdateWrapper<SecGoods>().set("sec_goods_stock",secGoodsStock).eq("sec_goods_id", secGoodsId));
        orderService.remove(new QueryWrapper<Order>().ne("order_id", -1));
        redisService.set(SecGoodsPrefix.secStock, secGoodsId.toString(), secGoodsStock);
        redisService.delete(OrderPrefix.getOrderByUserIdSecGoodsId);
    }


    /**
    * 从cookie中拿到token进一步获取到用户信息
     */
    private User getUser(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies==null){
            throw new GlobalException(Status.USER_INFO_LOSS);
        }
        for (Cookie cookie: cookies) {
            if ("token".equals(cookie.getName())){
                return redisService.get(UserPrefix.token, cookie.getValue(), User.class);
            }
        }
        return null;
    }
}
