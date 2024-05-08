package com.original.redis.controller;

import com.original.redis.config.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@Api(tags = "测试 消息接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class MsgController {


    @ApiOperation("发送消息")
    @GetMapping("/send")
    @ApiImplicitParam(name = "msg", value = "测试的msg，会直接返回", dataTypeClass = String.class, required = true)
    public Result send(String msg) {
        return Result.success("发送成功：" + msg);
    }


    @ApiOperation("接受消息")
    @GetMapping("/take/in")
    @ApiImplicitParam(name = "msg", value = "测试的msg，会直接返回", dataTypeClass = String.class, required = true)
    public Result takeIn(String msg) {
        return Result.success("接受成功：" + msg);
    }
}
