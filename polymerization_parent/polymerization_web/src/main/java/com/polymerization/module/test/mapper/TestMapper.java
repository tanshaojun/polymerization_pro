package com.polymerization.module.test.mapper;

import com.polymerization.module.test.model.Test;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface TestMapper extends Mapper<Test> {

}
