package com.wubing.service.impl;

import com.wubing.dubbo.annotation.DubboService;
import com.wubing.service.HelloService;

/**
 * @author: WB
 * @version: v1.0
 */
@DubboService
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String username) {
        return "hello, " + username;
    }
}
