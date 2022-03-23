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
public class CustomDubboProviderApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(CustomDubboProviderApplication.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
