package com.wubing;

import com.wubing.dubbo.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: WB
 * @version: v1.0
 */
@SpringBootApplication
//开启自定义Dubbo支持
@EnableDubbo
public class CustomDubboConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomDubboConsumerApplication.class);
    }

}
