package com.fir.gateway.controller;

import com.fir.gateway.config.result.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;


/**
 * @author dpe
 * @date 2022/8/4 22:58
 */
@Api(tags = "demo接口")
@Slf4j
@RestController
@RefreshScope
public class DemoController {


    /**使用@value注解取值，它能取到值，但没有自动更新的功能*/
    @Value(value = "${aa}")
    private String aa;

    @RequestMapping("/get/value")
    public AjaxResult getValue(String msg) {
        HashMap<String, Object> map = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeSleep = System.currentTimeMillis();
        String dateTime = sdf.format(timeSleep);

        String content = aa + "我是 网关 收到客户端消息:" + msg;

        map.put("value", aa);
        map.put("dateTime", dateTime);
        map.put("content", content);
        map.put("intValue", 120);
        map.put("doubleValue", 12.012);

        log.info("{} {}", dateTime, aa);
        return AjaxResult.success(map);
    }
}