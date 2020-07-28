package polymerization.module.test.serivce.impl;

import org.springframework.stereotype.Service;
import polymerization.module.test.serivce.TestService;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2020/7/28 13:48
 */
@Service("testService")
public class TestServiceImpl implements TestService {

    @Override
    public String test() {
        return "我是test............";
    }

}
