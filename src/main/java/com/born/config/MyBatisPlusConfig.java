package com.born.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//@EnableTransactionManagement  //开启事务
//@Configuration   //配置类
public class MyBatisPlusConfig {
    //注册乐观锁插件
    
    //@Bean
    //public OptimisticLockerInterceptor optimisticLockerInterceptor(){
    //    return new OptimisticLockerInterceptor();
    //}
}