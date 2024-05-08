package com.fir.gateway.config;

import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author fir
 * @date 2023/5/4 14:24
 */
@Component
public class GatewayConfig {


    /**
     * 设置每次登录的过期时间
     */
    public final static Integer TIME_NUM = 30;


    /**
     * 设置每次登录的过期时间单位
     */
    public final static TimeUnit TIME_UNIT = TimeUnit.MINUTES;


    /**
     * 设置每次登录的过期时间
     */
    public final static Integer RSA_NUM = 3;


    /**
     * 设置每次登录的过期时间单位
     */
    public final static TimeUnit RSA_UNIT = TimeUnit.MINUTES;
}
