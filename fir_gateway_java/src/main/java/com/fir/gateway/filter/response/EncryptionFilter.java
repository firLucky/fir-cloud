package com.fir.gateway.filter.response;

import com.alibaba.fastjson.JSONObject;
import com.fir.gateway.config.GlobalConfig;
import com.fir.gateway.config.exception.CustomException;
import com.fir.gateway.config.result.AjaxStatus;
import com.fir.gateway.entity.ConnectDTO;
import com.fir.gateway.singleton.Singleton;
import com.fir.gateway.uttls.AESUtils;
import com.fir.gateway.uttls.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * @author fir
 */
@Slf4j
@Component
public class EncryptionFilter implements GlobalFilter, Ordered {


    /**
     * 网关参数配置
     */
    @Resource
    private GlobalConfig globalConfig;


    @Override
    public int getOrder() {
        return -1;
    }


    /**
     * 返回加密
     * 这里@NonNull注解表示该参数不能为null，这可以帮助编译器和静态分析工具检测出潜在的空指针异常，从而提高代码的健壮性和可维护性。
     * 具体来说，它告诉编译器该方法的实现不会接受null值作为该参数的输入。如果传入null值，编译器将会在编译时发出警告。
     *
     * @param exchange 服务器Web交换对象 ，简单来说就是一次HTTP请求和响应的上下文对象。
     * @param chain    网关过滤器链 许开发者在路由之前或之后自定义过滤逻辑
     * @return 响应请求
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        ServerHttpRequest originalRequest = exchange.getRequest();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();


        boolean rsa = globalConfig.isRsa();
        boolean aes = globalConfig.isAes();
        if (rsa || aes) {

            // 白名单路由判断
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();
            List<String> whiteUrls = globalConfig.getWhiteUrls();
            if (whiteUrls.contains(path)) {
                log.info("响应整体加密:true,白名单");
                return chain.filter(exchange);
            }

            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                @Override
                @NonNull
                public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                    log.info("响应整体加密:start");
                    if (body instanceof Flux) {
                        // Mono 类型的处理逻辑(目前此类是被网关拦截之后的返回体)
                        Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                        return super.writeWith(fluxBody.map(dataBuffer -> {
                            byte[] responseBody = encrypt(dataBuffer, originalRequest, originalResponse);
                            log.info("响应整体加密:true");
                            return bufferFactory.wrap(responseBody);
                        }));
                    } else if (body instanceof Mono) {
                        // Mono 类型的处理逻辑(目前此类是被网关拦截之后的返回体)
                        Mono<? extends DataBuffer> monoBody = (Mono<? extends DataBuffer>) body;
                        return super.writeWith(monoBody.flatMap(dataBuffer -> {
                            byte[] responseBody = encrypt(dataBuffer, originalRequest, originalResponse);
                            log.info("响应整体加密:true");
                            return Mono.just(bufferFactory.wrap(responseBody));
                        }));
                    }
                    return super.writeWith(body);
                }
            };

            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        }


        return chain.filter(exchange);
    }


    /**
     * 数据加密
     *
     * @param dataBuffer       返回体字节数据
     * @param originalRequest  http请求体
     * @param originalResponse http响应体
     * @return 加密返回体
     */
    private byte[] encrypt(DataBuffer dataBuffer, ServerHttpRequest originalRequest,
                           ServerHttpResponse originalResponse) {
        boolean rsa = globalConfig.isRsa();
        boolean aes = globalConfig.isAes();

        byte[] contentByte = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(contentByte);
        //释放掉内存
        DataBufferUtils.release(dataBuffer);
        String content = new String(contentByte, StandardCharsets.UTF_8);
        // s就是response的值，想修改、查看就随意而为了

        // 请求头中获得会话id，处理得到加密密钥
        String sessionId = originalRequest.getHeaders().getFirst("s");
        if (StringUtils.isBlank(sessionId)) {
            throw new CustomException(AjaxStatus.SESSION_INVALID);
        }
        JSONObject jsonObject = (JSONObject) Singleton.getSingleton().redisTemplate.opsForValue().get(sessionId);
        if (jsonObject == null) {
            throw new CustomException(AjaxStatus.SESSION_EXPIRE);
        }
        ConnectDTO connectDTO = jsonObject.toJavaObject(ConnectDTO.class);
        String key = connectDTO.getSecretKey();
        String clientPublicKey = connectDTO.getClientPublicKey();

        if (aes) {
            // 对称加密返回体数据
            content = AESUtils.encrypt(content, key);

        }
        if (rsa) {
            // 非对称加密返回体数据
            content = RSAUtils.encryptSection(content, clientPublicKey);
        }

        byte[] responseBody = new byte[0];
        if (content != null) {
            responseBody = content.getBytes(StandardCharsets.UTF_8);
        }
        // 修改之后必须重置返回体长度
        originalResponse.getHeaders().setContentLength(responseBody.length);
        log.info("响应整体解密:true");
        return responseBody;
    }
}