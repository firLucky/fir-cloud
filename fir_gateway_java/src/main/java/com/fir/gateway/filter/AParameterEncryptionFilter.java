//package com.fir.gatteway.filter;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpMethod;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Slf4j
//@Component
//public class AParameterEncryptionFilter implements GlobalFilter, Ordered {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // 获取请求方法
//        HttpMethod method = exchange.getRequest().getMethod();
//
//        // 判断请求是否来自网关自身
//        if (isInternalGatewayRequest(exchange)) {
//            // 网关自身的请求处理逻辑
//            // ...
//            return Mono.empty(); // 或者返回自定义的响应
//        }
//
//        // 其他请求的处理逻辑
//        // ...
//
//        return chain.filter(exchange);
//    }
//
//    // 判断请求是否来自网关自身
//    private boolean isInternalGatewayRequest(ServerWebExchange exchange) {
//        // 根据实际情况判断请求是否来自网关自身
//        // 可以根据请求的URI、请求头、请求参数等进行判断
//        // 返回 true 表示来自网关自身的请求，需要进行特殊处理
//        // 返回 false 表示非网关自身的请求，按照正常逻辑处理
//        return false;
//    }
//
//    @Override
//    public int getOrder() {
//        return 2; // 优先级最高
//    }
//}
