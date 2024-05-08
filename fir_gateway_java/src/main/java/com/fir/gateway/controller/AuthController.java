package com.fir.gateway.controller;

import com.fir.gateway.config.result.AjaxResult;
import com.fir.gateway.config.result.AjaxStatus;
import com.fir.gateway.entity.ConnectDTO;
import com.fir.gateway.entity.LoginResultDTO;
import com.fir.gateway.service.IAuthService;
import com.fir.gateway.uttls.RSAUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


/**
 * 系统登录验证
 *
 * @author dpe
 * @date 2022/8/4 22:58
 */
@Api(tags = "系统登录接口")
@Slf4j
@RestController
@RefreshScope
public class AuthController {

    /**
     * 系统登录验证 接口层
     */
    @Resource
    private IAuthService iAuthService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号"),
            @ApiImplicitParam(name = "password", value = "密码"),
    })
    @ApiOperation("用户统一登陆")
    @RequestMapping("/auth/login")
    public AjaxResult login(String username, String password) {
        LoginResultDTO loginResultDTO;
        if(StringUtils.isNoneBlank(username) && StringUtils.isNoneBlank(password)){
            loginResultDTO = iAuthService.login(username, password);
        }else {
            return AjaxResult.error(AjaxStatus.NULL_LOGIN_DATA);
        }
        return AjaxResult.success(loginResultDTO);
    }


    @ApiOperation("客户端与服务端建立连接")
    @RequestMapping("/k")
    public AjaxResult info() {
        String publicKey = iAuthService.getPublicKey();
        return AjaxResult.success(publicKey);
    }


    @ApiOperation("客户端与服务端建立连接")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "k", value = "公钥字符串"),
            @ApiImplicitParam(name = "k", value = "客户端公钥字符串"),
    })
    @RequestMapping("/cn")
    public AjaxResult connect(String k, String ck) {
        ConnectDTO connectDTO = iAuthService.info(k, ck);
        String content = RSAUtils.encryptSection(connectDTO, connectDTO.getClientPublicKey());
        return AjaxResult.success(content);
    }
}
