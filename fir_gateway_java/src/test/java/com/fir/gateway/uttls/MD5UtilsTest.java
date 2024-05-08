package com.fir.gateway.uttls;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import java.io.File;


/**
 * @author fir
 * @date 2023/7/17 12:43
 */
class MD5UtilsTest {


    @SneakyThrows
    @Test
    void aa() {
        System.out.println(MD5Utils.generateMd5ForString("test"));
        System.out.println(MD5Utils.generateMd5ForFile(new File("1.jpg")));
    }
}