package com.fir.gateway.uttls;

import org.springframework.security.crypto.bcrypt.BCrypt;
import java.security.SecureRandom;


/**
 * 盐加密函数函数
 *
 * @author fir
 * @date 2023/7/13 21:19
 */
public class SaltedHashUtils {


    /**
     * 盐的长度
     */
    private static final int SALT_LENGTH = 16;


    /**
     * 盐值生成
     *
     * @return 16位盐值
     */
    public static String generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return bytesToHex(salt);
    }


    /**
     * 密码加盐
     *
     * @param password 密码
     * @param salt 盐值
     * @return 加盐数值
     */
    public static String generateHash(String password, String salt) {
        String saltedPassword = salt + password;
        return BCrypt.hashpw(saltedPassword, BCrypt.gensalt());
    }


    /**
     * 密码，盐 对比 盐后数值 是否相同
     *
     * @param password 密码
     * @param salt 盐值
     * @return 相同:true/不相同:false
     */
    public static boolean validatePassword(String password, String salt, String hashedPassword) {
        String saltedPassword = salt + password;
        return BCrypt.checkpw(saltedPassword, hashedPassword);
    }


    /**
     * 暂时未知
     *
     * @param bytes 字节流
     * @return 字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
