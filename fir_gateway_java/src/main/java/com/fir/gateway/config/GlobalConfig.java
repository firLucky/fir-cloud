package com.fir.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author fir
 * @date 2023/7/28 17:53
 */
@Data
@Component
@ConfigurationProperties(prefix = "global")
public class GlobalConfig {

    /**
     * 令牌校验
     */
    private boolean tokenCheck;

    /**
     * 令牌头变量名称
     */
    private String tokenHeader;

    /**
     * 整体对称加解密
     */
    private boolean aes;

    /**
     * 整体非对称加解密
     */
    private boolean rsa;

    /**
     * 防重放攻击
     */
    private boolean replay;

    /**
     * 完整性校验
     */
    private boolean reqIntegrity;

    /**
     * 防xss
     */
    private boolean xss;

    /**
     * 全局异常捕捉-打印堆栈异常
     */
    private boolean printStackTrace;

    /**
     * 白名单ip地址-允许与网建立连接
     */
    private List<String> whiteIp;

    /**
     * 白名单路由-不进行网关校验直接放过
     */
    private List<String> whiteUrls;
}
