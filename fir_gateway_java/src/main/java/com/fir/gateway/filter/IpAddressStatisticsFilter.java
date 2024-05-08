package com.fir.gateway.filter;

import com.fir.gateway.config.GlobalConfig;
import com.fir.gateway.config.exception.CustomException;
import com.fir.gateway.config.result.AjaxStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 记录访问ip地址-于访问次数
 *
 * @author fir
 */
@Slf4j
@Component
public class IpAddressStatisticsFilter implements GlobalFilter, Ordered {


    /**
     * 网关参数配置
     */
    @Resource
    private GlobalConfig globalConfig;

    /**
     * 用于保存次数的缓存
     */
    public static final Map<String, AtomicInteger> CACHE = new ConcurrentHashMap<>();


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("访问来源:start");
        ServerHttpRequest req = exchange.getRequest();
        InetSocketAddress host = exchange.getRequest().getHeaders().getHost();
        int originPort = Objects.requireNonNull(req.getRemoteAddress()).getPort();
        if (host == null || host.getHostName() == null) {
            throw new CustomException(AjaxStatus.BAD_REQUEST);
        }
        String hostName = host.getHostName();

        List<String> whiteIp = globalConfig.getWhiteIp();

        if (!whiteIp.contains(hostName)) {
            log.error("未被允许的ip地址请求:{}", hostName);
            log.info("访问来源:false");
            throw new CustomException(AjaxStatus.ILLEGAL_REQUEST);
        }


        AtomicInteger count = CACHE.getOrDefault(hostName, new AtomicInteger(0));
        count.incrementAndGet();
        String method = req.getMethodValue();
        CACHE.put(hostName, count);
        log.info("IP地址:{},端口:{},访问次数:{}", hostName, originPort, count.intValue());
        String urlPah = req.getPath().value();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String dataTime = formatter.format(date);
        log.info("时间: {} 请求地址: {} 当前请求类型为: {}", dataTime, urlPah, method);

        log.info("访问来源:true");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -300;
    }
}

