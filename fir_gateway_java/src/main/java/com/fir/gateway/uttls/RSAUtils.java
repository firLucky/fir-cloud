package com.fir.gateway.uttls;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


/**
 * 非对称加密工具方法
 *
 * @author fir
 */
@Slf4j
public class RSAUtils {


    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    /**
     * 类型
     */
    public static final String ENCRYPT_TYPE = "RSA";


    /**
     * 公钥名称
     */
    public static final String PUBLIC_KEY = "publicKey";


    /**
     * 私钥名称
     */
    public static final String PRIVATE_KEY = "privateKey";


    /**
     * 生成公钥私钥对
     *
     * @return 公钥私钥对
     */
    public static Map<String, String> generateKey() {
        Map<String, String> map = new HashMap<>();
        KeyPair pair = SecureUtil.generateKeyPair(ENCRYPT_TYPE);
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();


        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        map.put(PUBLIC_KEY, publicKeyStr);
        map.put(PRIVATE_KEY, privateKeyStr);

        return map;
    }


    /**
     * 从文件中读取公钥
     *
     * @param filename 公钥保存路径
     * @return 公钥字符串
     */
    public static String getPublicKey(String filename) {
        //默认UTF-8编码，可以在构造中传入第二个参数做为编码
        FileReader fileReader = new FileReader(filename);
        return fileReader.readString();
    }


    /**
     * 从文件中读取密钥
     *
     * @param filename 私钥保存路径
     * @return 私钥字符串
     */
    public static String getPrivateKey(String filename) {
        //默认UTF-8编码，可以在构造中传入第二个参数做为编码
        FileReader fileReader = new FileReader(filename);
        return fileReader.readString();
    }


    /**
     * 公钥加密
     *
     * @param content   要加密的内容
     * @param publicKey 公钥
     */
    public static String encrypt(String content, PublicKey publicKey) {
        try {
            RSA rsa = new RSA(null, publicKey);
            return rsa.encryptBase64(content, KeyType.PublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 公钥加密
     *
     * @param content   要加密的内容
     * @param publicKey 公钥（base64字符串）
     */
    public static String encrypt(String content, String publicKey) {
        try {
            RSA rsa = new RSA(null, publicKey);
            return rsa.encryptBase64(content, KeyType.PublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 公钥加密-分段加密（解密时需要分段解密）
     *
     * @param t   要加密的内容(泛型对象，转化为Json字符串加密)
     * @param publicKey 公钥（base64字符串）
     */
    public static <T> String encryptSection(T t, String publicKey) {

        String content = JSONObject.toJSONString(t);
        try {
            return encryptSection(content, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 公钥加密-分段加密（解密时需要分段解密）
     *
     * @param content   要加密的内容
     * @param publicKey 公钥（base64字符串）
     */
    public static String encryptSection(String content, String publicKey) {
        try {
            RSA rsa = new RSA(null, publicKey);

            int blockSize = 117;
            int encryptedLength = content.length();
            StringBuilder decryptedBlocks = new StringBuilder();
            // 拆分加密文本为块并逐个解密
            for (int i = 0; i < encryptedLength; i += blockSize) {
                int b = i + blockSize;
                if (b > encryptedLength) {
                    b = encryptedLength;
                }
                String block = content.substring(i, b);
                String decryptedBlock = rsa.encryptBase64(block, KeyType.PublicKey);
                decryptedBlocks.append(decryptedBlock);
            }

            return decryptedBlocks.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 私钥解密
     *
     * @param content    要解密的内容
     * @param privateKey 私钥
     */
    public static String decrypt(String content, PrivateKey privateKey) {
        try {
            RSA rsa = new RSA(privateKey, null);
            return rsa.decryptStr(content, KeyType.PrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 私钥解密
     *
     * @param content    要解密的内容
     * @param privateKey 私钥（base64字符串）
     */
    public static String decrypt(String content, String privateKey) {

        try {
            RSA rsa = new RSA(privateKey, null);
            return rsa.decryptStr(content, KeyType.PrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 私钥解密-（只能解密分段解密的数据）
     *
     * @param content    要解密的内容 （只能解密分段解密的数据）
     * @param privateKey 私钥（base64字符串）
     */
    public static String decryptSection(String content, String privateKey) {
        try {
            RSA rsa = new RSA(privateKey, null);

            int blockSize = 172;
            int encryptedLength = content.length();
            StringBuilder decryptedBlocks = new StringBuilder();
            // 拆分加密文本为块并逐个解密
            for (int i = 0; i < encryptedLength; i += blockSize) {
                int b = i + blockSize;
                if (b > encryptedLength) {
                    b = encryptedLength;
                }
                String block = content.substring(i, b);
                String decryptedBlock = rsa.decryptStr(block, KeyType.PrivateKey);
                decryptedBlocks.append(decryptedBlock);

            }

            return decryptedBlocks.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取公私钥-请获取一次后保存公私钥使用
     *
     * @param publicKeyFilename  公钥生成的路径
     * @param privateKeyFilename 私钥生成的路径
     */
    public static void generateKeyPair(String publicKeyFilename, String privateKeyFilename) {
        try {
            KeyPair pair = SecureUtil.generateKeyPair(ENCRYPT_TYPE);
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();
            // 获取 公钥和私钥 的 编码格式（通过该 编码格式 可以反过来 生成公钥和私钥对象）
            byte[] pubEncBytes = publicKey.getEncoded();
            byte[] priEncBytes = privateKey.getEncoded();

            // 把 公钥和私钥 的 编码格式 转换为 Base64文本 方便保存
            String pubEncBase64 = new BASE64Encoder().encode(pubEncBytes);
            String priEncBase64 = new BASE64Encoder().encode(priEncBytes);

            FileWriter pub = new FileWriter(publicKeyFilename);
            FileWriter pri = new FileWriter(privateKeyFilename);
            pub.write(pubEncBase64);
            pri.write(priEncBase64);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // - - - - - - - - - - - - - - - - - - - - SIGN 签名，验签 - - - - - - - - - - - - - - - - - - - - //

    /**
     * 加签：生成报文签名
     *
     * @param content    报文内容
     * @param privateKey 私钥
     * @param encode     编码
     * @return 签名
     */
    public static String doSign(String content, String privateKey, String encode) {
        try {
            String unSign = Base64.encodeBase64String(content.getBytes(StandardCharsets.UTF_8));
            byte[] privateKeys = Base64.decodeBase64(privateKey.getBytes());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeys);
            KeyFactory mykeyFactory = KeyFactory.getInstance(ENCRYPT_TYPE);
            PrivateKey psbcPrivateKey = mykeyFactory.generatePrivate(privateKeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(psbcPrivateKey);
            signature.update(unSign.getBytes(encode));
            byte[] signed = signature.sign();
            return Base64.encodeBase64String(signed);
        } catch (Exception e) {
            log.error("生成报文签名出现异常");
        }
        return null;
    }


    /**
     * 验证：验证签名信息
     *
     * @param content   签名报文
     * @param signed    签名信息
     * @param publicKey 公钥
     * @param encode    编码格式
     * @return 通过/失败
     */
    public static boolean doCheck(String content, String signed, PublicKey publicKey, String encode) {
        try {
            // 解密之前先把content明文，进行base64转码
            String unsigned = Base64.encodeBase64String(content.getBytes(encode));
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(unsigned.getBytes(encode));
            return signature.verify(Base64.decodeBase64(signed));
        } catch (Exception e) {
            log.error("报文验证签名出现异常");
        }
        return false;
    }
}
