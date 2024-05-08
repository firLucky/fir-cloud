package com.fir.seata.service.impl;

import com.fir.seata.entity.Test;
import com.fir.seata.mapper.TestMapper;
import com.fir.seata.service.ITestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dpe
 * @since 2023-04-17
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements ITestService {

}
