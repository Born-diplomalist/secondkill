package com.born.config.redis;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-04-11 12:40:33
 */
public class SecGoodsPrefix extends BasePrefix {

    public static final int TOKEN_EXPIRE=3600*24*2;

    public SecGoodsPrefix(int expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }

    //秒杀商品信息前缀
    public static SecGoodsPrefix secInfo=new SecGoodsPrefix(TOKEN_EXPIRE,"secinfo");

    //秒杀商品信息前缀
    public static SecGoodsPrefix secStock=new SecGoodsPrefix(TOKEN_EXPIRE,"secstock");

    //秒杀商品信息前缀
    public static SecGoodsPrefix secStartTime=new SecGoodsPrefix(TOKEN_EXPIRE,"secstarttime");

    //秒杀商品信息前缀
    public static SecGoodsPrefix secEndTime=new SecGoodsPrefix(TOKEN_EXPIRE,"secendtime");

}
