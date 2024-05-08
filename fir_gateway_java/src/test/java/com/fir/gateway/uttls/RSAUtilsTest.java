package com.fir.gateway.uttls;

import cn.hutool.crypto.SecureUtil;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;


/**
 * @author fir
 * @date 2023/7/14 13:21
 */
class RSAUtilsTest {


    @Test
    void a() {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCB5N0xQn0Bfv578aDg4U2AOfudNygA69/xvj1i\n" +
                "Ei0URCruqY4XXAaNf6+Wlm16wzvKCcxl/wACnD8L1bFRGfLIiyrmkg0QynsyOZloO4o+BmNnnKMP\n" +
                "fdpleaEAiGqAH16TO1uMH10FznLAGWQ5/chPn5gLgnz7iQbUT4vehw9QWQIDAQAB";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIHk3TFCfQF+/nvxoODhTYA5+503\n" +
                "KADr3/G+PWISLRREKu6pjhdcBo1/r5aWbXrDO8oJzGX/AAKcPwvVsVEZ8siLKuaSDRDKezI5mWg7\n" +
                "ij4GY2ecow992mV5oQCIaoAfXpM7W4wfXQXOcsAZZDn9yE+fmAuCfPuJBtRPi96HD1BZAgMBAAEC\n" +
                "gYAKFhx687/DOvpF45hrffNHrrj0F1Fa6PYFzpDzqZeoiDaRO8dV1waHPFAFMH8l1j0xTht6HBgD\n" +
                "G+DlaV2Qf6RTg1xfO7qphIGlcI8bGgrmIPfxgmYKiTxBn1cVE043i2vMO/wLxKcaiGPpihrkfQFc\n" +
                "2I08gr3SnGRKyBNHeUq/EQJBAN80liE9PqXTaWSV+A2QsOhGccJQN2pFzeqef+oBUsWHkhAwLT5l\n" +
                "xpYP49MwnucLoPEhHyAcJr7WLnMGU9bR5SUCQQCU+o9quu6bapEIsvfLE3MRB8ISfHMALF47UVvt\n" +
                "X9P6WDQ9HTJ5mKo+WZuhyNRkEAtlq3Au80AHQ8FCSOWpIsolAj9Gn1zhJ3+q+DlgmhLiN+XBmIhl\n" +
                "8vuVpICCu6O9Zq3J0htlA9lM5ObwlBaBu+CLOiKyKMiwYDfiKeKIgngEgGUCQGxfYJipZw2TlfGY\n" +
                "Dv2hJmFLHXhJI3cKz3mjE5Y30YIZS9bxOFiQH7e/g6FK8IXhzgilj7P3q6odNaa8VKSW9CkCQQDS\n" +
                "GxaEW5ryqpVJ0lZwL59Ben+XsXWQwWYyYrko9Yg2EnMC5XkNE74l8w69NMyrQUfQO6tPqHkwtXlV\n" +
                "lnxbmnbo";

        System.out.println("读取的公钥是：" + publicKey);
        System.out.println("读取的私钥是：" + privateKey);
        //公钥加密
        String encrypt = RSAUtils.encrypt("加密的内容",  publicKey);
        System.out.println("获取的加密串：" + encrypt);
        //私钥解密
        String decrypt = RSAUtils.decrypt(encrypt,  privateKey);
        System.out.println("解密之后的数据：" + decrypt);



    }


    @Test
    void b() {
        KeyPair pair = SecureUtil.generateKeyPair("RSA");
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();


        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        System.out.println("读取的公钥是：" + publicKeyStr);
        System.out.println("读取的私钥是：" + privateKeyStr);
        //公钥加密
        String encrypt = RSAUtils.encrypt("加密的内容",  publicKeyStr);
        System.out.println("获取的加密串：" + encrypt);
        //私钥解密
        String decrypt = RSAUtils.decrypt(encrypt,  privateKeyStr);
        System.out.println("解密之后的数据：" + decrypt);
    }


    @Test
    void c() {
        Map<String, String> map = RSAUtils.generateKey();
        String publicKey = map.get(RSAUtils.PUBLIC_KEY);
        String privateKey = map.get(RSAUtils.PRIVATE_KEY);
        System.out.println("读取的公钥是：" + publicKey);
        System.out.println("读取的私钥是：" + privateKey);
        //公钥加密
        String encrypt = RSAUtils.encrypt("加密的内容", publicKey);
        System.out.println("获取的加密串：" + encrypt);
        //私钥解密
        String decrypt = RSAUtils.decrypt(encrypt, privateKey);
        System.out.println("解密之后的数据：" + decrypt);
    }


    @Test
    void d(){
        String json = "{\"clientPublicKey\":\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDfYF/Q7d/N/0SRaAC+7AS2dfDTL7U+piCj42xgUNmM60lemQVpOGn/ujQjIhfa/hhFckx6r7PHFYPwmypvlpKJ5Ddw8+LMt1AgHBXTPTkbyfjE3CBHvzeoPkgd4Tb0FoQwJ87SKl+OcOTXponm6+MCDnswenIeFw2KZq+tQk4WHQIDAQAB\",\"privateKey\":\"MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALH9/mAuBxTE2Cy5ne73yVynOGrQHCA34bk+kj7ZDHMjfLlVkPKhsm+FyWZG5BmlGS7J9epdfKgaPuQEx9TItcfE7gXNIqt+5lNjKYr4bTg2nzCXihhmfP6WignRm19/oPqrsI/ZqvPUNwluTb31vcL7TkfLdzdku9LysoWq0Ww1AgMBAAECgYACG4fPi7v5uvr0TJ5aHgT8W96HJT+wAfliQCNrKxbCpkDr0N+Of+uk9miUFXLN+u55Z1rKE6FOPEFSYdwb4OSGG+7D71FuKjOxTBpo0abUXbODnDIeimJNvZpTIp1vaUpaXDoloXRM9y5p4wro9DJrJOd/fD947PZuYHnxiZ4bAQJBAPGiJmk7r882w5nfwF3BIbEtsvRymPqQIk6r7SPqybm3HKoerFsFy5vYVnzhhK9SMvXb+NHDQ1IXZSzg4H3MBo0CQQC8kyyO/5DrxijtA4yQO0HkfkEIISbX2lhaUf4EmCn8wRNCD/TH56hkHwGjTd/WdlYuzTtD8ZY9FM/IWjfLfUZJAkAIcXQKJU3FXdKD4++i1wbIXCJurDpwNu0b9qH4qVGXLbDQuPWo0JEGxw5umqq6PZMOSDtviPUnPy4H3Wu6uZBZAkAp1NQLnLG/O6QUf7cMv7hsQX2XmhMNywfScWHSDxcxVHs7KmL4fXEjVKV+XmYLIxEXLLu65LqZLiaxoSHDaDTRAkBTBXaAx2PdPw3O5i8AJMkRZAHvJ6G5svwc7TIbA0ctc7xsIMueyxMbPspC2lLmU7w+ePnTGoVvj+yG6kW0i/ok\",\"salt\":\"950a5cdf2083945fe9bc305818ca3b17\",\"secretKey\":\"bOifTH/DrZZo634V7m3ftg==\",\"sessionId\":\"cTmSaqR+N2iJJ6OpfJ+2NP9J6OimCVLaQq3wDaPTeAE=\"}\n";
        Map<String, String> map = RSAUtils.generateKey();
        String publicKey = map.get(RSAUtils.PUBLIC_KEY);
        String privateKey = map.get(RSAUtils.PRIVATE_KEY);
        System.out.println("读取的公钥是：" + publicKey);
        System.out.println("读取的私钥是：" + privateKey);
        //公钥加密
        String encrypt = RSAUtils.encrypt(json, publicKey);
        System.out.println("获取的加密串：" + encrypt);
        //私钥解密
        String decrypt = RSAUtils.decrypt(encrypt, privateKey);
        System.out.println("解密之后的数据：" + decrypt);
    }


    @Test
    void e(){
        String json = "{\"clientPublicKey\":\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDfYF/Q7d/N/0SRaAC+7AS2dfDTL7U+piCj42xgUNmM60lemQVpOGn/ujQjIhfa/hhFckx6r7PHFYPwmypvlpKJ5Ddw8+LMt1AgHBXTPTkbyfjE3CBHvzeoPkgd4Tb0FoQwJ87SKl+OcOTXponm6+MCDnswenIeFw2KZq+tQk4WHQIDAQAB\",\"privateKey\":\"MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALH9/mAuBxTE2Cy5ne73yVynOGrQHCA34bk+kj7ZDHMjfLlVkPKhsm+FyWZG5BmlGS7J9epdfKgaPuQEx9TItcfE7gXNIqt+5lNjKYr4bTg2nzCXihhmfP6WignRm19/oPqrsI/ZqvPUNwluTb31vcL7TkfLdzdku9LysoWq0Ww1AgMBAAECgYACG4fPi7v5uvr0TJ5aHgT8W96HJT+wAfliQCNrKxbCpkDr0N+Of+uk9miUFXLN+u55Z1rKE6FOPEFSYdwb4OSGG+7D71FuKjOxTBpo0abUXbODnDIeimJNvZpTIp1vaUpaXDoloXRM9y5p4wro9DJrJOd/fD947PZuYHnxiZ4bAQJBAPGiJmk7r882w5nfwF3BIbEtsvRymPqQIk6r7SPqybm3HKoerFsFy5vYVnzhhK9SMvXb+NHDQ1IXZSzg4H3MBo0CQQC8kyyO/5DrxijtA4yQO0HkfkEIISbX2lhaUf4EmCn8wRNCD/TH56hkHwGjTd/WdlYuzTtD8ZY9FM/IWjfLfUZJAkAIcXQKJU3FXdKD4++i1wbIXCJurDpwNu0b9qH4qVGXLbDQuPWo0JEGxw5umqq6PZMOSDtviPUnPy4H3Wu6uZBZAkAp1NQLnLG/O6QUf7cMv7hsQX2XmhMNywfScWHSDxcxVHs7KmL4fXEjVKV+XmYLIxEXLLu65LqZLiaxoSHDaDTRAkBTBXaAx2PdPw3O5i8AJMkRZAHvJ6G5svwc7TIbA0ctc7xsIMueyxMbPspC2lLmU7w+ePnTGoVvj+yG6kW0i/ok\",\"salt\":\"950a5cdf2083945fe9bc305818ca3b17\",\"secretKey\":\"bOifTH/DrZZo634V7m3ftg==\",\"sessionId\":\"cTmSaqR+N2iJJ6OpfJ+2NP9J6OimCVLaQq3wDaPTeAE=\"}\n";
        Map<String, String> map = RSAUtils.generateKey();
        String publicKey = map.get(RSAUtils.PUBLIC_KEY);
        String privateKey = map.get(RSAUtils.PRIVATE_KEY);
        System.out.println("读取的公钥是：" + publicKey);
        System.out.println("读取的私钥是：" + privateKey);
        //公钥加密
        String encrypt = RSAUtils.encryptSection(json, publicKey);
        System.out.println("获取的加密串：" + encrypt);
        //私钥解密
        String decrypt = RSAUtils.decryptSection(encrypt, privateKey);
        System.out.println("解密之后的数据：" + decrypt);
    }
}