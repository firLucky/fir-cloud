package com.fir.gateway.uttls;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;


/**
 * 功能：AES 工具类
 * 说明：对称分组密码算法
 * @author fir
 * @date 2020-5-20 11:25
 */
@Slf4j
@SuppressWarnings("all")
public class AESUtils {
    private static final Logger logger = LoggerFactory.getLogger(AESUtils.class);

    public final static String KEY_ALGORITHMS = "AES";
    public final static int KEY_SIZE = 128;

    /**
     * 生成AES密钥，base64编码格式 (128)
     * @return
     * @throws Exception
     */
    public static String getKeyAES_128() throws Exception{
        KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHMS);
        keyGen.init(KEY_SIZE);
        SecretKey key = keyGen.generateKey();
        String base64str = Base64.encodeBase64String(key.getEncoded());
        return base64str;
    }

    /**
     * 生成AES密钥，base64编码格式 (256)
     * @return
     * @throws Exception
     */
    public static String getKeyAES_256() throws Exception{
        // 256需要换jar包暂时用128
        String base64str = getKeyAES_128();
        return base64str;
    }

    /**
     * 根据base64Key获取SecretKey对象
     * @param base64Key
     * @return
     */
    public static SecretKey loadKeyAES(String base64Key) {
        byte[] bytes = Base64.decodeBase64(base64Key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, KEY_ALGORITHMS);
        return secretKeySpec;
    }


    /**
     * 生成SecretKey, base64对象
     *
     * @return
     */
    public static String generateKeyAES() {
        String keyBase64Str = null;
        String base64Key = "";
        try {
            base64Key = AESUtils.getKeyAES_128();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("AES密钥生成失败");
        }
        if(!base64Key.equals("")){
            byte[] bytes = Base64.decodeBase64(base64Key);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, KEY_ALGORITHMS);
            keyBase64Str = Base64.encodeBase64String(secretKeySpec.getEncoded());
        }
        return keyBase64Str;
    }


    /**
     * AES 加密字符串，SecretKey对象
     *
     * @param encryptData
     * @param key
     * @param encode
     * @return
     */
    public static String encrypt(String encryptData, SecretKey key, String encode) {
        try {
            final Cipher cipher = Cipher.getInstance(KEY_ALGORITHMS);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptBytes = encryptData.getBytes(encode);
            byte[] result = cipher.doFinal(encryptBytes);
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            logger.error("加密异常:" + e.getMessage());
            return null;
        }
    }

    /**
     * AES 加密字符串，base64Key对象
     *
     * @param encryptData
     * @param base64Key
     * @param encode
     * @return
     */
    public static String encrypt(String encryptData, String base64Key, String encode) {
        SecretKey key = loadKeyAES(base64Key);
        try {
            final Cipher cipher = Cipher.getInstance(KEY_ALGORITHMS);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptBytes = encryptData.getBytes(encode);
            byte[] result = cipher.doFinal(encryptBytes);
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            logger.error("加密异常:" + e.getMessage());
            return null;
        }
    }

    /**
     * AES 加密字符串，base64Key对象
     *
     * @param encryptData
     * @param base64Key
     * @return
     */
    public static String encrypt(String encryptData, String base64Key) {
        SecretKey key = loadKeyAES(base64Key);
        try {
            final Cipher cipher = Cipher.getInstance(KEY_ALGORITHMS);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptBytes = encryptData.getBytes(String.valueOf(StandardCharsets.UTF_8));
            byte[] result = cipher.doFinal(encryptBytes);
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            logger.error("加密异常:" + e.getMessage());
            return null;
        }
    }

    /**
     * AES 解密字符串，SecretKey对象
     *
     * @param decryptData
     * @param key
     * @param encode
     * @return
     */
    public static String decrypt(String decryptData, SecretKey key, String encode) {
        try {
            final Cipher cipher = Cipher.getInstance(KEY_ALGORITHMS);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptBytes = Base64.decodeBase64(decryptData);
            byte[] result = cipher.doFinal(decryptBytes);
            return new String(result, encode);
        } catch (Exception e) {
            logger.error("加密异常:" + e.getMessage());
            return null;
        }
    }

    /**
     * AES 解密字符串，base64Key对象
     *
     * @param decryptData
     * @param base64Key
     * @param encode
     * @return
     */
    public static String decrypt(String decryptData, String base64Key, String encode) {
        SecretKey key = loadKeyAES(base64Key);
        try {
            final Cipher cipher = Cipher.getInstance(KEY_ALGORITHMS);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptBytes = Base64.decodeBase64(decryptData);
            byte[] result = cipher.doFinal(decryptBytes);
            return new String(result, encode);
        } catch (Exception e) {
            logger.error("加密异常:" + e.getMessage());
            return null;
        }
    }


    /**
     * AES 解密字符串，base64Key对象
     *
     * @param decryptData
     * @param base64Key
     * @return
     */
    public static String decrypt(String decryptData, String base64Key) {
        SecretKey key = loadKeyAES(base64Key);
        try {
            final Cipher cipher = Cipher.getInstance(KEY_ALGORITHMS);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptBytes = Base64.decodeBase64(decryptData);
            byte[] result = cipher.doFinal(decryptBytes);
            return new String(result, String.valueOf(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error("解密异常:" + e.getMessage());
            return null;
        }
    }
}