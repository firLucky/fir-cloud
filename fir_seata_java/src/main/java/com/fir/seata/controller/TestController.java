package com.fir.seata.controller;

import com.fir.seata.config.CommonException;
import com.fir.seata.config.Result;
import com.fir.seata.entity.Test;
import com.fir.seata.feign.AccountFeignClient;
import com.fir.seata.service.ITestService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dpe
 * @since 2023-04-17
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private ITestService iTestService;

    @Resource
    private AccountFeignClient accountFeignClient;


    @GetMapping("/list")
    public Result testInfo(){
        return Result.success(iTestService.list());
    }


    @GlobalTransactional(rollbackFor ={ Exception.class, CommonException.class})
    @PostMapping("/save")
    public Result aa(Integer num, Integer state) throws InterruptedException {
        Test test = Test.builder()
                .num(num)
                .build();
        iTestService.save(test);
        accountFeignClient.reduce(num, state - 1);
        if(state == 1){
            throw new CommonException("数据操作异常");
        }

        return Result.success("");
    }
}

