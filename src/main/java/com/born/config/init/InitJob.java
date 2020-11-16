package com.born.config.init;

import com.born.common.ConstantClass;
import com.born.config.redis.OrderPrefix;
import com.born.config.redis.SecGoodsPrefix;
import com.born.controller.back.SecGoodsController;
import com.born.controller.front.FrontSecGoodsController;
import com.born.domain.entity.Order;
import com.born.domain.vo.SecGoodsVo;
import com.born.service.OrderService;
import com.born.service.RedisService;
import com.born.service.SecGoodsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: 容器初始化时的操作
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-04-04 10:18:42
 */
@Component
public class InitJob implements InitializingBean {
    //
    //@Autowired
    //private RedisTemplate redisTemplate;

    @Autowired
    private FrontSecGoodsController frontSecGoodsController;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderService orderService;

        @Override
    public void afterPropertiesSet(){
        frontSecGoodsController.initSecKillInfoToRedis();
        org.apache.ibatis.logging.LogFactory.useSlf4jLogging();
    }


}
