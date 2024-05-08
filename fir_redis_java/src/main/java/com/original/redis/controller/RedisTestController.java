package com.original.redis.controller;

import com.original.redis.config.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


@Slf4j
@Api(tags = "redis 测试接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/redis")
public class RedisTestController {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    @ApiOperation("set请求")
    @GetMapping("/send")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "k", value = "键", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "v", value = "值", dataTypeClass = String.class, required = true)
    })
    public Result send(String k, String v) {
        redisTemplate.opsForValue().set(k, v);
        return Result.success();
    }

    @ApiOperation("get请求")
    @GetMapping("/take/in")
    @ApiImplicitParam(name = "k", value = "键", dataTypeClass = String.class, required = true)
    public Result takeIn(String k) {
        return Result.success(redisTemplate.opsForValue().get(k));
    }

}
