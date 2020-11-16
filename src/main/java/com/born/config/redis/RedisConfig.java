package com.born.config.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @Description: Redis客户端自定义配置
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-04-09 17:02:32
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisConfig {
    //private String host="192.168.235.131";
    private String host;
    private Integer port;
    private Integer timeOut;//秒
    //private String password="123456";
    private Integer poolMaxTotal;
    private Integer poolMaxIdle;
    private Integer poolMaxWait;//秒

}
