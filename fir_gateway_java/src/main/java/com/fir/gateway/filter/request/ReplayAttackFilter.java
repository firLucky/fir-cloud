package com.fir.gateway.filter.request;

import com.alibaba.fastjson.JSONObject;
import com.fir.gateway.config.GlobalConfig;
import com.fir.gateway.config.result.AjaxResult;
import com.fir.gateway.config.result.AjaxStatus;
import com.fir.gateway.entity.ReplayAttackInfo;
import com.fir.gateway.uttls.SignatureUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * 防重放攻击-请求拦截器
 *
 * @author fir
 */
@Component
@Slf4j
public class ReplayAttackFilter implements GlobalFilter, Ordered {


    /**
     * 网关参数配置
     */
    @Resource
    private GlobalConfig globalConfig;


    /**
     * 5 * 60 * 1000 表示5分钟的间隔，用于防重放的间隔之中
     */
    private static final long TIMESTAMP_VALID_TIME = 5 * 60 * 1000;

    private final Set<String> usedNonceSet = Collections.synchronizedSet(new HashSet<>());


    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 * * * * *")
    public void clearUsedNonceSet() {
        usedNonceSet.clear();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("防重放攻击验证:start");
        boolean replay = globalConfig.isReplay();
        if(replay) {
            // 白名单路由判断
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();
            List<String> whiteUrls = globalConfig.getWhiteUrls();
            if(whiteUrls.contains(path)){
                log.info("防重放攻击验证:true,白名单");
                return chain.filter(exchange);
            }

            // 从请求头中获取Nonce和Timestamp
            String nonce = exchange.getRequest().getHeaders().getFirst("n");
            String timestamp = exchange.getRequest().getHeaders().getFirst("t");
            String s = exchange.getRequest().getHeaders().getFirst("s");
            // 验证Nonce和Timestamp是否合法
            boolean validateKey = validateNonceAndTimestamp(nonce, timestamp, s);
            if (validateKey) {
                // 如果合法，则放行请求
                log.info("防重放攻击验证:true");
            } else {
                log.info("防重放攻击验证:false");
                // 如果不合法，则返回错误响应
                ServerHttpResponse response = exchange.getResponse();
                // 自定义返回体描述
                AjaxResult error = AjaxResult.error(AjaxStatus.ANTI_REPLAY_VERIFY_FAILED);
                String resData = JSONObject.toJSONString(error);

                byte[] responseBody = resData.getBytes(StandardCharsets.UTF_8);

                response.getHeaders().setContentLength(responseBody.length);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBody)));

            }
        }else {
            log.info("防重放攻击验证:true,验证已关闭");
        }

        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        // 设置过滤器的优先级
        return -200;
    }


    /**
     * 根据请求时间戳，与请求签名密钥，判断请求是否是合法的
     *
     * @param nonce 请求签名密钥
     * @param timestamp 请求时间戳
     * @return 是否合法
     */
    private boolean validateNonceAndTimestamp(String nonce, String timestamp, String s) {
        // 判断Nonce和Timestamp是否为空
        if (nonce == null || timestamp == null) {
            log.error("防重放攻击验证:非法请求,无请求时间戳");
            return false;
        }

        // 验证Nonce是否已经使用过
        if (usedNonceSet.contains(nonce)) {
            log.error("防重放攻击验证:请求签名已使用");
            return false;
        } else {
            // 将本次的请求签名记录，用于下次判断是否有相同的请求签名
            usedNonceSet.add(nonce);
        }

        // 判断事件戳与数据签名是否相同
        String str = SignatureUtils.decryptSignatureBase64(nonce);
        ReplayAttackInfo replayAttackInfo = JSONObject.parseObject(str, ReplayAttackInfo.class);
        String t = replayAttackInfo != null ? replayAttackInfo.getT() : null;
        if (StringUtils.isBlank(t) || !timestamp.equals(t)){
            log.error("防重放攻击验证:非法请求，请求时间非法");
            return false;
        }

        // 验证Timestamp是否在合理时间范围内
        long timeStampValue;
        try {
            timeStampValue = Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            log.error("防重放攻击验证:非法请求，请求时间错误");
            return false;
        }

        long currentTime = System.currentTimeMillis();

        // 判断请求是是否是在n分钟之前请求的
        boolean a = timeStampValue >= currentTime - TIMESTAMP_VALID_TIME;
        // 判断请求是是否是在n分钟后前请求的
        boolean b = timeStampValue <= currentTime + TIMESTAMP_VALID_TIME;

        boolean c = a && b;
        if (!c){
            log.info("防重放攻击验证:请求过期");
        }
        return c;
    }
}
