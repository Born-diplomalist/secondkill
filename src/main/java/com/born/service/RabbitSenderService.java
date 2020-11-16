package com.born.service;/**
 * Created by Administrator on 2020/3/21.
 */

import cn.hutool.core.lang.Assert;
import com.born.domain.OrderDto;
import com.born.domain.entity.Order;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ发送邮件服务
 *
 * 业务模块的服务化、功能模块的解耦
 *
 * @Author:gyk
 * @Date: 2020/3/21 21:47
 **/
@Service
public class RabbitSenderService {

    public static final Logger log= LoggerFactory.getLogger(RabbitSenderService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private Environment env;




    //发送消息测试
    //Message message=MessageBuilder.withBody(orderNo.getBytes("UTF-8")).build();
    //rabbitTemplate.convertAndSend(message);

    /**
     * 发送新生成的未支付订单信息到消息队列，并设置超时时间
     * 使用try-catch，直接处理异常，避免影响到主模块
     */
    public void sendKillSuccessOrderExpireMsg(final OrderDto orderDto) {
        Assert.notNull(orderDto,"订单信息未接收到");
        String orderId = orderDto.getOrderId();
        try {
            if (StringUtils.isNotBlank(orderId)){
                Order order = orderService.getById(orderId);
                if (order!=null){
                    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                    //设置基本交换机和基本路由
                    rabbitTemplate.setExchange(env.getProperty("mq.kill.item.success.kill.dead.prod.exchange"));
                    rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.item.success.kill.dead.prod.routing.key"));
                    rabbitTemplate.convertAndSend(order, new MessagePostProcessor() {
                        @Override
                        public Message postProcessMessage(Message message) throws AmqpException {
                            MessageProperties mp=message.getMessageProperties();
                            //设置消息传输模式为持久化
                            mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            //设置消息头
                            mp.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, Order.class);
                            //TODO：动态设置TTL
                            mp.setExpiration(env.getProperty("mq.kill.item.success.kill.expire"));
                            return message;
                        }
                    });
                }
            }
        }catch (Exception e){
            log.error("秒杀成功后生成抢购订单消息-发送信息入死信队列，等待着一定时间失效超时未支付的订单-发生异常，消息为：{}",orderId,e.fillInStackTrace());
        }
    }

    /**
     * 执行DB操作
     * @param orderDto
     */
    public void secKillDbOperations(OrderDto orderDto) {
        Assert.notNull(orderDto,"秒杀相关信息未接收到");
        try {
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            //设置基本交换机和基本路由
            rabbitTemplate.setExchange(env.getProperty("mq.kill.item.dboperations.exchange"));
            rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.item.dboperations.routing.key"));
            rabbitTemplate.convertAndSend(orderDto, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    MessageProperties mp=message.getMessageProperties();
                    //设置消息传输模式为持久化
                    mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    //设置消息头
                    mp.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, OrderDto.class);
                    //TODO：动态设置TTL
                    //mp.setExpiration(env.getProperty("mq.kill.item.dboperations.expire"));
                    return message;
                }
            });
        }catch (Exception e){
            log.error("秒杀时数据库操作失败，对应秒杀ID为：{}，对应用户ID为：{}",orderDto.getKillId(),orderDto.getUserId(),e.fillInStackTrace());
        }

    }

}




























