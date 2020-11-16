package com.born.service;/**
 * Created by Administrator on 2020/3/29.
 */

import com.born.domain.entity.SecGoods;
import com.born.domain.vo.SecGoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务
 * @Author:gyk
 * @Date: 2020/3/29 21:26
 **/
@Service
public class SchedulerService {

    private static final Logger log= LoggerFactory.getLogger(SchedulerService.class);

    @Autowired
    private Environment env;

    @Autowired
    private SecGoodsService secGoodsService;

    @Autowired
    private OrderService orderService;


    /**
     * 定时获取status=0的订单并判断是否超过TTL，然后进行订单失效
     *
     * 可避免RabbitMQ宕机造成的大量失效订单集中失效问题
     */
    @Scheduled(cron = "0 0/60 * * * ?")//cron表达式，每60分钟执行一次
    public void schedulerExpireOrders(){

        //try {
        //    List<OrderInfo> list= orderInfoMapper.listExpireOrder();
        //    if (list!=null && !list.isEmpty()){
        //        for (OrderInfo orderInfo:list) {
        //            if (orderInfo!=null && orderInfo.getDiffTime() > env.getProperty("scheduler.expire.orders.time",Integer.class)){
        //                orderInfoMapper.expireOrder(orderInfo.getCode());
        //                 SecKill secKill = new SecKill();
        //                secKill.setTotal(secKill.getTotal()+1);
        //                if (seckillMapper.updateKill(secKill)>0){
        //                    redisTemplate.opsForHash().increment(orderInfo.getKillId().toString(),orderInfo.getUserId(),1);
        //                }
        //                throw new Exception("定时任务回补库存失败");
        //            }
        //        }
        //    }
        //}catch (Exception e){
        //    log.error("定时获取status=0的订单并判断是否超过TTL，然后进行失效-发生异常：",e.fillInStackTrace());
        //}
    }


    /**
     * 更新Mysql信息到Redis deprecated
     *
     *
     * 初始化的数据
     * {
     *  秒杀商品ID--详细信息+商品信息
     *  秒杀商品ID--剩余库存
     *  秒杀商品ID--开始时间
     *  秒杀商品ID--结束时间
     * }
     * 定期更新redis缓存数据
     *
     * 关于订单：
     * redis 持有最新的订单信息，但是同时其中一些订单可能过期了
     * mysql 过期的订单会被删除，新的订单信息可能还在mq队列中没来得及处理
     *
     *
     * 所以最好不要从mysql更新信息到redis，可能造成最新订单被覆盖掉，直接想办法在删除mysql失效订单时也删除redis中的
     * 或者维护一个全局集合（最好是左进右出的队列），失效订单ID放里面，定时按照该集合
     */
    @Scheduled(cron = "0/3 * * * * ?")//cron表达式，每3秒执行一次
    public void updateRedisInfo(){

        //orderService.list
        List<SecGoods> list = secGoodsService.list();
        List<SecGoodsVo> secGoodsVo = secGoodsService.listWithGoods();

    }


}




































