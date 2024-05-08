package com.fir.gateway.uttls;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author fir
 * @date 2023/7/14 13:23
 */
class SaltedHashUtilsTest {

    @Test
    void a() {
        String password = "password";
        String salt = SaltedHashUtils.generateSalt();
        System.out.println(salt);
        String hashedPassword = SaltedHashUtils.generateHash(password, salt);
        System.out.println(hashedPassword);

        // 验证密码
        boolean isValid = SaltedHashUtils.validatePassword(password, salt, hashedPassword);
        System.out.println("Password is valid: " + isValid);
    }
}