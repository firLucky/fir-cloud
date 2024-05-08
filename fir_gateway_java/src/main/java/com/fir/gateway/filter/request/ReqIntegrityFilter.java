package com.fir.gateway.filter.request;

import com.alibaba.fastjson.JSONObject;
import com.fir.gateway.config.GlobalConfig;
import com.fir.gateway.config.exception.CustomException;
import com.fir.gateway.config.result.AjaxResult;
import com.fir.gateway.config.result.AjaxStatus;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;


/**
 * 完整性检验-请求拦截器
 *
 * @author fir
 */
@Slf4j
@Component
public class ReqIntegrityFilter implements GlobalFilter, Ordered {


    /**
     * 网关参数配置
     */
    @Resource
    private GlobalConfig globalConfig;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("完整性校验:start");
        boolean reqIntegrity = globalConfig.isReqIntegrity();

        if(reqIntegrity) {
            ServerHttpRequest request = exchange.getRequest();

            // 获取请求的相关信息
            HttpMethod method = request.getMethod();
            String path = request.getPath().toString();

            // 白名单路由判断
            List<String> whiteUrls = globalConfig.getWhiteUrls();
            if(whiteUrls.contains(path)){
                log.info("完整性校验:true,白名单");
                return chain.filter(exchange);
            }
            MultiValueMap<String, String> query = request.getQueryParams();
            String queryJson = JSONObject.toJSONString(query);
            // request.getQueryParams() 获取到的参数使得前后端完整性参数不一致，故暂时使用去除中括号[]的方式使得前后端统一
            queryJson = queryJson.replaceAll("\\[|\\]", "");
            queryJson = queryJson.replaceAll("\\\"", "");


            // 计算请求的完整性校验值
            String calculatedChecksum;
            if (method != null) {
                calculatedChecksum = calculateChecksum(method, path, queryJson);
            } else {
                log.error("完整性校验失败：请求类型获取失败，请求类型为空");
                throw new CustomException(AjaxStatus.INTEGRITY_VERIFY_FAILED);
            }

            // 获取请求中携带的校验值
            String providedChecksum = request.getHeaders().getFirst("c");

            // 比较校验值
            if (calculatedChecksum != null && calculatedChecksum.equals(providedChecksum)) {
                log.info("完整性校验:true");
                // 校验通过，继续处理请求
                return chain.filter(exchange);
            } else {
                log.info("完整性校验:false");
                // 校验失败，拒绝请求

                // 如果不合法，则返回错误响应
                ServerHttpResponse response = exchange.getResponse();
                // 自定义返回体描述
                AjaxResult error = AjaxResult.error(AjaxStatus.INTEGRITY_VERIFY_FAILED);
                String resData = JSONObject.toJSONString(error);

                byte[] responseBody = resData.getBytes(StandardCharsets.UTF_8);

                response.getHeaders().setContentLength(responseBody.length);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBody)));
            }
        }else {
            log.info("完整性校验:true,验证已关闭");
        }


        return chain.filter(exchange);
    }

    @SneakyThrows
    private String calculateChecksum(HttpMethod method, String path, String query) {
        // TODO: 根据具体需求计算请求的完整性校验值，可以使用哈希算法或消息认证码

        // 示例：使用HMAC-SHA256计算校验值
        String data = method.toString() + path + query;
//        byte[] secretKeyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
//        SecretKeySpec keySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
//        Mac mac = Mac.getInstance("HmacSHA256");
//        mac.init(keySpec);
//        byte[] checksumBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
//        // 将解密结果转为字符串
//        return Base64.getEncoder().encodeToString(checksumBytes);

        byte[] bytes = data.getBytes();

        //Base64 加密

        return Base64.getEncoder().encodeToString(bytes);
    }

    @Override
    public int getOrder() {
        // 设置过滤器的优先级
        return -190;
    }
}
