package com.fir.gateway.service;


import com.fir.gateway.entity.ConnectDTO;
import com.fir.gateway.entity.LoginResultDTO;


/**
 * @author fir
 * @date 2023/4/23 17:03
 */
public interface IAuthService {


    /**
     * 用户统一登录
     *
     * @param username 账号
     * @param password 密码
     * @return 登录信息
     */
    LoginResultDTO login(String username, String password);


    /**
     * 获取公钥
     *
     * @return 公钥
     */
    String getPublicKey();


    /**
     * 获取通信加密信息
     *
     * @param publicKeyMd5    RSA公钥md5取值
     * @param clientPublicKey 客户端公钥
     * @return 加密信息
     */
    ConnectDTO info(String publicKeyMd5, String clientPublicKey);
}


