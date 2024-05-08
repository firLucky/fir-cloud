package com.fir.gateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;

/**
 * @author fir
 * @date 2023/7/31 15:06
 */
@SpringBootTest
class GlobalConfigTest {


    @Resource
    private GlobalConfig globalConfig;

    @Test
    void readingInfo() {


        System.out.println(globalConfig);
    }
}