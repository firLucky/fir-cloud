package com.fir.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dpe
 */
@RestController
public class FallBackController {


    /**
     * GET请求方式的请求路由转发出错时，会触发服务降级, 提示警告
     */
    @RequestMapping("/fallback")
    public String fallback() {
        return "系统繁忙，请稍后再试！";
    }


}