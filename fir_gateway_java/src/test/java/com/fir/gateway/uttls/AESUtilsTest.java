package com.fir.gateway.uttls;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@RunWith(SpringRunner.class)
//@ActiveProfiles("ut")
//@SpringBootTest
class AESUtilsTest {

    @SneakyThrows
    @Test
    void getKeyAES_128() {

        String key = AESUtils.getKeyAES_128();
        System.out.println("密钥为：" + key);

        String str = "加密字符串";
        System.out.println("加密前：" + str);

        str = AESUtils.encrypt(str, key);
        System.out.println("加密后："+ str);

        str = AESUtils.decrypt(str, key);
        System.out.println("解密后："+ str);
    }

    @SneakyThrows
    @Test
    void testGetKeyAES_128() {
    }

    @Test
    void getKeyAES_256() {
    }

    @SneakyThrows
    @Test
    void loadKeyAES() {
        String getKeyAES_128b = AESUtils.getKeyAES_128();
        SecretKey aa = AESUtils.loadKeyAES(getKeyAES_128b);
        System.out.println(aa.getAlgorithm());
        System.out.println(aa.getFormat());
        System.out.println(Arrays.toString(aa.getEncoded()));
        System.out.println(aa);

    }

    @Test
    void b() {
        String content = "";
        String key = "";
        System.out.println(content);
        // 自解密测试
        String contentNew = AESUtils.decrypt(key, content, String.valueOf(StandardCharsets.UTF_8));
        System.out.println(contentNew);
    }

    @Test
    void testEncrypt() {
    }

    @Test
    void decrypt() {
    }

    @Test
    void testDecrypt() {
    }
}