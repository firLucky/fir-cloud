package com.fir.seata.feign;

import com.fir.seata.config.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Program Name: springcloud-nacos-seata
 * <p>
 * Description:
 * <p>
 *
 * @author fir
 * @version 1.0
 */
@FeignClient(name = "AD")
public interface AccountFeignClient {

    @PostMapping("/test/save")
    Result reduce(@RequestParam("num") Integer num, @RequestParam("state") Integer state);
}
