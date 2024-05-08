package com.fir.nacos.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.HashMap;


/**
 * @author fir
 */
@Slf4j
@RestController
@RefreshScope
public class DemoController {

 
    /** 使用@value注解取值，它能取到值，但没有自动更新的功能 */
    @Value(value = "${aa}")
    private String aa;

    @Value(value = "${name}")
    private String name;


    @RequestMapping("/getValue")
    public HashMap<String, Object> getValue(String msg) {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeSleep = System.currentTimeMillis();
        String dateTime = sdf.format(timeSleep);

        String content = aa + "我是" + name + " 收到客户端消息:" + msg;

        map.put("value", aa);
        map.put("dateTime", dateTime);
        map.put("content", content);
        map.put("intValue", 120);
        map.put("doubleValue", 12.012);

        log.info("{} {} {}", name, timeSleep, aa);
        result.put("code", 200);
        result.put("msg", "操作成功");
        result.put("data", map);
        return result;
    }


    @RequestMapping("/get/value/body")
    public HashMap<String, Object> getValueBody(@RequestBody String msg) {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeSleep = System.currentTimeMillis();
        String dateTime = sdf.format(timeSleep);

        String content = aa + "我是" + name + " 收到客户端消息:" + msg;

        map.put("value", aa);
        map.put("dateTime", dateTime);
        map.put("content", content);
        map.put("intValue", 120);
        map.put("doubleValue", 12.012);

        log.info("{} {} {}", name, timeSleep, aa);
        result.put("code", 200);
        result.put("msg", "操作成功");
        result.put("data", map);
        return result;
    }
}