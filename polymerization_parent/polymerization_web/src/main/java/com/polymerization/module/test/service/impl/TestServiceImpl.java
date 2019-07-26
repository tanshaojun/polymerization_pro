package com.polymerization.module.test.service.impl;

import com.polymerization.module.test.mapper.TestMapper;
import com.polymerization.module.test.model.Test;
import com.polymerization.module.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/26 10:29
 */
@Service("testService")
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public List<Test> findAll() {
        return testMapper.selectAll();
    }
}
