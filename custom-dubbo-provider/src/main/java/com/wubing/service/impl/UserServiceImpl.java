package com.wubing.service.impl;

import com.wubing.service.HelloService;
import com.wubing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: WB
 * @version: v1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private HelloService helloService;

    @Override
    public String sayHello(String username) {
        System.out.println(helloService.sayHello("张龙赵虎"));

        return "hello, " + username;
    }
}
