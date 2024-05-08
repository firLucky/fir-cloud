package com.fir.gateway.uttls;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author fir
 */
public class SignatureUtils {

    /**
     * 解密前端防重放校验的签名密钥（失败未验证）
     *
     * @param signature 签名密钥
     * @param secretKey 加密密钥
     * @return 返回解密结果
     */
    public static String decryptSignature(String signature, String secretKey) {
        try {
            // 使用HMAC-SHA256算法
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKeySpec);

            // 对签名进行Base64解码
            byte[] signatureBytes = Base64.getDecoder().decode(signature);

            // 进行解密操作
            byte[] decryptedBytes = sha256Hmac.doFinal(signatureBytes);

            // 将解密结果转为字符串
            String decryptedSignature = new String(decryptedBytes, StandardCharsets.UTF_8);

            return decryptedSignature;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 解密前端防重放校验的签名密钥
     *
     * @param signature 签名密钥
     * @return 返回解密结果
     */
    public static String decryptSignatureBase64(String signature) {
        try {
            // 对签名进行Base64解码
            byte[] signatureBytes = Base64.getDecoder().decode(signature);

            // 将解密结果转为字符串

            return new String(signatureBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
