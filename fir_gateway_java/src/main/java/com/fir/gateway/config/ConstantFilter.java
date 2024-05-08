package com.fir.gateway.config;

/**
 * @calssName ConstanFilter
 * @Description gateway中的参量
 * @Author dpe
 * @date 2023/4/10
 */
public class ConstantFilter {

    // 设置到 exchange.getAttributes()中的key
    public final static String CACHED_REQUEST_BODY_OBJECT_KEY = "CACHED_REQUEST_BODY_OBJECT_KEY";

    // 设置到 exchange.getAttributes()中的key
    public final static String BG_DEBUG_KEY = "BG_DEBUG";

    // 请求参数，返回参数 加密
    public final static String REQ_RES_ENCRYPT = "0";

    // 请求参出，返回结果 无需加密
    public final static String REQ_RES_NALMORE = "1";
}

