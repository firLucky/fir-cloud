package com.fir.gateway.filter.request;

import com.fir.gateway.config.GlobalConfig;
import com.fir.gateway.entity.XssWhiteUrl;
import com.fir.gateway.uttls.XssUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;


/**
 * XSS过滤
 *
 * @author lieber
 */
@Data
@Slf4j
@ConfigurationProperties("config.form.xss")
@Component
public class XssFormFilter implements GlobalFilter, Ordered {


    /**
     * 网关参数配置
     */
    @Resource
    private GlobalConfig globalConfig;


    private List<XssWhiteUrl> whiteUrls;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("xss攻击验证:start");
        boolean xss = globalConfig.isXss();

        if(xss) {
            // 白名单路由判断
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();
            List<String> whiteUrls = globalConfig.getWhiteUrls();
            if(whiteUrls.contains(path)){
                log.info("xss攻击验证:true,白名单");
                return chain.filter(exchange);
            }


            ServerHttpRequest req = exchange.getRequest();
            String method = req.getMethodValue();

            ServerHttpRequest builder = req.mutate().build();
            if (HttpMethod.GET.matches(method)) {
                builder = change(exchange, builder);
            } else if (HttpMethod.POST.matches(method)) {
                builder = change(exchange, builder);
            }
            exchange = exchange.mutate().request(builder).build();
            log.info("xss攻击验证:true");
        }else {
            log.info("xss攻击验证:true,验证已关闭");
        }

        return chain.filter(exchange);
    }

    /**
     * 获取请求参数等信息进行过滤处理
     *
     * @param exchange          请求
     * @param serverHttpRequest 请求
     * @return 处理结束的参数
     */
    private ServerHttpRequest change(ServerWebExchange exchange, ServerHttpRequest serverHttpRequest) {
        // 获取原参数
        URI uri = serverHttpRequest.getURI();
        String originalQuery = uri.getRawQuery();
        // 更改参数

        if(StringUtils.isNoneBlank(originalQuery)) {
            // 执行XSS清理
            log.info("{} - XSS清理,处理前参数：{}", uri.getPath(), originalQuery);

            originalQuery = XssUtils.INSTANCE.cleanXss(originalQuery);
            log.info("{} - XSS清理,处理后参数：{}", uri.getPath(), originalQuery);
        }
        // 替换查询参数
        URI newUri = UriComponentsBuilder.fromUri(uri)
                .replaceQuery(originalQuery)
                .build(true)
                .toUri();

        return exchange.getRequest().mutate().uri(newUri).build();
    }


    @Override
    public int getOrder() {
        return -180;
    }
}
