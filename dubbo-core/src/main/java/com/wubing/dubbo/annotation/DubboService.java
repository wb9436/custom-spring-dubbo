package com.wubing.dubbo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 * <p>
 * 用于服务注册，将当前注解的类注册到注册中心，并封装BeanDefinition注入Spring容器
 * 表示当前被标注的接口是个服务接口
 *
 * @author: WB
 * @version: v1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DubboService {
}
