package com.born.config.redis;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-04-11 12:40:33
 */
public class UserPrefix extends BasePrefix {

    public static final int TOKEN_EXPIRE=3600*24*2;

    public UserPrefix(int expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }

    //用户模块token前缀
    public static UserPrefix token=new UserPrefix(TOKEN_EXPIRE,"tk");

    //检测某用户是否已有token的前缀
    public static UserPrefix checkTokenExist=new UserPrefix(TOKEN_EXPIRE,"tkexist");



}
