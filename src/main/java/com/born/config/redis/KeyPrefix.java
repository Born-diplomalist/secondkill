package com.born.config.redis;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-10-08 09:52:16
 */
public interface KeyPrefix {

    /**
     * 超时时间
     * @return
     */
    int getExpireSeconds();

    /**
     * 前缀
     * @return
     */
    String getPrefix();

}
