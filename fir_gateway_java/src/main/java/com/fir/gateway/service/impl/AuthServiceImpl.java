package com.fir.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fir.gateway.config.GatewayConfig;
import com.fir.gateway.config.exception.CustomException;
import com.fir.gateway.config.result.AjaxStatus;
import com.fir.gateway.entity.ConnectDTO;
import com.fir.gateway.entity.LoginResultDTO;
import com.fir.gateway.entity.RsaKeyDTO;
import com.fir.gateway.entity.User;
import com.fir.gateway.service.IAuthService;
import com.fir.gateway.singleton.Singleton;
import com.fir.gateway.uttls.AESUtils;
import com.fir.gateway.uttls.MD5Utils;
import com.fir.gateway.uttls.RSAUtils;
import com.fir.gateway.uttls.SaltedHashUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * @author fir
 * @date 2023/4/23 17:03
 */
@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {


    /**
     * 用户统一登录
     *
     * @param username 账号
     * @param password 密码
     * @return 登录信息
     */
    @Override
    public LoginResultDTO login(String username, String password) {
        LoginResultDTO loginResultDTO = new LoginResultDTO();

        // 账户信息登录
        HashMap<Integer, Object> map = authenticate(username, password);

        int state = Integer.parseInt(map.get(0).toString());
        if (state == 1) {
            // 生成token
            String token = generateToken(username);
            loginResultDTO.setToken(token);
        } else {
            log.error("登录失败了:" + map.get(1).toString());
            throw new CustomException(AjaxStatus.NULL_LOGIN_DATA);
        }

        return loginResultDTO;
    }


    /**
     * 获取公钥
     *
     * @return 公钥
     */
    @Override
    public String getPublicKey() {

        // 生成本此次连接的公钥与私钥对存储，将公钥发送到前端作为加密通信签名的方式
        Map<String, String> map = RSAUtils.generateKey();

        String publicKey = map.get(RSAUtils.PUBLIC_KEY);
        String privateKey = map.get(RSAUtils.PRIVATE_KEY);

        // 将公钥取md5后，作为key存入redis中
        String publicKeyMd5 = MD5Utils.generateMd5ForString(publicKey);
        RsaKeyDTO rsaKeyDTO = new RsaKeyDTO();
        rsaKeyDTO.setPublicKey(publicKey);
        rsaKeyDTO.setPrivateKey(privateKey);

        Object obj = JSONObject.toJSON(rsaKeyDTO);
        Singleton.getSingleton().redisTemplate.opsForValue().set(publicKeyMd5, obj, GatewayConfig.RSA_NUM, GatewayConfig.RSA_UNIT);

        return publicKey;
    }


    /**
     * 判断当前用户是否登陆成功
     *
     * @param username 用户ID
     * @param password 用户密码
     * @return map key中存储 0：登陆成功/失败，1：错误描述，2：用户实体
     */
    private HashMap<Integer, Object> authenticate(String username, String password) {
        User user = null;
        String mag = "";
        HashMap<Integer, Object> map = new HashMap<>(2);
        int key = 0;

        // 判断用户是否存在，用户密码是否正确（在其他方式下，可通过数据库，其他系统判断用户是否登录成功）
        Integer isPass = checkUsername(username);
        if (isPass > 0) {
            user = checkLogin(username, password);
            if (user != null) {
                key = 1;
            } else {
                mag = "用户密码不正确";
            }
        } else {
            mag = "用户不存在";
        }
        map.put(0, key);
        map.put(1, mag);

        // 生成用户信息实体（在数据库连接下， 查询出用户基本信息）
        if (key == 1) {
            map.put(2, user);
        }

        return map;
    }


    private String generateToken(String username) {

        // 生成 Token (可用多种方式例如jwt,此处不额外集成)
        String token = "Bearer " + username + "token";

        String generateKeyAes = AESUtils.generateKeyAES();
        ConnectDTO connectDTO = ConnectDTO.builder()
                .secretKey(generateKeyAes)
                .build();

        Object o = JSONObject.toJSON(connectDTO);
        Singleton.getSingleton().redisTemplate.opsForValue().set(token, o, GatewayConfig.TIME_NUM, GatewayConfig.TIME_UNIT);
        return token;
    }


    // 一下是临时使用的方法，替代数据库功能

    private final static String USERNAME = "fir";
    private final static String PASSWORD = "123";


    /**
     * 如果当前用户存在返回1，否则返回2
     *
     * @param username 登录账号
     * @return 存在返回1，否则返回2
     */
    private Integer checkUsername(String username) {
        int num = 0;

        if (AuthServiceImpl.USERNAME.equals(username)) {
            num = 1;
        }

        return num;
    }


    /**
     * 如果当前用户存在返回1，否则返回2
     *
     * @param username 登录账号
     * @return 存在返回1，否则返回2
     */
    private User checkLogin(String username, String password) {
        User user = null;

        if (AuthServiceImpl.USERNAME.equals(username) && AuthServiceImpl.PASSWORD.equals(password)) {
            user = User.builder()
                    .userid("1")
                    .deptName("测试职位")
                    .name(username)
                    .orgName("测试公司")
                    .build();

        }

        return user;
    }


    /**
     * 获取通信加密信息
     *
     * @param publicKeyMd5       RSA公钥
     * @param clientPublicKey 客户端公钥
     * @return 加密信息
     */
    @Override
    public ConnectDTO info(String publicKeyMd5, String clientPublicKey) {
        ConnectDTO connectDTO;
        // 将公钥取md5后，作为key存入redis中
        JSONObject jsonObject = (JSONObject) Singleton.getSingleton().redisTemplate.opsForValue().get(publicKeyMd5);
        if (jsonObject != null) {
            RsaKeyDTO rsaKeyDTO = jsonObject.toJavaObject(RsaKeyDTO.class);
            String publicKey = rsaKeyDTO.getPublicKey();
            String privateKey = rsaKeyDTO.getPrivateKey();
            clientPublicKey = RSAUtils.decryptSection(clientPublicKey, privateKey);

            String secretKey = AESUtils.generateKeyAES();
            String sessionId = generateSessionId();
            String salt = SaltedHashUtils.generateSalt();
            connectDTO = ConnectDTO.builder()
                    .secretKey(secretKey)
                    .sessionId(sessionId)
                    .salt(salt)
                    .privateKey(privateKey)
                    .clientPublicKey(clientPublicKey)
                    .build();
            Singleton.getSingleton().redisTemplate.opsForValue()
                    .set(sessionId, JSONObject.toJSON(connectDTO), GatewayConfig.TIME_NUM, GatewayConfig.TIME_UNIT);
            connectDTO = ConnectDTO.builder()
                    .secretKey(secretKey)
                    .sessionId(sessionId)
                    .salt(salt)
                    .publicKey(publicKey)
                    .clientPublicKey(clientPublicKey)
                    .build();
            return connectDTO;
        } else {
            throw new CustomException(AjaxStatus.FAILED_COMMUNICATION);
        }
    }

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateSessionId() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }


}
