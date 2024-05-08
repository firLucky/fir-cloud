package com.fir.gateway.filter.request;

import com.fir.gateway.config.GlobalConfig;
import com.fir.gateway.config.exception.CustomException;
import com.fir.gateway.config.result.AjaxStatus;
import com.fir.gateway.singleton.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * 令牌token校验拦截器
 *
 * @author fir
 */
@Slf4j
@Component
public class AuthorizationFilter implements GlobalFilter, Ordered {


    /**
     * 网关参数配置
     */
    private  GlobalConfig globalConfig;


    @Autowired
    public void someComponent(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("登录信息校验:start");
        // 判断token是否存在，且是否过期
        boolean tokenKey = globalConfig.isTokenCheck();
        if(tokenKey) {
            // 白名单路由判断
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();
            List<String> whiteUrls = globalConfig.getWhiteUrls();

            if(!whiteUrls.contains(path)){
                String tokenHeader = globalConfig.getTokenHeader();
                String token = exchange.getRequest().getHeaders().getFirst(tokenHeader);

                if (token == null || "".equals(token)) {
                    log.error("token为空");
                    throw new CustomException(AjaxStatus.EXPIRATION_TOKEN);
                }

                Object obj = Singleton.getSingleton().redisTemplate.opsForValue().get(token);
                if (obj != null) {
                    log.info("登录信息校验:true");
                } else {
                    log.error("token已失效");
                    throw new CustomException(AjaxStatus.EXPIRATION_TOKEN);
                }
            }else {
                log.info("登录信息校验:true,白名单");
            }
        }else {
            log.info("登录信息校验:true,验证已关闭");
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -290;
    }
}

