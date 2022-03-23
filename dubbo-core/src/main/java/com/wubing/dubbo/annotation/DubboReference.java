package com.wubing.dubbo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 * <p>
 * 用于服务发现，并将当前注解的FactoryBean注入Spring容器，并实现依赖注入
 * 表示当前被标注的接口属性为是个远程服务
 *
 * @author: WB
 * @version: v1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DubboReference {

}
