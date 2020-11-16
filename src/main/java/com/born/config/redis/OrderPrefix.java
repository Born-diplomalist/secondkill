package com.born.config.redis;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-04-11 12:40:33
 */
public class OrderPrefix extends BasePrefix {

    public static final int TOKEN_EXPIRE=3600*24*2;

    public OrderPrefix(int expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }

    //订单信息前缀
    public static OrderPrefix effectiveOrder=new OrderPrefix(TOKEN_EXPIRE,"eff");

    public static OrderPrefix getOrderByUserIdSecGoodsId=new OrderPrefix(TOKEN_EXPIRE,"orderbyusersec");

}
