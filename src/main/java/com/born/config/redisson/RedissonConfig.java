package com.born.config.redisson;

import com.born.config.redis.RedisConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * redisson通用化配置
 * @Author: gyk
 * @Date: 2020/3/2 10:57
 **/
@Configuration
public class RedissonConfig {

    @Autowired
    RedisConfig redisConfig;

    @Bean
    public RedissonClient redissonClient(){
        Config config=new Config();
        config.useSingleServer()
                .setAddress("http://"+redisConfig.getHost()+":"+redisConfig.getPort());
                //.setPassword(redisConfig.getPassword());
        RedissonClient client=Redisson.create(config);
        return client;
    }


}