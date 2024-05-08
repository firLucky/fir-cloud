package com.fir.gateway.filter.request;

import com.alibaba.fastjson.JSONObject;
import com.fir.gateway.config.GlobalConfig;
import com.fir.gateway.config.exception.CustomException;
import com.fir.gateway.config.result.AjaxStatus;
import com.fir.gateway.entity.ConnectDTO;
import com.fir.gateway.singleton.Singleton;
import com.fir.gateway.uttls.AESUtils;
import com.fir.gateway.uttls.RSAUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import javax.annotation.Resource;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * 请求整理解密-请求拦截器
 *
 * @author fir
 */
@Slf4j
@Component
public class DecryptionFilter implements Ordered, GlobalFilter {


    /**
     * 网关参数配置
     */
    @Resource
    private GlobalConfig globalConfig;


    @Override
    public int getOrder() {
        return -280;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("整体解密:start");
        ServerHttpRequest req = exchange.getRequest();
        String method = req.getMethodValue();

        boolean rsa = globalConfig.isRsa();
        boolean aes = globalConfig.isAes();
        if (rsa || aes) {
            // 白名单路由判断
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();
            List<String> whiteUrls = globalConfig.getWhiteUrls();

            if(!whiteUrls.contains(path)){
                ServerHttpRequest builder = req.mutate().build();
                if (HttpMethod.GET.matches(method)) {
                    log.info("当前请求参数为: {}", req.getQueryParams());
                    builder = changeGet(exchange, builder);
                } else if (HttpMethod.POST.matches(method)) {
                    log.info("当前请求参数为: {}", req.getQueryParams());
                    builder = changeGet(exchange, builder);
                }
                exchange = exchange.mutate().request(builder).build();
                log.info("整体解密:true");
            }else {
                log.info("整体解密:true,白名单");
            }
        }else {
            log.info("整体解密:true,验证已关闭");
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
    @SneakyThrows
    private ServerHttpRequest changeGet(ServerWebExchange exchange, ServerHttpRequest serverHttpRequest) {
        String session = exchange.getRequest().getHeaders().getFirst("s");

        if (session == null) {
            throw new CustomException(AjaxStatus.SESSION_INVALID);
        }

        JSONObject jsonObject = (JSONObject) Singleton.getSingleton().redisTemplate.opsForValue().get(session);
        if (jsonObject == null) {
            throw new CustomException(AjaxStatus.SESSION_EXPIRE);
        }
        ConnectDTO connectDTO = jsonObject.toJavaObject(ConnectDTO.class);
        String privateKey = connectDTO.getPrivateKey();
        String secretKey = connectDTO.getSecretKey();


        // 获取原参数
        URI uri = serverHttpRequest.getURI();
        String originalQuery = uri.getRawQuery();
        String decodedQuery = null;
        if(StringUtils.isNotBlank(originalQuery)){
            decodedQuery = URLDecoder.decode(originalQuery, "UTF-8");
        }

        // 更改参数
        MultiValueMap<String, String> newQueryParams = new LinkedMultiValueMap<>();
        if (StringUtils.isNotBlank(originalQuery) && org.springframework.util.StringUtils.hasText(decodedQuery)) {
            // 修改请求参数，String[] array只能处理前端特定加密 {data:加密内容的形式}, 传递到后端，会变更为 data=加密内容。
            // 除此以外的所有方式不能通过本方法进行解密
            String[] array = decodedQuery.split("=");
            if (array.length > 1) {
                decodedQuery = array[1];

                if (decodedQuery != null) {
                    boolean rsa = globalConfig.isRsa();
                    boolean aes = globalConfig.isAes();

                    if (rsa) {
                        // 对数据进行非对称解密
                        originalQuery = RSAUtils.decryptSection(decodedQuery, privateKey);
                    }
                    if (aes) {
                        // 对数据进行对称解密
                        originalQuery = AESUtils.decrypt(originalQuery, secretKey);
                    }
                }

                Map<String, Object> dataMap = JSONObject.parseObject(originalQuery, Map.class);
                if (dataMap != null) {
                    Set<String> strings = dataMap.keySet();
                    for (String key : strings) {
                        String encodedString = URLEncoder.encode(dataMap.get(key).toString(), StandardCharsets.UTF_8.toString());
                        newQueryParams.add(key, encodedString);
                    }
                }
            }

        }

        // 替换查询参数
        URI newUri = UriComponentsBuilder.fromUri(uri)
                .query(null)
                .queryParams(newQueryParams)
                .build(true)
                .toUri();

        ServerHttpRequest request = exchange.getRequest().mutate().uri(newUri).build();
        // 将解密后的参数重新设置到请求中

        uri = request.getURI();
        log.info("更改后的当前请求参数为: {}", uri.getRawQuery());
        return request;
    }
}
